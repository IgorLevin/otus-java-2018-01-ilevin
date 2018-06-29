package ru.otus.l7;

import ru.otus.l7.atm.ATM;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class ATMDepartment {

    private List<ATM> atmList = new ArrayList<>();

    public int getATMsTotal() {

        if (atmList.isEmpty()) return 0;

        int total = 0;
        for (ATM atm : atmList) {
            total += atm.getTotal();
        }
        return total;
    }

    public void addATM(ATM atm) {
        atmList.add(atm);
    }

    public void restoreAllATMs() {

        Map<Integer, Integer> bunchOfBanknotes = new TreeMap<>();
        bunchOfBanknotes.put(100, 100);
        bunchOfBanknotes.put(50, 100);
        bunchOfBanknotes.put(25, 100);
        bunchOfBanknotes.put(10, 100);
        bunchOfBanknotes.put(5, 100);
        bunchOfBanknotes.put(2, 100);
        bunchOfBanknotes.put(1, 100);

        for (ATM atm : atmList) {
            atm.fill(bunchOfBanknotes);
        }
    }

    public void restoreATM(ATM atm) {

        Map<Integer, Integer> bunchOfBanknotes = new TreeMap<>();
        bunchOfBanknotes.put(100, 100);
        bunchOfBanknotes.put(50, 100);
        bunchOfBanknotes.put(25, 100);
        bunchOfBanknotes.put(10, 100);
        bunchOfBanknotes.put(5, 100);
        bunchOfBanknotes.put(2, 100);
        bunchOfBanknotes.put(1, 100);
    }

}
