package com.dneydev.fluxia.config;

import com.dneydev.fluxia.service.ia.FinancasTools;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("ai")
public class ChatClientConfig {

    @Bean
    public ChatClient chatClient(ChatClient.Builder builder, FinancasTools financasTools) {
        return builder
                .defaultTools(financasTools)
                .build();
    }
}