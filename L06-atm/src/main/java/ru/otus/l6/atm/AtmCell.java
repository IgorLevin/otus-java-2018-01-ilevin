package ru.otus.l6.atm;

public class AtmCell {

    public static final int DEFAULT_CAPACITY = 100;
    private final int capacity;
    private int nominal;
    private int numberOfBanknotes;

    public AtmCell(int capacity, int numOfNotes, int nominal) {
        this.capacity = capacity;
        this.nominal = nominal;
        this.numberOfBanknotes = numOfNotes;
    }

    public AtmCell(int nominal, int numOfNotes) {
        this.capacity = DEFAULT_CAPACITY;
        this.nominal = nominal;
        this.numberOfBanknotes = numOfNotes;
    }

    public AtmCell(AtmCell cell) {
        this.capacity = cell.getCapacity();
        this.nominal = cell.getNominal();
        this.numberOfBanknotes = cell.getNumberOfBanknotes();
    }

    public void load(int nominal, int numberOfBanknotes) {
        this.nominal = nominal;
        this.numberOfBanknotes = numberOfBanknotes;
    }

    public void putBanknotes(int numToPut) {
        this.numberOfBanknotes += numToPut;
        if (numberOfBanknotes > capacity) {
            numberOfBanknotes = capacity;
        }
    }

    public void setNominal(int nominal) {
        this.nominal = nominal;
    }

    public int getNominal() {
        return nominal;
    }

    public int getNumberOfBanknotes() {
        return numberOfBanknotes;
    }

    public void setNumberOfBanknotes(int numberOfBanknotes) {
        this.numberOfBanknotes = numberOfBanknotes;
    }

    public boolean hasBanknotes() {
        return numberOfBanknotes > 0;
    }

    public void pullBanknote() throws RuntimeException {
        if (numberOfBanknotes > 0) {
            numberOfBanknotes = numberOfBanknotes - 1;
        } else {
            throw new RuntimeException("Out of bank notes");
        }
    }

    public int getCapacity() {
        return capacity;
    }
}
