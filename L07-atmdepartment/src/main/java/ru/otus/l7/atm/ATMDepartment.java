package ru.otus.l7.atm;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l7.banknotes.BanknotesSelection;

import java.util.ArrayList;
import java.util.List;

public class ATMDepartment {

    private static final int DEFAULT_NUMBER_OF_ATMS = 5;
    private List<ATM> atmList = new ArrayList<>();

    private static final Logger log = LoggerFactory.getLogger(ATMDepartment.class);

    public ATMDepartment() {
        for (int i = 0; i < DEFAULT_NUMBER_OF_ATMS; i++) {
            ATM atm = new ATM(new MinNumberOfBanknotesStrategy());
            atm.load(ATMContentFactory.createAtmContent());
            atmList.add(atm);
        }
        log.info("{} ATMs added", atmList.size());
    }

    public int getATMsTotal() {
        if (atmList.isEmpty()) return 0;

        int total = 0;
        for (ATM atm : atmList) {
            total += atm.getTotal();
        }
        log.info("Get ATM total: {}", total);
        return total;
    }

    public void addATM(ATM atm) {
        atmList.add(atm);
        log.info("ATM added: {}", atm);
    }

    public ATM getATM(String name) {
        for (ATM atm : atmList) {
            if (atm.getName().equalsIgnoreCase(name)) {
                return atm;
            }
        }
        return null;
    }

    public List<ATM> getATMs() {
        return atmList;
    }

    public void restoreAllATMs() {
        log.info("Restore all ATMs");
        log.info("Balance: {}", getATMsTotal());
        for (ATM atm : atmList) {
            restoreATM(atm);
        }
        log.info("New balance: {}", getATMsTotal());
    }

    public void restoreATM(ATM atm) {
        int atmBalance = 0;
        BanknotesSelection bs = atm.empty();
        if (bs != null) {
            atmBalance = bs.getSum();
        }
        atm.load(ATMContentFactory.createAtmContent());
        log.info("{} Balance: {}", atm.getName(), atmBalance);
    }

}
