package com.app.my_stock.application.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MarketDecisionAgentConfig {

	@Bean
	public ChatClient marketDecisionChatClient(ChatClient.Builder builder, MarketTools tools) {
		return builder
				.defaultSystem(DecisionAgentSystemPrompt.TEXT)
				.defaultTools(tools)
				.build();
	}
}

