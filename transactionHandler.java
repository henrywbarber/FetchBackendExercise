// Fetch Backend Take Home Test
// Filename: transactionHandler.java
// Name: Henry Barber
// Email: henrywbarber@gmail.com
// Date: 02/22/2023

import java.io.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * This class takes user input, in cordination with a csv file and transactions.java file to
 * track and carryout a user inputted spenditure on a log of transactions. The user must run
 * this file with an input for spendature amount and name of CSV transactions file.
 */
public class transactionHandler {

    /**
     * Creates transaction elements and carrys out spendatures on the transaction elemnts.
     * Transactions are chronologically taken into account to allocate spent points. Finally
     * the totals remaining in the transaction elements are compiled and printed.
     * 
     * @param toSpend Used inputted amount to subtract from payers
     * @param parsedCSV ArrayList containing parsed lines found in CSV file
     */
    private static void transcationComputer(int toSpend, ArrayList<String> parsedCSV) {
        // ArrayList to hold transaction objects
        ArrayList<transaction> transactions = new ArrayList<transaction>();
        // Removes CSV header values
        parsedCSV.remove(0);
        // Iterates through lines taken from CSV file, uses data to create transaction objects
        // and adds objects to transactions ArrayList
        for (String transaction : parsedCSV) {
            String[] values = transaction.split(",");
            transactions.add(new transaction(values[0].replaceAll("\"", ""), values[1], 
            values[2].replaceAll("\"", "")));
        }
        // Utilized comparator import to reverse sort transactions chronologically based on
        // timestamp, oldest to youngest
        transactions.sort(Comparator.comparing(transaction::getTimestamp));
        // Points to spend
        int spending = toSpend;
        // Iterates through transaction record, taking points from payers based on oldest timestamp,
        // while preventing payers from having negative points totals
        for (transaction transaction : transactions) {
            // Spending is complete
            if (spending <= 0) {
                break;
            }
            // Payer has points that can be spent
            if (transaction.points > 0) {
                // Payer does not have enough points to cover full spendature
                if (transaction.points <= spending) {
                    spending -= transaction.points;
                    transaction.points = 0;
                } 
                // Payer has enough points to cover remaining spendings
                else {
                    transaction.points -= spending;
                    spending = 0;
                    break;
                }
            } 
            // Payer has negative points transaction, points added to spendature to be taken from another payer
            else {
                spending -= transaction.points;
                transaction.points = 0;
            }
        }
        // Catches outstanding spendature remaining, otherwise continues to compile transaction results
        if (spending > 0) {
            System.out.println("Error: Spent " + spending + " more points than total payers have");
        } else {
            compileResults(transactions);
        }
    }

    /**
     * Creates profiles for different payers from transactions list and compiles their total points.
     * Then these result are formatted and printed.
     * 
     * @param transactions List of transaction objects containing results after spending
     */
    private static void compileResults(ArrayList<transaction> transactions) {
        // Creates list of profiles to compile different payers from transcations list
        ArrayList<transaction> profiles = new ArrayList<transaction>();
        // Iterates through transactions list to identify repeat payers and adds their transaction to
        // the profile list while updating point totals
        for (transaction transaction : transactions) {
            // Adds first transaction payer to profiles list
            if (profiles.isEmpty()) {
                profiles.add(transaction);
            } else {
                // Iterates through profiles to check if payer already has a profile
                boolean containsProfile = false;
                for (transaction profile : profiles) {
                    // Payer already has profile, points are compiled
                    if (profile.getPayer().equals(transaction.payer)) {
                        profile.addPoints(transaction.points);
                        containsProfile = true;
                        break;
                    }
                }
                // Payer not in profiles, transaction added
                if (containsProfile == false) {
                    profiles.add(transaction);
                }
            }
        }
        // Iterates through profiles to print points totals while correctly formatting data
        System.out.println("{");
        for (int i = 0; i < profiles.size() - 1; i++) {
            System.out.println("\t\"" + profiles.get(i).payer + "\": " + profiles.get(i).points + ",");
        }
        System.out.println("\t\"" + profiles.get(profiles.size() - 1).payer + "\": " + profiles.get(
                profiles.size() - 1).points);
        System.out.println("}");
    }

    /**
     * Parses CSV file for transaction data
     * 
     * @param scan Scanner object that has been passed correct file location
     * @return ArrayList<String> of lines found in CSV file
     */
    private static ArrayList<String> parseCSV(Scanner scan) {
        // ArrayList to store seperate lines from CSV file
        ArrayList<String> parsed = new ArrayList<String>();
        // Iterates through lines of CSV file before terminating at end of file
        while (scan.hasNextLine()) {
            parsed.add(scan.nextLine());
        }
        scan.close();
        return parsed;
    }
    
    /**
     * Aquires necessary information from user, including spendature amount and CSV file location.
     * Calls transaction computing and related methods.
     * @param args User inputed arguments for points spendature followed by CSV file location
     * @throws Exception Upon incorrect user input of points to spend or CSV file location
     */
    public static void main(String[] args) throws Exception {
        // Verifies correct amount of user arguments
        if (args.length != 2) {
            System.out.println("Error: Please specify points to spend and csv filename (ex. 5000 transactions.csv)");
        } else {
            // Interperate user input and scan CSV file
            try {
                // Scanner object retrives transaction CSV file and user argued points amount converted into integer
                Scanner scan = new Scanner(new File(args[1]));
                int toSpend = Integer.parseInt(args[0]);
                // Calls computational method to complete operations
                transcationComputer(toSpend, parseCSV(scan));
            } catch (FileNotFoundException e1) {
                System.out.println("Error: Could not find CSV file");
            } catch (NumberFormatException e2) {
                System.out.println("Error: Could not identify points to spend");
            }
        }
    }
}