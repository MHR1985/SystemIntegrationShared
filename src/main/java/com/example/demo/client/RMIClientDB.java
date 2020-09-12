package com.example.demo.client;

import com.example.demo.server.BankInterface;
import com.example.demo.server.Customer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Scanner;

public class RMIClientDB {
    private static void printOptions() {
        System.out.println("");
        System.out.println("You have the following options:");
        System.out.println("Press 1 to get all millionares");
        System.out.println("Press 2 to create Customers");
        System.out.println("Press 3 to get all Customers");
        System.out.println("Press 4 to transfer money between customers");
        System.out.println("Press 5 to get a customer");
        System.out.println("Press 6 to edit a customer");
        System.out.println("Press 7 to quit");
    }

    public static void main(String args[]) throws Exception {

        String remoteEngine = "rmi://localhost/BankServices";

        // Create local stub, lookup in the registry searching for the remote engine - the interface with the methods we want to use remotely
        BankInterface bankInterface = (BankInterface) Naming.lookup(remoteEngine);
        boolean run = true;


        Scanner scan = new Scanner(System.in);
        while (run) {
            printOptions();
            switch (scan.nextInt()) {
                case (1): {
                    List<Customer> list = bankInterface.getMillionaires();
                    for (Customer c : list) {
                        System.out.println(c.getId() + " " + c.getName() + " " + c.getAmount());
                    }
                    break;
                }
                case (2): {
                    System.out.println("Enter customers first name:");
                    String firstName = scan.next();
                    System.out.println("Enter customers last name:");
                    String lastName = scan.next();
                    String fullName = firstName + " " + lastName;
                    Customer create = bankInterface.createCustomer(fullName, 50000000.0);
                    System.out.println("New customer was created under the name: " + create.getName());
                    System.out.println("Account contains " + create.getAmount());
                    break;
                }
                case (3): {
                    List<Customer> allCustomers = bankInterface.getAllCustomers();
                    for (Customer c : allCustomers) {
                        System.out.println("Id: " + c.getId() + " Name: " + c.getName() + " Balance: " + c.getAmount());
                    }
                    break;
                }
                case (4): {
                    System.out.println("Enter id of sender:");
                    long idSender = scan.nextLong();
                    System.out.println("Enter id of receiver");
                    long idReceiver = scan.nextLong();
                    System.out.println("Enter amount of money to transfer:");
                    double amount = scan.nextDouble();
                    try {
                        List<Customer> customers = bankInterface.transferMoney(idSender, idReceiver, amount);
                        for (Customer c : customers) {
                            System.out.println("Id: " + c.getId() + " Name: " + c.getName() + " Balance: " + c.getAmount());
                        }
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                case (5): {
                    System.out.println("please type the id of the customer");
                    long id = scan.nextLong();
                    try {
                        Customer c = bankInterface.getCustomer(id);
                        System.out.println("Id: " + c.getId() + " Name: " + c.getName() + " Balance: " + c.getAmount());
                    } catch (Exception ex){
                        System.out.println(ex.getMessage());
                    }
                    break;
                }
                case (6): {
                    System.out.println("Enter id of customer you want to edit:");
                    long id = scan.nextLong();
                    System.out.println("Enter customers first name:");
                    String firstName = scan.next();
                    System.out.println("Enter customers last name:");
                    String lastName = scan.next();
                    String fullName = firstName + " " + lastName;
                    Customer edit = bankInterface.editCustomer(id, fullName);
                    System.out.println("Customer name was changed to: " + edit.getName());
                    break;
                }
                case (7): {
                    run = false;
                    break;
                }
            }
        }

    }

}
