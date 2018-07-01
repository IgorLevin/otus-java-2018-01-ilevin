package ru.otus.l7.atm;

import ru.otus.l7.banknotes.BanknotesSelection;
import ru.otus.l7.banknotes.Nominal;

import java.util.List;

public class MinNumberOfBanknotesStrategy implements ATMStrategy {

    @Override
    public BanknotesSelection withdraw(List<ATMCell> cells, int sumToWithdraw) {

        BanknotesSelection bs = new BanknotesSelection();

        int sumLeft = sumToWithdraw;

        for (ATMCell cell : cells) {
            Nominal nominal = cell.getNominal();
            while (sumLeft > 0 && nominal.asNnt() <= sumLeft && !cell.isEmpty()) {
                cell.pullBanknote();
                bs.putBanknote(nominal);
                sumLeft = sumLeft - nominal.asNnt();
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
