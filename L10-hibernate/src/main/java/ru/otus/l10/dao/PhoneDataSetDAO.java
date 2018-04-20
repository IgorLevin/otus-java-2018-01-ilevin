package ru.otus.l10.dao;

import org.hibernate.Session;
import ru.otus.l10.dataset.PhoneDataSet;
import ru.otus.l10.dataset.UserDataSet;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.util.List;

public class PhoneDataSetDAO {

    private Session session;

    public PhoneDataSetDAO(Session session) {
        this.session = session;
    }

    public void save(PhoneDataSet dataSet) {
        session.save(dataSet);
    }

    public PhoneDataSet read(long id) {
        return session.load(PhoneDataSet.class, id);
    }

    public List<PhoneDataSet> readAll() {
        CriteriaBuilder builder = session.getCriteriaBuilder();
        CriteriaQuery<PhoneDataSet> criteria = builder.createQuery(PhoneDataSet.class);
        criteria.from(PhoneDataSet.class);
        return session.createQuery(criteria).list();
    }

    public void delete(PhoneDataSet dataSet) {
        session.delete(dataSet);
    }
}
