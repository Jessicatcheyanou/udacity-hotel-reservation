package service;

import model.customer.Customer;

import java.util.*;
import java.util.stream.Stream;

public class CustomerService {

    private static final CustomerService SINGLETON = new CustomerService();
    private final Map<String,Customer> customers = new HashMap<>();
    private final Map<String,Customer> loadCustomers = new HashMap<>();

    public CustomerService() {
    }

    public static CustomerService getSingleton(){
        return SINGLETON;
    }

    public void addCustomer(final String firstName,final String lastName,final String email){
        if (customers.values().stream().noneMatch(e -> Objects.equals(e.getEmail(), email))){
            customers.put(email,new Customer(firstName,lastName,email));
        }
    }

    public Customer getCustomer(final String customerEmail){

        return customers.get(customerEmail);
    }

    public Collection<Customer> getAllCustomers(){
        return customers.values();
    }

//    public void loadCustomersTestData(){
//        loadCustomers.put("jessicatcheyanou@gmail.com", new Customer("jessicatcheyanou@gmail.com","Jessica","Tcheyanou"));
//        loadCustomers.put("tcheyanou@outlook.com", new Customer("tcheyanou@outlook.com","Jessica","Tcheyanou"));
//        loadCustomers.put("jessica.ines@ubuea.cm", new Customer("jessica.ines@ubuea.cm","Jessica","Ines"));
//
//        customers.putAll(loadCustomers);
//    }
}
