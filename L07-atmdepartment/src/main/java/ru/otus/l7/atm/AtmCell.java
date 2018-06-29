package ru.otus.l7.atm;

import ru.otus.l7.BunchOfBanknotes;

public class AtmCell extends BunchOfBanknotes {

    public static final int DEFAULT_CAPACITY = 100;
    private final int capacity;

    public AtmCell(int nominal) {
        super(nominal, 0);
        this.capacity = DEFAULT_CAPACITY;
    }

    public AtmCell(int nominal, int capacity) {
        super(nominal, 0);
        this.capacity = capacity;
    }

    private AtmCell(int nominal, int capacity, int numberOfBanknotes) {
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
    protected AtmCell clone() {
        return new AtmCell(getNominal(), capacity, getNumOfBanknotes());
    }
}
