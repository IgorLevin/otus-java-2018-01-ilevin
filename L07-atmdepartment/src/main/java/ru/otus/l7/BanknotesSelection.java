package ru.otus.l7;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class BanknotesSelection {

    private List<BunchOfBanknotes> content = new ArrayList<>();

    public BanknotesSelection() {}

    public void putBanknotes(int nominal, int numOfBanknotes) {
        if (hasNominal(nominal)) {
            getNominal(nominal).putBanknotes(numOfBanknotes);
        } else {
            content.add(new BunchOfBanknotes(nominal, numOfBanknotes));
            content.sort((o1, o2) -> o2.getNominal() - o1.getNominal());
        }
    }

    public void putBanknote(int nominal) {
        putBanknotes(nominal, 1);
    }

    public boolean getBanknote(int nominal) {
        if (isEmpty() || hasNominal(nominal)) {
            return false;
        }
        return getNominal(nominal).getBanknote();
    }

    public int getBanknotes(int nominal, int numOfBanknotes) {
        if (isEmpty() || !hasNominal(nominal)) {
            return 0;
        }
        BunchOfBanknotes bb = getNominal(nominal);
        int result = bb.getBanknotes(numOfBanknotes);
        if (bb.isEmpty()) {
            content.remove(bb);
        }
        return result;
    }

    public int getBanknotes(int nominal) {
        if (!isEmpty() && hasNominal(nominal)) {
            return 0;
        }
        return getBanknotes(nominal, getNominal(nominal).getNumOfBanknotes());
    }

    public int getNumberOfBanknotes(int nominal) {
        BunchOfBanknotes bb = getNominal(nominal);
        if (bb != null) {
            return bb.getNumOfBanknotes();
        } else {
            return 0;
        }
    }

    public boolean hasNominal(int nominal) {
        for (BunchOfBanknotes bb : content) {
            if (bb.getNominal() == nominal) {
                return true;
            }
        }
        return false;
    }

    private BunchOfBanknotes getNominal(int nominal) {
        for (BunchOfBanknotes bb : content) {
            if (bb.getNominal() == nominal) {
                return bb;
            }
        }
        return null;
    }

    public List<BunchOfBanknotes> getContent() {
        return content;
    }

    public boolean isEmpty() {
        return content.isEmpty() || getSum() == 0;
    }

    public int getSum() {
        int total = 0;
        for (BunchOfBanknotes bb : content) {
            total += bb.getSum();
        }
        return total;
    }

    @Override
    public String toString() {
        return content.stream()
                .map(BunchOfBanknotes::toString)
                .collect(Collectors.joining(" "));
    }

    @Override
    protected BanknotesSelection clone() {
        BanknotesSelection bs = new BanknotesSelection();
        bs.content = copyContent();
        return bs;
    }

    private List<BunchOfBanknotes> copyContent() {
        List<BunchOfBanknotes> copy = new ArrayList<>();
        for (BunchOfBanknotes bb : content) {
            copy.add(bb.clone());
        }
        return copy;
    }
}
