package org.robbie.modulareducationenvironment.websockets;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	private final String SECRET;
	private final AuthChannelInterceptor authChannelInterceptor;

	public WebSocketConfig(
			@Value("${app.jwt.verifier.key}") String secret,
			AuthChannelInterceptor authChannelInterceptor) {
		this.SECRET = secret;
		this.authChannelInterceptor = authChannelInterceptor;
	}

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/quiz");
		config.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/gs-guide-websocket").setAllowedOrigins("http://localhost:5173"); // Allow CORS for testing; restrict in production;//TODO REMOVE ALLOW ALL
		registry.addEndpoint("/ws").setAllowedOrigins("http://localhost:5173").withSockJS(); // Register the WebSocket endpoint for clients to connect
	}


	@Override
	public void configureClientInboundChannel(ChannelRegistration registration) {
		registration.interceptors(authChannelInterceptor); // Register the interceptor
	}

}
