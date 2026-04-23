package com.app.my_stock.application.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class MarketDecisionAgent {

	private final ChatClient chatClient;

	public MarketDecisionAgent(ChatClient marketDecisionChatClient) {
		this.chatClient = marketDecisionChatClient;
	}

	public String decide(List<String> symbols, Instant now) {
		String report = """
				현재 시각: %s (UTC)
				모니터링 심볼: %s

				요청:
				- 각 심볼에 대해 필요 시 getCurrentPrice(symbol)와 getHistoricalMovingAverage(symbol, days)를 호출해서 상태를 평가해.
				- 200일 이동평균 돌파(상향/하향) 같은 특이 사항이 있으면 텔레그램 알림 메시지 1개를 작성해.
				- 없으면 NO_ALERT.
				""".formatted(now, String.join(",", symbols));

		return chatClient.prompt()
				.user(report)
				.call()
				.content();
	}
}

