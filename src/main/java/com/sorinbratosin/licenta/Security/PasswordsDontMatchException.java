package com.sorinbratosin.licenta.Security;

public class PasswordsDontMatchException extends Exception{

    public PasswordsDontMatchException(String message) {
        super(message);
    }
}
