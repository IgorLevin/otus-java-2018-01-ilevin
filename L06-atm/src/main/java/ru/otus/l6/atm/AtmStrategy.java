package ru.otus.l6.atm;

import java.util.List;
import java.util.Map;

public interface AtmStrategy {
    void init(List<AtmCell> cellList);
    Map<Integer, Integer> withdraw(int sumToWithdraw);
}
