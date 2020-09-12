package com.example.demo.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BankInterface extends Remote {

    List<Customer> getMillionaires() throws RemoteException;

    Customer createCustomer(String name, Double amount) throws RemoteException;

    Customer getCustomer(long id) throws Exception;

    List<Customer> getAllCustomers() throws RemoteException;

    Customer editCustomer(long id, String name) throws Exception;

    List<Customer> transferMoney(long idSender, long idReceiver, double amount) throws Exception;

}
