package com.bank.interaction;

import com.bank.cash.CashService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Scanner;

@Service
public class InteractionService {


    private CashService cashService;

    @Autowired
    public InteractionService(CashService cashService){
        this.cashService = cashService;
    }

    public void startInteration(){
        Scanner scanner = new Scanner(System.in);
        while (true){
            System.exit(0);
        }
    }
}
