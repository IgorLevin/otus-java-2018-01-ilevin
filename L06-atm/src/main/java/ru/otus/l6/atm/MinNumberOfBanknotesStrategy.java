package ru.otus.l6.atm;

import java.util.*;

public class MinNumberOfBanknotesStrategy implements AtmStrategy {

    private List<AtmCell> cells;

    public MinNumberOfBanknotesStrategy() {
    }

    @Override
    public void init(List<AtmCell> cellList) {
        this.cells = cellList;
    }

    @Override
    public Map<Integer, Integer> withdraw(int sumToWithdraw) {

        Map<Integer, Integer> bunchOfBanknotes = new TreeMap<>();

        int sumLeft = sumToWithdraw;

        for (AtmCell cell : cells) {
            int nominal = cell.getNominal();
            while (sumLeft > 0 && nominal <= sumLeft && cell.hasBanknotes()) {
                Integer num = bunchOfBanknotes.getOrDefault(nominal, 0);
                bunchOfBanknotes.put(nominal, num + 1);
                sumLeft = sumLeft - nominal;
                cell.pullBanknote();
            }
            if (sumLeft == 0) break;
        }

        if (sumLeft > 0) {
            return null;
        } else {
            return bunchOfBanknotes;
        }
    }
}
