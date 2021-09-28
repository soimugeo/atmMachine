package com.bank.atm;

import com.bank.interaction.InteractionService;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

@Component
public class AtmService implements CommandLineRunner {

    private InteractionService interactionService;


    @Autowired
    public AtmService(InteractionService interactionService) {
        this.interactionService = interactionService;
    }

    @Override
    public void run(String... args) throws Exception {
        interactionService.startInteration();
    }
}
