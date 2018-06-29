package ru.otus.l7;

import ru.otus.l7.atm.ATM;
import ru.otus.l7.atm.AtmStrategy;
import ru.otus.l7.atm.MinNumberOfBanknotesStrategy;

public class ATMFactory {

    private static final int[] DEFAULT_NOMINALS = new int[] {1, 2, 5, 10, 25, 50, 100};
    private static int ATM_INDEX = 0;

    private ATMFactory() {}

    public static ATM createATM() {
        return new ATM(DEFAULT_NOMINALS, new MinNumberOfBanknotesStrategy(), "ATM#" + ATM_INDEX++);
    }

    public static ATM createATM(int[] nominals) {
        return new ATM(nominals, new MinNumberOfBanknotesStrategy(), "ATM#" + ATM_INDEX++);
    }

    public static ATM createATM(int[] nominals, AtmStrategy strategy) {
        return new ATM(nominals, strategy, "ATM#" + ATM_INDEX++);
    }

    public static ATM createATM(int[] nominals, AtmStrategy strategy, String name) {
        return new ATM(nominals, strategy, name);
    }
}
