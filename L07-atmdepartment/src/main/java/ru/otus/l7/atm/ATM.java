package ru.otus.l7.atm;

import ru.otus.l7.banknotes.BanknotesSelection;
import ru.otus.l7.banknotes.BunchOfBanknotes;
import ru.otus.l7.banknotes.Nominal;

import java.util.*;

public class ATM {

    private List<AtmCell> cells = new ArrayList<>();
    private AtmStrategy strategy;
    private final String name;
    private static int atmIndex = 0;

    public ATM(AtmStrategy strategy) {
        this.strategy = strategy;
        this.name = "ATM#" + atmIndex++;
    }

    public ATM(AtmStrategy strategy, String name) {
        this.strategy = strategy;
        this.name = name;
    }

    public void load(BanknotesSelection bs) {
        List<BunchOfBanknotes> banknotes = bs.getContent();
        for (BunchOfBanknotes bunch : banknotes) {
            AtmCell cell = getCell(bunch.getNominal());
            if (cell == null) {
                cell = new AtmCell(bunch.getNominal());
                cells.add(cell);
            }
            int loaded = cell.loadBanknotes(bunch.getNumOfBanknotes());
            bunch.getBanknotes(loaded);
        }
    }

    public BanknotesSelection empty() {
        if (isEmpty()) return null;
        BanknotesSelection bs = new BanknotesSelection();
        for (AtmCell cell : cells) {
            bs.putBanknotes(cell.getNominal(), cell.getNumOfBanknotes());
        }
        cells.clear();
        return bs;
    }

    public BanknotesSelection withdraw(int sumToWithdraw) throws AtmWithdrawException {
        if (cells.isEmpty() || strategy == null) throw new RuntimeException("System error");
        List<AtmCell> tmp = copyCells(cells);
        BanknotesSelection bs = strategy.withdraw(cells, sumToWithdraw);
        if (bs == null) {
            cells = tmp;
            throw new AtmWithdrawException();
        } else {
            return bs;
        }
    }

    public String getName() {
        return name;
    }

    public BanknotesSelection getState() {
        BanknotesSelection bs = new BanknotesSelection();
        for (AtmCell cell : cells) {
            bs.putBanknotes(cell.getNominal(), cell.getNumOfBanknotes());
        }
        return bs;
    }

    public int getTotal() {
        int total = 0;
        for (AtmCell cell : cells) {
            total += cell.getSum();
        }
        return total;
    }


    public boolean isEmpty() {
        return cells.isEmpty() || getTotal() == 0;
    }

    public void putBanknotes(Nominal nominal, int numOfBanknotes) {
        for(AtmCell cell : cells) {
            if (cell.getNominal() == nominal) {
                cell.putBanknotes(numOfBanknotes);
                break;
            }
        }
    }

    public void putBanknote(Nominal nominal) throws RuntimeException {
        for(AtmCell cell : cells) {
            if (cell.getNominal() == nominal) {
                if (cell.getNumOfBanknotes() == cell.getCapacity()) {
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
            cellsCopy.add(cell.clone());
        }
        return cellsCopy;
    }

    private AtmCell getCell(Nominal nominal) {
        for (AtmCell cell : cells) {
            if (cell.getNominal() == nominal) {
                return cell;
            }
        }
        return null;
    }
}
