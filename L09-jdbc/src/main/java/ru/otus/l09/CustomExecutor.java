package ru.otus.l09;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.sql.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Class with simple ORM functionality
 * Used for saving into and loading from DB {@link DataSet} successors
 */
public class CustomExecutor {

    private final Connection connection;
    private Logger log = LoggerFactory.getLogger(CustomExecutor.class);

    public CustomExecutor(Connection connection) {
        this.connection = connection;
    }

    public <T extends DataSet> void save(T user) throws Exception {

        String tableName = getTableName(user.getClass());

        Field[] fields = user.getClass().getDeclaredFields();

        Map<String, String> columns = getTableColumns(fields);
        createTableIfNotExist(tableName, columns);

        if (isRowExists(tableName, user.getId())) {
            updateRow(tableName, user);
        } else {
            insertRow(tableName, user);
        }
    }

    public <T extends DataSet> T load(long id, Class<T> clazz) throws Exception {

        String tableName = getTableName(clazz);

        try (Statement st = connection.createStatement()) {
            st.execute("SELECT * FROM " + tableName + " WHERE id=" + id);

            ResultSet rs = st.getResultSet();
            ResultSetMetaData md = rs.getMetaData();

            T dataSet = clazz.getConstructor().newInstance();
            dataSet.setId(id);
            while (rs.next()) {
                for (int i = 1; i <= md.getColumnCount(); i++) {
                    try {
                        Field f = clazz.getDeclaredField(md.getColumnName(i).toLowerCase());
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                        f.set(dataSet, getColumnValue(rs, md.getColumnType(i), i));
                    } catch (NoSuchFieldException e) {
                        log.warn("Getting class field error", e);
                    }
                }
            }
            return dataSet;
        }
    }

    private void createTableIfNotExist(String tableName, Map<String, String> columns) throws Exception {

        try (Statement st = connection.createStatement()) {
            StringBuilder sb = new StringBuilder();
            sb.append("CREATE TABLE IF NOT EXISTS ")
               .append(tableName)
               .append(" ")
               .append("(id BIGINT(20) NOT NULL AUTO_INCREMENT");

            for (Map.Entry<String, String> column: columns.entrySet()) {
                sb.append(", ")
                  .append(column.getKey())
                  .append(" ")
                  .append(column.getValue());
            }
            sb.append(")");

            log.trace("Statement: {}", sb.toString());
            st.execute(sb.toString());

        } catch (SQLException e) {
            log.error("Table creation error", e);
            throw e;
        }
    }

    private boolean isRowExists(String tableName, long id) throws Exception {

        try (Statement st = connection.createStatement()) {
            StringBuilder sb = new StringBuilder();

            sb.append("SELECT * FROM ")
              .append(tableName)
              .append(" WHERE id=")
              .append(id);

            log.trace("Statement: {}", sb.toString());
            st.execute(sb.toString());

            ResultSet rs = st.getResultSet();
            return rs.next();

        } catch (SQLException e) {
            log.error("Checking if row exists error ", e);
            throw e;
        }
    }

    private <T extends DataSet> void updateRow(String tableName, T user) throws Exception {

        Field[] fields = user.getClass().getDeclaredFields();

        try (Statement st = connection.createStatement()) {
            StringBuilder sb = new StringBuilder();
            sb.append("UPDATE ")
              .append(tableName)
              .append(" SET ")
              .append(String.join(", ", getNamesValuesFromObjectFields(fields, user)))
              .append(" WHERE id=").append(user.getId());

            log.trace("Statement: {}", sb.toString());
            st.execute(sb.toString());

        } catch (SQLException e) {
            log.error("Updating row error ", e);
            throw e;
        }
    }

    private <T extends DataSet> void insertRow(String tableName, T user) throws Exception {

        Field[] fields = user.getClass().getDeclaredFields();

        try (Statement st = connection.createStatement()) {
            StringBuilder sb = new StringBuilder();
            sb.append("INSERT INTO ")
              .append(tableName)
              .append(" (")
              .append("id, ")
              .append(String.join(", ", getFieldsNamesAsStringArray(fields)))
              .append(") VALUES (")
              .append(user.getId())
              .append(", ")
              .append(String.join(", ", getObjectFieldsValuesAsStringArray(fields, user)))
              .append(")");

            log.trace("Statement: {}", sb.toString());
            st.execute(sb.toString());

            log.debug("Saved: {}", st.getUpdateCount());

        } catch (SQLException e) {
            log.error("Inserting row error ", e);
            throw e;
        }
    }

    /**
     -7	BIT
     -6	TINYINT
     -5	BIGINT
     -4	LONGVARBINARY
     -3	VARBINARY
     -2	BINARY
     -1	LONGVARCHAR
     0	NULL
     1	CHAR
     2	NUMERIC
     3	DECIMAL
     4	INTEGER
     5	SMALLINT
     6	FLOAT
     7	REAL
     8	DOUBLE
     12	VARCHAR
     91	DATE
     92	TIME
     93	TIMESTAMP
     1111 	OTHER
     */
    private Object getColumnValue(ResultSet rs, int columnType, int columnIndex) throws Exception {
        log.trace("Column #{} type: {}", columnIndex, columnType);
        switch (columnType) {
            case 12:
                return rs.getString(columnIndex);
            case 2:
            case -5:
                return rs.getLong(columnIndex);
            case 5:
            case 4:
                return rs.getInt(columnIndex);
            default:
                log.error("Not supported column type: {}", columnType);
                return null;
        }
    }

    private String javaTypeNameToSQLColumnType(String typeName) throws Exception {
        log.trace("Try to convert typeName: {}", typeName);
        switch (typeName) {
            case "java.lang.String":
                return "VARCHAR(255)";
            case "int":
                return "INT";
            case "long":
                return "BIGINT(20)";
            default:
                throw new RuntimeException("Unsupported type: " + typeName);
        }
    }

    private String getFieldValue(Field field, Object obj) throws Exception {
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        String fieldTypeName = field.getType().getName();
        log.trace("get value from {} field", fieldTypeName);
        switch (fieldTypeName) {
            case "java.lang.String":
                return "\'" + field.get(obj) + "\'" ;
            case "int":
                return String.valueOf(field.getInt(obj));
            case "long":
                return String.valueOf(field.getLong(obj));
                default:
                    throw new RuntimeException("Unsupported filed type");
        }
    }

    private <T extends DataSet> String getTableName(Class<T> clazz) {
        return clazz.getName().replace('.', '_');
    }

    /**
     * @param classFields - array of class fields
     * @return  Map with string "column name" "column type" pairs
     */
    private Map<String, String> getTableColumns(Field[] classFields) throws Exception {
        Map<String, String> types = new HashMap<>();
        for (Field field : classFields) {
            types.put(field.getName(), javaTypeNameToSQLColumnType(field.getType().getTypeName()));
        }
        return types;
    }

    private String[] getFieldsNamesAsStringArray(Field[] fields) {
        String[] names = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            names[i] = fields[i].getName();
        }
        return names;
    }

    private String[] getObjectFieldsValuesAsStringArray(Field[] fields, Object obj) throws Exception {
        String[] values = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            values[i] = getFieldValue(fields[i], obj);
        }
        return values;
    }

    private String[] getNamesValuesFromObjectFields(Field[] fields, Object obj) throws Exception {
        String[] namesValues = new String[fields.length];
        for (int i = 0; i < fields.length; i++) {
            Field f = fields[i];
            namesValues[i] = f.getName() + "=" + getFieldValue(f, obj);
        }
        return namesValues;
    }
}
