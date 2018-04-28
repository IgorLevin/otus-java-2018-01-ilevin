#!/bin/sh
java -cp h2-1.4.197.jar org.h2.tools.Shell -url "jdbc:h2:file:./test" -user sa -sql "CREATE TABLE IF NOT EXISTS USER (id BIGINT(20) AUTO_INCREMENT, name VARCHAR(255), age INT, PRIMARY KEY(id));"
