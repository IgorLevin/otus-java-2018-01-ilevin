package ru.otus.l7.atm;

import ru.otus.l7.BanknotesSelection;

import java.util.List;
import java.util.Map;

public interface AtmStrategy {
    void init(List<AtmCell> cellList);
    BanknotesSelection withdraw(int sumToWithdraw);
}
