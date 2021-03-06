package ru.otus.l7.atm;

import ru.otus.l7.banknotes.BunchOfBanknotes;
import ru.otus.l7.banknotes.Nominal;

public class ATMCell extends BunchOfBanknotes {

    public static final int DEFAULT_CAPACITY = 100;
    private final int capacity;

    public ATMCell(Nominal nominal) {
        super(nominal, 0);
        this.capacity = DEFAULT_CAPACITY;
    }

    public ATMCell(Nominal nominal, int capacity) {
        super(nominal, 0);
        this.capacity = capacity;
    }

    private ATMCell(Nominal nominal, int capacity, int numberOfBanknotes) {
        super(nominal, numberOfBanknotes);
        this.capacity = capacity;
    }

    public int getCapacity() {
        return capacity;
    }

    public int loadBanknotes(int numToLoad) {
        int numInCell = getNumOfBanknotes();
        if (numInCell + numToLoad > capacity) {
            int loaded = capacity - (numInCell + numToLoad);
            putBanknotes(loaded);
            return loaded;
        } else {
            putBanknotes(numToLoad);
            return numToLoad;
        }
    }

    public void pullBanknote() throws RuntimeException {
        if (!getBanknote()) {
            throw new RuntimeException("Out of bank notes");
        }
    }

    @Override
    protected ATMCell clone() {
        return new ATMCell(getNominal(), capacity, getNumOfBanknotes());
    }
}
