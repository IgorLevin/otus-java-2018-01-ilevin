package ru.otus.l51;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l51.annotations.After;
import ru.otus.l51.annotations.Before;
import ru.otus.l51.annotations.Test;

public class TestSuite {

    private Logger log = LoggerFactory.getLogger(TestSuite.class);

    @Before
    public void before1Method() {
        log.info("before 1 called");
    }

    @Before
    public void before2Method() {
        log.info("before 2 called");
    }

    @After
    public void after1Method() {
        log.info("after 1 called");
    }

    @After
    public void after2Method() {
        log.info("after 2 called");
    }

    @Test
    public void test1Method() {
        log.info("test 1 called");
    }

    @Test
    public void test2Method() {
        log.info("test 2 called");
    }

}
