package com.sorinbratosin.licenta.Security;

public class EmailAlreadyTakenException extends Exception {

    public EmailAlreadyTakenException(String message) {
        super(message);
    }
}
