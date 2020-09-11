package com.example.demo.client;

import com.example.demo.server.BankInterface;
import com.example.demo.server.Customer;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.util.List;

public class RMIClientDB {
    public static void main(String args[])throws Exception {

        String remoteEngine = "rmi://localhost/BankServices";

        // Create local stub, lookup in the registry searching for the remote engine - the interface with the methods we want to use remotely
        BankInterface bankInterface = (BankInterface) Naming.lookup(remoteEngine);

        List<Customer> list=bankInterface.getMillionaires();
        for(Customer c:list)
        {
            System.out.println(c.getId()+ " " + c.getName() + " " + c.getAmount());
        }

    }



}
