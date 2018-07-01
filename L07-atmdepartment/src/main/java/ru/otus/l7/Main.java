package ru.otus.l7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.l7.atm.ATM;
import ru.otus.l7.atm.ATMDepartment;
import ru.otus.l7.atm.AtmWithdrawException;
import ru.otus.l7.banknotes.BanknotesSelection;
import ru.otus.l7.banknotes.Nominal;

import java.util.List;
import java.util.Scanner;

public class Main {

    private static Logger log = LoggerFactory.getLogger(Main.class);

    @SuppressWarnings("EmptyCatchBlock")
    public static void main(String... args){

        ATMDepartment department = new ATMDepartment();

        System.out.println("ATM Department util");
        Scanner scanner = new Scanner(System.in).useDelimiter("\n");
        boolean terminated = false;
        while(!terminated) {
            System.out.println("Enter ATM name and sum to withdraw:");
            String input = scanner.next();
            String[] atmArgs = input.split(" ");
            if (atmArgs.length == 1) {
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
                        System.out.println("ATM Department Util");
                        System.out.println("Options: NAME NUMBER|NAME p NUMBER|s|q|h");
                        System.out.println("  NAME          - ATM name");
                        System.out.println("      NUMBER    - sum to withdraw e.g. 1, 200 ...");
                        System.out.println("      p NOMINAL - put banknote into ATM");
                        System.out.println("  s             - ATMs state");
                        System.out.println("  r             - restore ATMs");
                        System.out.println("  l             - list ATMs");
                        System.out.println("  q             - exit");
                        System.out.println("  h             - this help");
                        System.out.println();
                        break;
                    case "s":
                    case "-s":
                    case "--state":
                        printAtmsState(department.getATMs());
                        break;
                    case "l":
                    case "-l":
                        listATMs(department.getATMs());
                        break;
                    case "r":
                    case "-r":
                        restoreAllATMs(department);
                        break;
                    default:
                        System.out.println("Wrong input. Enter -h to get help");
                        break;
                }
            } else if (atmArgs.length == 2) { // withdraw
                try {
                    int sum = Integer.valueOf(atmArgs[1]);
                    String name = atmArgs[0];
                    ATM atm = department.getATM(name);
                    if (atm == null) {
                        System.out.println("Incorrect ATM name. Use 'l' to list AMT's");
                        log.info("Incorrect ATM name: {}", name);
                    } else {
                        withdraw(atm, sum);
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Incorrect sum");
                    log.warn("Incorrect sum");
                }
            } else if (atmArgs.length == 3) { // put money
                try {
                    String flag = atmArgs[1];
                    if (!flag.equalsIgnoreCase("p") &&
                        !flag.equalsIgnoreCase("-p")) {
                        System.out.println("Incorrect flag. Enter -h to get help");
                        log.info("Incorrect flag");
                    }
                    String name = atmArgs[0];
                    ATM atm = department.getATM(name);
                    if (atm == null) {
                        System.out.println("Incorrect ATM name. Use 'l' to list AMT's");
                        log.info("Incorrect ATM name: {}", name);
                    } else {
                        putBanknoteIntoAtm(atm, Nominal.fromString(atmArgs[2]));
                    }
                } catch (IllegalArgumentException e) {
                    System.out.println("Incorrect sum");
                    log.warn("Incorrect sum");
                }
            } else {
                System.out.println("Wrong input. Enter -h to get help");
                log.warn("Wrong input");
            }
        }
        System.out.println("Bye");
    }

    private static void withdraw(ATM atm, int sum) {
        log.trace("withdraw() {} {}", atm, sum);
        System.out.println("Try to withdraw " + sum + " from " + atm.getName() + " ...");
        try {
            BanknotesSelection bs = atm.withdraw(sum);
            System.out.println("  Success.");
            System.out.println("  Take: " + bs.toString() + "\n");
            log.info("Withdrawn: {}", bs.toString());
        } catch (AtmWithdrawException e) {
            System.out.println("  Requested sum can't be withdrawn");
            log.error("Requested sum can't be withdrawn. ", e);
        } catch (Exception e) {
            System.out.println("ATM error. " + e.getMessage());
            log.error("Error: ", e);
        }
    }

    private static void listATMs(List<ATM> atmList) {
        log.trace("list ATMs()");
        if (atmList.isEmpty()) {
            System.out.println("No ATMs");
        }
        System.out.println("ATMs:");
        for (ATM atm : atmList) {
            log.info("ATM: {}", atm.getName());
            System.out.println("   " + atm.getName());
        }
    }

    private static void printAtmsState(List<ATM> atmList) {
        log.trace("printAtmsState()");
        System.out.println("ATMs state:");
        for (ATM atm : atmList) {
            BanknotesSelection bs = atm.getState();
            log.info("ATM: {}", atm.getName());
            if (bs.isEmpty()) {
                System.out.println("   " + atm.getName() + " - ATM is empty");
                log.info("Empty");
            } else {
                System.out.println("   " + atm.getName() + " - " + bs.toString());
                log.info(bs.toString());
            }
        }
    }

    private static void restoreAllATMs(ATMDepartment department) {
        System.out.println("Restoring ATMs ...");
        department.restoreAllATMs();
        System.out.println("Done.");
        printAtmsState(department.getATMs());
    }

    private static void putBanknoteIntoAtm(ATM atm, Nominal nominal) {
        log.trace("putBanknoteIntoAtm() {} {}", atm,  nominal);
        try {
            atm.putBanknote(nominal);
            System.out.println("Success");
            log.info("Banknote accepted");
        } catch (RuntimeException e) {
            System.out.println("Banknote input error. "  + e.getMessage());
            log.error("Banknote input error. ", e);
        }
    }
}
