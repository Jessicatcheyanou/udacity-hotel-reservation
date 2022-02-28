package service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import model.customer.Customer;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;


public class CustomerService {

    private static final CustomerService SINGLETON = new CustomerService();
    private final Map<String,Customer> customers = new HashMap<>();

    private CustomerService() {
    }

    public static CustomerService getSingleton(){
        return SINGLETON;
    }

    public void addCustomer(final String firstName,final String lastName,final String email){

        if (customers.values().stream().noneMatch(e -> e.isValidEmail(email))){
            customers.put(email,new Customer(firstName,lastName,email));
        }

    }

    public Customer getCustomer(final String customerEmail){

        for (Customer customer:customers.values()){
            if (customer.isValidEmail(customerEmail))
            return customer;
        }

        return null;

    }

    public Collection<Customer> getAllCustomers(){
        return customers.values();
    }

}
