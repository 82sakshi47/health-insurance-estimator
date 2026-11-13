package com.insurance.app;

import com.insurance.exception.InvalidPolicyDataException;
import com.insurance.exception.RecordNotFoundException;
import com.insurance.model.*;
import com.insurance.service.PolicyManager;

import java.util.*;

public class Main {
    private static final Scanner in = new Scanner(System.in);
    private static final PolicyManager manager = new PolicyManager();

    public static void main(String[] args) {
        System.out.println("=== Health Insurance Premium Estimator ===");
        boolean running = true;
        while (running) {
            printMenu();
            try {
                String choice = in.nextLine().trim();
                switch (choice) {
                    case "1" -> addCustomer();
                    case "2" -> createIndividual();
                    case "3" -> createFloater();
                    case "4" -> listCustomers();
                    case "5" -> listPolicies();
                    case "6" -> findPolicy();
                    case "0" -> running = false;
                    default -> System.out.println("Choose a number from the menu.");
                }
            } catch (InvalidPolicyDataException | RecordNotFoundException e) {
                System.out.println("Error: " + e.getMessage());
            } catch (NumberFormatException e) {
                System.out.println("Error: please enter a valid number.");
            } catch (NoSuchElementException e) {
                running = false;
            }
        }
        System.out.println("Goodbye.");
    }

    private static void printMenu() {
        System.out.println("\n1. Add customer");
        System.out.println("2. Create individual policy");
        System.out.println("3. Create family floater policy");
        System.out.println("4. List customers");
        System.out.println("5. List policies");
        System.out.println("6. Find policy by ID");
        System.out.println("0. Exit");
        System.out.print("Choice: ");
    }

    private static void addCustomer() throws InvalidPolicyDataException {
        System.out.print("Name: ");
        String name = in.nextLine();
        System.out.print("Age: ");
        int age = Integer.parseInt(in.nextLine().trim());
        System.out.println("Risk factors " + Arrays.toString(RiskFactor.values()));
        System.out.print("Enter comma-separated, or press Enter for none: ");
        Set<RiskFactor> risks = parseRisks(in.nextLine());
        Customer c = manager.addCustomer(name, age, risks);
        System.out.println("Added: " + c);
    }

    private static void createIndividual()
            throws RecordNotFoundException, InvalidPolicyDataException {
        System.out.print("Customer ID: ");
        String id = in.nextLine().trim();
        CoverageTier tier = askTier();
        System.out.println("Created: " + manager.createIndividualPolicy(id, tier));
    }

    private static void createFloater()
            throws RecordNotFoundException, InvalidPolicyDataException {
        System.out.print("Member customer IDs (comma-separated): ");
        List<String> ids = new ArrayList<>();
        for (String part : in.nextLine().split(",")) {
            if (!part.isBlank()) ids.add(part.trim());
        }
        CoverageTier tier = askTier();
        System.out.println("Created: " + manager.createFamilyFloater(ids, tier));
    }

    private static void listCustomers() {
        List<Customer> all = manager.getAllCustomers();
        all.sort(Comparator.comparing(Customer::getId));
        if (all.isEmpty()) System.out.println("No customers yet.");
        all.forEach(System.out::println);
    }

    private static void listPolicies() {
        List<Policy> all = manager.getAllPolicies();
        if (all.isEmpty()) System.out.println("No policies yet.");
        all.forEach(System.out::println);
    }

    private static void findPolicy() throws RecordNotFoundException {
        System.out.print("Policy ID: ");
        System.out.println(manager.getPolicy(in.nextLine().trim()));
    }

    private static CoverageTier askTier() throws InvalidPolicyDataException {
        System.out.print("Coverage tier " + Arrays.toString(CoverageTier.values()) + ": ");
        try {
            return CoverageTier.valueOf(in.nextLine().trim().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new InvalidPolicyDataException("Unknown coverage tier.");
        }
    }

    private static Set<RiskFactor> parseRisks(String line) throws InvalidPolicyDataException {
        Set<RiskFactor> risks = EnumSet.noneOf(RiskFactor.class);
        if (line.isBlank()) return risks;
        for (String part : line.split(",")) {
            try {
                risks.add(RiskFactor.valueOf(part.trim().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new InvalidPolicyDataException("Unknown risk factor: " + part.trim());
            }
        }
        return risks;
    }
}
