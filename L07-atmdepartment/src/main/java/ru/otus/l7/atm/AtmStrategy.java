package ru.otus.l7.atm;

import ru.otus.l7.banknotes.BanknotesSelection;

import java.util.List;

public interface AtmStrategy {
    BanknotesSelection withdraw(List<AtmCell> cellList, int sumToWithdraw);
}
