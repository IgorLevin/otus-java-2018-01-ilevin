package ru.otus.l7.atm;

import ru.otus.l7.BanknotesSelection;

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
    public BanknotesSelection withdraw(int sumToWithdraw) {

        BanknotesSelection bs = new BanknotesSelection();

        int sumLeft = sumToWithdraw;

        for (AtmCell cell : cells) {
            int nominal = cell.getNominal();
            while (sumLeft > 0 && nominal <= sumLeft && !cell.isEmpty()) {
                cell.pullBanknote();
                bs.putBanknote(nominal);
                sumLeft = sumLeft - nominal;
            }
            if (sumLeft == 0) break;
        }

        if (sumLeft > 0) {
            return null;
        } else {
            return bs;
        }
    }
}
