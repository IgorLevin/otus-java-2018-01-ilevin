package ru.otus.l6.atm;

import java.util.*;

public class ATM {

    private List<AtmCell> cells = new ArrayList<>();
    private AtmStrategy strategy;

    public ATM() {
    }

    public ATM(List<AtmCell> cells, AtmStrategy strategy) {
        this.cells = cells;
        this.cells.sort((o1, o2) -> o2.getNominal() - o1.getNominal());
        this.strategy = strategy;
        strategy.init(this.cells);
    }

    public void setStrategy(AtmStrategy strategy) {
        this.strategy = strategy;
    }

    public Map<Integer, Integer> withdraw(int sumToWithdraw) {
        if (cells.isEmpty() || strategy == null) throw new RuntimeException("System error");
        List<AtmCell> tmp = copyCells(cells);
        strategy.init(cells);
        Map<Integer, Integer> bunchOfBanknotes = strategy.withdraw(sumToWithdraw);
        if (bunchOfBanknotes == null) {
            cells = tmp;
            return null;
        } else {
            return bunchOfBanknotes;
        }
    }

    public Map<Integer, Integer> getState() {
        Map<Integer, Integer> reminder = new TreeMap<>();
        for (AtmCell cell : cells) {
            reminder.put(cell.getNominal(), cell.getNumberOfBanknotes());
        }
        return reminder;
    }

    public void fill(Map<Integer, Integer> bunchOfBanknotes) {
        for (AtmCell cell : cells) {
            if (bunchOfBanknotes.containsKey(cell.getNominal())) {
                cell.putBanknotes(bunchOfBanknotes.get(cell.getNominal()));
            }
        }
    }

    public void putBanknotes(int nominal, int numOfBanknotes) {
        for(AtmCell cell : cells) {
            if (cell.getNominal() == nominal) {
                cell.putBanknotes(numOfBanknotes);
                break;
            }
        }
    }

    public void putBanknote(int nominal) throws RuntimeException {
        for(AtmCell cell : cells) {
            if (cell.getNominal() == nominal) {
                if (cell.getNumberOfBanknotes() == cell.getCapacity()) {
                    throw new RuntimeException("Cell is full");
                }
                cell.putBanknotes(1);
                break;
            }
        }
    }

    private List<AtmCell> copyCells(List<AtmCell> cells) {
        List<AtmCell> cellsCopy = new ArrayList<>();
        for (AtmCell cell : cells) {
            cellsCopy.add(new AtmCell(cell));
        }
        return cellsCopy;
    }
}
