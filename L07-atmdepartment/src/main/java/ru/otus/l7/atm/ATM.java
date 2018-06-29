package ru.otus.l7.atm;

import ru.otus.l7.BanknotesSelection;
import ru.otus.l7.BunchOfBanknotes;

import java.util.*;

public class ATM {

    private List<AtmCell> cells;
    private AtmStrategy strategy;
    private final String name;

    public ATM(int[] nominals, AtmStrategy strategy) {
        this.cells = createCells(nominals);
        this.strategy = strategy;
        this.name = "ATM";
    }

    public ATM(int[] nominals, AtmStrategy strategy, String name) {
        this.cells = createCells(nominals);
        this.strategy = strategy;
        this.name = name;
    }

    public void load(BanknotesSelection bs) {
        List<BunchOfBanknotes> banknotes = bs.getContent();
        for (BunchOfBanknotes bunch : banknotes) {
            AtmCell cell = getCell(bunch.getNominal());
            if (cell != null) {
                int loaded = cell.loadBanknotes(bunch.getNumOfBanknotes());
                bunch.getBanknotes(loaded);
            }
        }
    }

    public void setStrategy(AtmStrategy strategy) {
        this.strategy = strategy;
    }

    public BanknotesSelection withdraw(int sumToWithdraw) {
        if (cells.isEmpty() || strategy == null) throw new RuntimeException("System error");
        List<AtmCell> tmp = copyCells(cells);
        strategy.init(cells);
        BanknotesSelection bs = strategy.withdraw(sumToWithdraw);
        if (bs == null) {
            cells = tmp;
            return null;
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
            bs.getBanknotes(cell.getNominal(), cell.getNumOfBanknotes());
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

    // TODO remove
    public void fill(Map<Integer, Integer> bunchOfBanknotes) {
        for (AtmCell cell : cells) {
            if (bunchOfBanknotes.containsKey(cell.getNominal())) {
                cell.putBanknotes(bunchOfBanknotes.get(cell.getNominal()));
            }
        }
    }

    public boolean isEmpty() {
        return cells.isEmpty() || getTotal() == 0;
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

    private AtmCell getCell(int nominal) {
        for (AtmCell cell : cells) {
            if (cell.getNominal() == nominal) {
                return cell;
            }
        }
        return null;
    }

    private static List<AtmCell> createCells(int[] nominals) {
        List<AtmCell> cells = new ArrayList<>();
        for (int i = 0; i < nominals.length; i++) {
            cells.add(new AtmCell(nominals[i], AtmCell.DEFAULT_CAPACITY));
        }
        return cells;
    }
}
