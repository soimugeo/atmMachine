package com.bank.atm;

import com.bank.interaction.InteractionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

@Service
public class AtmService implements CommandLineRunner {

    @Autowired
    InteractionService interactionService;
    @Override
    public void run(String... args) throws Exception {
        interactionService.startInteration();
    }
}
