package model.customer;

import java.util.regex.Pattern;

public class CustomerEmailValidation {

    public boolean isValidEmail(final String email) {
        String emailRegex = "^(.+)@(.+).(.+)$";
        Pattern pattern = Pattern.compile(emailRegex);

        if (!pattern.matcher(email).matches()){
            return false;
        }
        return true;
    }
}
