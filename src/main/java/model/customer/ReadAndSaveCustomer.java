package model.customer;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import service.CustomerService;

import java.io.InputStream;
import java.util.List;

public class ReadAndSaveCustomer  {

    private final CustomerService customerService = CustomerService.getSingleton();

    public void readAndSaveCustomer() throws Exception{
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.findAndRegisterModules();
        InputStream inputStream = TypeReference.class.getResourceAsStream("/json/customer.json");

        List<Customer> customerList = objectMapper.readValue(inputStream, new TypeReference<>() {
        });

        for (Customer customer:customerList){
            customerService.addCustomer(customer.getFirstName(),customer.getLastName(),customer.getEmail());
            System.out.println(customer);
        }
    }


}
