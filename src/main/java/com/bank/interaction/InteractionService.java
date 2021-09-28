package com.bank.interaction;

import com.bank.atm.AtmResponseDTO;
import com.bank.cash.CashServiceImpl;
import com.bank.exceptions.AmountCanNotBeWithdrawnException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.Scanner;

@Slf4j
@Component
public class InteractionService {


    private CashServiceImpl cashServiceImpl;

    @Autowired
    public InteractionService(CashServiceImpl cashServiceImpl){
        this.cashServiceImpl = cashServiceImpl;
    }

    public void startInteration() {
        Scanner scanner = new Scanner(System.in);
        while (true){
            printOptions();

            int selection = scanner.nextInt();

            switch (selection){
                case 1 -> {
                    System.out.println("Insert amount you want to withdraw:");

                    try {
                        AtmResponseDTO atmResponseDTO = cashServiceImpl.getRequiredAmount(scanner.nextInt());
                        System.out.println("Operation was made using following bills: "+atmResponseDTO);
                    } catch (AmountCanNotBeWithdrawnException e) {
                        log.error(e.getLocalizedMessage());
                        log.error("Please try another amount!");
                    }
                }
                case 2 -> {
                    System.out.println("Thank you for playing! Bye!");
                    System.exit(0);
                }
            }

        }
    }

    private void printOptions(){
        System.out.println("----------------------------------------------------");
        System.out.println("ATM MACHINE");
        System.out.println("Please choose one of the options by inserting number");
        System.out.println("1: Withdraw");
        System.out.println("2: Exit");
    }
}
