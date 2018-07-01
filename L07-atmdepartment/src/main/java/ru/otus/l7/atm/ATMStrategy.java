package ru.otus.l7.atm;

import ru.otus.l7.banknotes.BanknotesSelection;

import java.util.List;

public interface ATMStrategy {
    BanknotesSelection withdraw(List<ATMCell> cellList, int sumToWithdraw);
}
