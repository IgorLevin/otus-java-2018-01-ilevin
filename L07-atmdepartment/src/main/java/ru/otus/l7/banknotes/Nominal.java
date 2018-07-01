package ru.otus.l7.banknotes;

public enum Nominal {

    RUB_100(100),
    RUB_50(50),
    RUB_25(25),
    RUB_10(10),
    RUB_5(5),
    RUB_2(2),
    RUB_1(1);

    private int nominal;

    Nominal(int nom) {
        nominal = nom;
    }

    public int asNnt() {
        return nominal;
    }

    public static Nominal fromString(String strNom) throws WrongNominalException {
        switch (strNom) {
            case "100":
                return RUB_100;
            case "50":
                return RUB_50;
            case "25":
                return RUB_25;
            case "10":
                return RUB_10;
            case "5":
                return RUB_5;
            case "2":
                return RUB_2;
            case "1":
                return RUB_1;
            default:
                throw new WrongNominalException();
        }
    }
}
