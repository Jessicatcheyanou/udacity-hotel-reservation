package model.customer;

import java.util.regex.Pattern;

public class Customer {

    private String firstName;
    private String lastName;
    private String email;


    private void isValidEmail(final String email) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);
        assert false;
        if (!pattern.matcher(email).matches()){
            throw new  IllegalArgumentException("Error,invalid email");
        }
    }

    public Customer(String firstName, String lastName,String email) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public Customer() {
    }

    //getter methods
    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    //Setter Methods


    @Override
    public String toString() {
        return "Customer{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}
