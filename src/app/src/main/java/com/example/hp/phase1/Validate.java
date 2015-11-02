package com.example.hp.phase1;

/**
 * Created by Balaji on 2/25/2015.
 */
public class Validate {

    String SignUpPage(UserObj user) {
        if (user.firstName.isEmpty()) {
            return buildMessage("first name");
        } else if (user.mobileNum.isEmpty() || !(user.mobileNum.trim().length() == 10) || (user.mobileNum == null)) {

            return buildMessage("valid PhoneNumber");
        } else if (user.password.isEmpty()) {
            return buildMessage("password");
        } else if (user.repPassword.isEmpty()) {
            return "Please re-enter Password";
        } else if (!(user.password.length() >= 8)) {
            return buildMessage("password");
        } else if (!user.password.equals(user.repPassword))
            return buildMessage("Please re-enter Correct Password");
        return "ok";
    }

    private String buildMessage(String Arg1) {
        return "Please enter a " + Arg1;
    }

    public String MainActivity(String mobile, String password) {
        if (mobile.isEmpty())
            return buildMessage("username");
        else if (password.isEmpty()) {
            return buildMessage("password");
        }
        return "ok";
    }
}
