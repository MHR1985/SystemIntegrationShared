package com.example.demo.client;

import com.example.demo.server.BankInterface;
import com.example.demo.server.Customer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.util.List;

public class RMIClientDB {
    public static void main(String args[]) throws RemoteException, MalformedURLException, NotBoundException {

        String remoteEngine = "rmi://localhost/BankServices";

        // Create local stub, lookup in the registry searching for the remote engine - the interface with the methods we want to use remotely
        BankInterface bankInterface = (BankInterface) Naming.lookup(remoteEngine);

        List<Customer> list=bankInterface.getMillionaires();
        for(Customer c:list)
        {
            System.out.println(c.getId()+ " " + c.getName() + " " + c.getAmount());
        }

        Customer create = bankInterface.createCustomer(3000, "kaj er en haj", 50000000.0);
        System.out.println("New customer was created under the name: " + create.getName());
        System.out.println("Account contains " + create.getAmount());
        //Customer customer = bankInterface.getCustomer();

    }



}
