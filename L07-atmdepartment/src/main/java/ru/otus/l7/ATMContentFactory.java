package ru.otus.l7;

/**
 * i.e. Bank
 */
public class ATMContentFactory {

    private static final int[] DEFAULT_NOMINALS = new int[] {1, 2, 5, 10, 25, 50, 100};

    private ATMContentFactory() {}

    public static BanknotesSelection createAtmContent(int[] nominals, int numberOfBanknotes) {
        BanknotesSelection bs = new BanknotesSelection();
        for(int i = 0; i < nominals.length; i++) {
            bs.putBanknotes(nominals[i], numberOfBanknotes);
        }
        return bs;
    }

    public static BanknotesSelection createAtmContent(int numberOfBanknotes) {
        return createAtmContent(DEFAULT_NOMINALS, numberOfBanknotes);
    }
}
