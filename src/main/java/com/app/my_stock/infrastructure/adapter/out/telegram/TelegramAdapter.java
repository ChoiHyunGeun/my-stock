package com.app.my_stock.infrastructure.adapter.out.telegram;

import com.app.my_stock.application.port.out.NotificationPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientException;

import java.util.Map;

@Component
public class TelegramAdapter implements NotificationPort {

	private static final Logger log = LoggerFactory.getLogger(TelegramAdapter.class);

	private final RestClient restClient;
	private final String botToken;
	private final String chatId;

	public TelegramAdapter(
			RestClient.Builder restClientBuilder,
			@Value("${telegram.bot-token}") String botToken,
			@Value("${telegram.chat-id}") String chatId
	) {
		this.restClient = restClientBuilder
				.baseUrl("https://api.telegram.org")
				.build();
		this.botToken = botToken;
		this.chatId = chatId;
	}

	@Async
	@Override
	public void sendReport(String message) {
		if (message == null || message.isBlank()) {
			return;
		}
		if (botToken == null || botToken.isBlank() || chatId == null || chatId.isBlank()) {
			log.warn("Telegram config missing. Skip sending. telegram.bot-token or telegram.chat-id is blank.");
			return;
		}

		try {
			restClient.post()
					.uri("/bot{token}/sendMessage", botToken)
					.contentType(MediaType.APPLICATION_JSON)
					.body(Map.of(
							"chat_id", chatId,
							"text", message,
							"parse_mode", "Markdown",
							"disable_web_page_preview", true
					))
					.retrieve()
					.toBodilessEntity();
		} catch (RestClientException e) {
			// 알림 실패가 전체 파이프라인/스케줄러를 깨면 안 됨
			log.warn("Telegram send failed.", e);
		} catch (Exception e) {
			log.warn("Telegram send unexpected failure.", e);
		}
	}
}

