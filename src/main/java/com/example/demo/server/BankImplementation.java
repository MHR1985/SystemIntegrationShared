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
public class BankImplementation extends UnicastRemoteObject  implements BankInterface  {

    public static String url = "jdbc:h2:file:D:/SystemIntegrationShared/src/main/resources/shadybank/bank;AUTO_SERVER=TRUE";
    public static String user = "sa";
    public static String password = "";
    public static String driver = "org.h2.Driver";

    BankImplementation()throws RemoteException{}

    private Connection con;

    public ResultSet runQuery(String query) throws Exception {
        Class.forName(driver);
        Connection con = DriverManager.getConnection(url, user, password);
        PreparedStatement ps = con.prepareStatement(query);
        ResultSet rs=ps.executeQuery();
        return rs;
    }

    @GetMapping("/millionaires")
    public List<Customer> getMillionaires() {
        List<Customer> list=new ArrayList<Customer>();
        try
        {
            ResultSet rs= runQuery("select * from Customer where amount >= 100000");
            while(rs.next())
            {
                Customer c=new Customer();
                c.setId(rs.getLong(1));
                c.setName(rs.getString(2));
                c.setAmount(rs.getDouble(3));
                System.out.println(c);
                list.add(c);
            }
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return list;
    }

    @GetMapping("/customers")
    public List<Customer> getAllCustomers() {
        List<Customer> list=new ArrayList<Customer>();
        try
        {
            ResultSet rs= runQuery("select * from Customer");
            while(rs.next())
            {
                Customer c=new Customer();
                c.setId(rs.getLong(1));
                c.setName(rs.getString(2));
                c.setAmount(rs.getDouble(3));
                System.out.println(c);
                list.add(c);
            }
            con.close();
        }
        catch(Exception e)
        {
            System.out.println(e);
        }
        return list;
    }

    @PostMapping("/customer")
    public Customer createCustomer() {
        return null;
    }

    @GetMapping("/customer")
    public Customer getCustomer(@RequestParam long id) {
        try {
            ResultSet rs = runQuery("select * from Customer where id = '" + id + "'");
            if(rs.next())
            {
                Customer c= new Customer();
                c.setId(rs.getLong(1));
                c.setName(rs.getString(2));
                c.setAmount(rs.getDouble(3));
                con.close();
                return c;
            }
        }catch(Exception e){

        }
        return null;
    }

    @PutMapping("/customer")
    public Customer editCustomer() {
        return null;
    }

    @PutMapping("/transfer")
    public Customer transferMoney() throws RemoteException {
        return null;
    }
}
