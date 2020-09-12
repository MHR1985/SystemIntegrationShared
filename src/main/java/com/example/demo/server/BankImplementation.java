package com.example.demo.server;

import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class BankImplementation extends UnicastRemoteObject implements BankInterface {

    public static String url = "jdbc:h2:file:C:/SystemIntegration_RCI/src/main/resources/shadybank/bank;AUTO_SERVER=TRUE";
    public static String user = "sa";
    public static String password = "";
    public static String driver = "org.h2.Driver";

    BankImplementation() throws RemoteException {
    }

    private Connection con;

    public ResultSet getQuery(String query) throws Exception {
        Class.forName(driver);
        con = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = con.prepareStatement(query);
        return ps.executeQuery();
    }

    public int createQuery(String query) throws Exception {
        Class.forName(driver);
        con = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = con.prepareStatement(query);
        return ps.executeUpdate();
    }

    @ResponseBody
    @GetMapping("/millionaires")
    public List<Customer> getMillionaires() {
        List<Customer> list = new ArrayList<Customer>();
        try {
            ResultSet rs = getQuery("select * from Customer where amount >= 100000");
            getListFromResultSet(list, rs);
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    private void getListFromResultSet(List<Customer> list, ResultSet rs) throws SQLException {
        while (rs.next()) {
            Customer c = new Customer();
            c.setId(rs.getLong(1));
            c.setName(rs.getString(2));
            c.setAmount(rs.getDouble(3));
            System.out.println(c);
            list.add(c);
        }
        con.close();
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<Customer>();
        try {
            ResultSet rs = getQuery("select * from Customer");
            getListFromResultSet(list, rs);
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }


    @PostMapping("/create")
    public Customer createCustomer(String name, Double amount) {
        Customer c = new Customer();
        c.setName(name);
        c.setAmount(amount);
        try {
            int i = createQuery(
                    "insert into customer(name, amount) " +
                    "values ('" + name + "'," + amount + ");");
            if (con != null)
                con.close();
            if (i != 0){
                ResultSet rs = getQuery("SELECT * FROM Customer WHERE name = '"+name+"' AND amount = "+amount);
                return getCustomerFromResultSet(rs);
            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }


    @GetMapping("/customer")
    public Customer getCustomer(@RequestParam long id) throws Exception {
        ResultSet rs = getQuery("select * from Customer where id = " + id);
        return getCustomerFromResultSet(rs);
    }

    @PutMapping("/customer")
    public Customer editCustomer(@RequestParam long id, @RequestParam String name) throws Exception {
        int i = createQuery("UPDATE Customer SET name = '" + name + "' WHERE ID = " + id);
        if (i > 0) {
            var rs = getQuery(" SELECT * FROM Customer WHERE ID = " + id);
            return getCustomerFromResultSet(rs);
        }
        return null;
    }

    @PutMapping("/transfer")
    public List<Customer> transferMoney(@RequestParam long idSender,@RequestParam long idReceiver, @RequestParam double amount) throws Exception {
        Customer sender = null;
        Customer receiver = null;
        ResultSet rs = getQuery(" SELECT * FROM Customer WHERE ID = " + idSender);
        sender = getCustomerFromResultSet(rs);
        int i = createQuery("UPDATE Customer SET amount = " + (sender.getAmount()-amount) + " WHERE ID = " + idSender);
        if(i>0){
            rs = getQuery(" SELECT * FROM Customer WHERE ID = " + idSender);
            sender = getCustomerFromResultSet(rs);
        }
        rs = getQuery(" SELECT * FROM Customer WHERE ID = " + idReceiver);
        receiver = getCustomerFromResultSet(rs);
        i = createQuery("UPDATE Customer SET amount = " + (receiver.getAmount()+amount) + " WHERE ID = " + idReceiver);
        if(i>0){
            rs = getQuery(" SELECT * FROM Customer WHERE ID = " + idReceiver);
            receiver = getCustomerFromResultSet(rs);
        }
        List<Customer> list = new ArrayList<Customer>();
        list.add(sender);
        list.add(receiver);
        return list;
    }

    private Customer getCustomerFromResultSet(ResultSet rs) {
        try {
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getLong(1));
                c.setName(rs.getString(2));
                c.setAmount(rs.getDouble(3));
                con.close();
                return c;
            }
        } catch (Exception e) {

        }
        return null;
    }

}


