package com.bank.exceptions;

public class AmountCanNotBeWithdrawnException extends Exception {

    public AmountCanNotBeWithdrawnException(String message){
        super(message);
    }
}
