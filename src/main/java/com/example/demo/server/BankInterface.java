package com.example.demo.server;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface BankInterface extends Remote {

    List<Customer> getMillionaires() throws RemoteException;

    Customer createCustomer(long id, String name, Double amount) throws RemoteException;

    Customer getCustomer(long id) throws RemoteException;

    List<Customer> getAllCustomers() throws RemoteException;

    Customer editCustomer() throws RemoteException;

    Customer transferMoney() throws RemoteException;

}
