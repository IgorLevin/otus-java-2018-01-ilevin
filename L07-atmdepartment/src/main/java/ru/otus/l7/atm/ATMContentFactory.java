package ru.otus.l7.atm;

import ru.otus.l7.banknotes.BanknotesSelection;
import ru.otus.l7.banknotes.Nominal;

/**
 * i.e. Bank
 */
public class ATMContentFactory {

    private static final Nominal[] DEFAULT_NOMINALS = new Nominal[] {
            Nominal.RUB_1,
            Nominal.RUB_2,
            Nominal.RUB_5,
            Nominal.RUB_10,
            Nominal.RUB_25,
            Nominal.RUB_50,
            Nominal.RUB_100
    };
    private static final int DEFAULT_NUMBER_OF_BANKNOTES = 100;

    private ATMContentFactory() {}

    public static BanknotesSelection createAtmContent(Nominal[] nominals, int numberOfBanknotes) {
        BanknotesSelection bs = new BanknotesSelection();
        for(int i = 0; i < nominals.length; i++) {
            bs.putBanknotes(nominals[i], numberOfBanknotes);
        }
        return bs;
    }

    public static BanknotesSelection createAtmContent(int numberOfBanknotes) {
        return createAtmContent(DEFAULT_NOMINALS, numberOfBanknotes);
    }

    public static BanknotesSelection createAtmContent() {
        return createAtmContent(DEFAULT_NOMINALS, DEFAULT_NUMBER_OF_BANKNOTES);
    }
}
