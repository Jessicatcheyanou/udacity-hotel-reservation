package model.customer;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.regex.Pattern;

public class Customer {

    private final String firstName;

    private final String lastName;

    private final String email;


    public Customer(@JsonProperty("firstName")String firstName,@JsonProperty("lastName")String lastName, @JsonProperty("email")String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public boolean isValidEmail(final String email) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        return pattern.matcher(email).matches();
    }

    //getter methods
    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
