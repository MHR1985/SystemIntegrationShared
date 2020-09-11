package com.example.demo.server;

import org.springframework.web.bind.annotation.*;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
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

    public ResultSet runQuery(String query) throws Exception {
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = con.prepareStatement(query);
        return ps.executeQuery();
    }

    @GetMapping("/millionaires")
    public List<Customer> getMillionaires() {
        List<Customer> list = new ArrayList<Customer>();
        try {
            ResultSet rs = runQuery("select * from Customer where amount >= 100000");
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getLong(1));
                c.setName(rs.getString(2));
                c.setAmount(rs.getDouble(3));
                System.out.println(c);
                list.add(c);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        List<Customer> list = new ArrayList<Customer>();
        try {
            ResultSet rs = runQuery("select * from Customer");
            while (rs.next()) {
                Customer c = new Customer();
                c.setId(rs.getLong(1));
                c.setName(rs.getString(2));
                c.setAmount(rs.getDouble(3));
                System.out.println(c);
                list.add(c);
            }
            con.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        return list;
    }


    @PostMapping("/create")
    public Customer createCustomer(long id, String name, Double amount) {
        Customer c = new Customer();
        c.setId(id);
        c.setName(name);
        c.setAmount(amount);
        try{
            ResultSet rs = runQuery(
                    "insert into customer " +
                            "values ("+id+","+name+","+amount+");");
            if (rs.next()) con.close(); return c;
        } catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
    }

    @GetMapping("/customer")
    public Customer getCustomer(@RequestParam long id) throws Exception {

        ResultSet rs = runQuery("select * from Customer where id = '" + id + "'");
        return getCustomerFromResultSet(rs);

    }

    @PutMapping("/customer")
    public Customer editCustomer(@RequestParam long id,@RequestParam String name) throws Exception {
        ResultSet rs = runQuery("UPDATE Customer SET name = '" + name + "' WHERE ID = '" + id + "'; SELECT * FROM Customer WHERE ID = '" + id + "';");
        return getCustomerFromResultSet(rs);
    }

    @PutMapping("/transfer")
    public Customer transferMoney(@RequestParam long id, @RequestParam double amount) throws Exception {
        ResultSet rs = runQuery("UPDATE Customer SET amount = '" + amount + "' WHERE ID = '" + id + "'; SELECT * FROM Customer WHERE ID = '" + id + "';");
        return getCustomerFromResultSet(rs);

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

