package ru.otus.l6;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l6.atm.ATM;
import ru.otus.l6.atm.AtmCell;
import ru.otus.l6.atm.MinNumberOfBanknotesStrategy;

import java.util.*;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("EmptyCatchBlock")
    public static void main(String... args) throws Exception {

        ATM atm = new ATM(createAtmContent(), new MinNumberOfBanknotesStrategy());

        log.info("ATM util");
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        boolean terminated = false;
        while(!terminated) {
            log.info("Enter sum to withdraw:");
            String input = scanner.next();
            try {
                int sum = Integer.valueOf(input);
                log.trace("Sum: {}", sum);
                withdraw(atm, sum);
                log.info("");
            } catch (NumberFormatException e) {
                String[] atmArgs = input.split(" ");
                if (atmArgs.length > 3 || atmArgs.length == 0) {
                    log.info("Wrong input. Enter -h to get help");
                }
                switch (atmArgs[0]) {
                    case "q":
                    case "Q":
                    case "quit":
                    case "exit":
                        terminated = true;
                        break;
                    case "h":
                    case "-h":
                    case "help":
                    case "--help":
                        log.info("ATM util");
                        log.info("Options: NUMBER|p NUMBER|s|q|h");
                        log.info("  NUMBER    - sum to withdraw - 1; 200 ...");
                        log.info("  p NOMINAL - put banknote into ATM");
                        log.info("  s         - ATM state");
                        log.info("  q         - exit");
                        log.info("");
                        break;
                    case "s":
                    case "-s":
                    case "--state":
                        printAtmState(atm);
                        break;
                    case "p":
                    case "-p":
                        putBanknoteIntoAtm(atm, atmArgs[1]);
                        break;
                    default:
                        log.info("Wrong input. Enter -h to get help");
                        break;

                }
            }

        }
        log.info("Bye");
    }

    private static void withdraw(ATM atm, int sum) {
        log.trace("Try to withdraw {} ...", sum);
        try {
            Map<Integer, Integer> bunchOfBanknotes = atm.withdraw(sum);
            if (bunchOfBanknotes == null) {
                log.info("Requested sum can't be withdrawn");
            } else {
                printBunchOfBanknotes(bunchOfBanknotes);
            }
        } catch (Exception e) {
            log.error("ATM error. " + e.getMessage());
        }
    }

    private static void printAtmState(ATM atm) {
        log.trace("ATM State:");
        Map<Integer, Integer> bunchOfBanknotes = atm.getState();
        if (bunchOfBanknotes.isEmpty()) {
            log.info("ATM is empty");
        } else {
            printBunchOfBanknotes(bunchOfBanknotes);
        }
    }

    private static void putBanknoteIntoAtm(ATM atm, String nominal) {
        try {
            int nom = Integer.valueOf(nominal);
            atm.putBanknote(nom);
            log.info("Success");
        } catch (NumberFormatException e) {
            log.error("Wrong nominal");
        } catch (RuntimeException e) {
            log.error("Banknote input error. " + e.getMessage());
        }
    }

    private static void printBunchOfBanknotes(Map<Integer, Integer> bunchOfBanknotes) {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<Integer, Integer> entry : bunchOfBanknotes.entrySet()) {
            int nominal = entry.getKey();
            int numOfBn = entry.getValue();
            sb.append(nominal).append("[").append(numOfBn).append("] ");
        }
        log.info(sb.toString());
    }

    private static List<AtmCell> createAtmContent() {
        List<AtmCell> cells = new ArrayList<>();
        cells.add(new AtmCell(100, 100));
        cells.add(new AtmCell(50, 100));
        cells.add(new AtmCell(25, 100));
        cells.add(new AtmCell(10, 100));
        cells.add(new AtmCell(5, 100));
        cells.add(new AtmCell(2, 100));
        cells.add(new AtmCell(1, 100));
        return cells;
    }
}
