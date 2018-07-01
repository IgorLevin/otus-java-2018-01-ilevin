package ru.otus.l7.banknotes;

public class BunchOfBanknotes {

    private Nominal nominal;
    private int numOfBanknotes;

    public BunchOfBanknotes(Nominal nominal, int numOfBanknotes) {
        this.nominal = nominal;
        this.numOfBanknotes = numOfBanknotes;
    }

    public Nominal getNominal() {
        return nominal;
    }

    public void putBanknote() {
        putBanknotes(1);
    }

    public void putBanknotes(int numOfBanknotes) {
        this.numOfBanknotes += numOfBanknotes;
    }

    public boolean getBanknote() {
        if (isEmpty()) return false;
        numOfBanknotes -= 1;
        return true;
    }

    public int getBanknotes() {
        if (isEmpty()) return 0;
        int result = numOfBanknotes;
        numOfBanknotes = 0;
        return result;
    }

    public int getBanknotes(int num) {
        if (isEmpty()) return 0;

        if (numOfBanknotes <= num) {
            int result = numOfBanknotes;
            numOfBanknotes = 0;
            return result;
        } else {
            numOfBanknotes -= num;
            return num;
        }

    }

    public int getNumOfBanknotes() {
        return numOfBanknotes;
    }

    public boolean isEmpty() {
        return numOfBanknotes == 0;
    }

    public int getSum() {
        return numOfBanknotes * nominal.asNnt();
    }

    @Override
    public String toString() {
        return nominal + "[" + numOfBanknotes + "]";
    }

    @Override
    protected BunchOfBanknotes clone() {
        return new BunchOfBanknotes(nominal, numOfBanknotes);
    }
}
