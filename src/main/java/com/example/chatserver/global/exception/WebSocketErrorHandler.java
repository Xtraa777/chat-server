package com.example.chatserver.global.exception;

import java.nio.charset.StandardCharsets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.StompSubProtocolErrorHandler;

@Component
@Slf4j
public class WebSocketErrorHandler extends StompSubProtocolErrorHandler {

    @Override
    public Message<byte[]> handleClientMessageProcessingError(
        Message<byte[]> clientMessage, Throwable ex) {

        Throwable cause = (ex.getCause() != null) ? ex.getCause() : ex;

        if (cause instanceof BusinessException) {
            BusinessException businessException = (BusinessException) cause;
            ErrorCode errorCode = businessException.getErrorCode();

            log.warn("WebSocket BusinessException: code={}, message={}, clientMessage={}",
                errorCode.name(), errorCode.getMessage(), clientMessage.getHeaders());

            return prepareErrorMessage(clientMessage, errorCode);
        }
        log.error("WebSocket unexpected error occurred", ex);

        return super.handleClientMessageProcessingError(clientMessage, ex);
    }

    private Message<byte[]> prepareErrorMessage(
        Message<byte[]> clientMessage, ErrorCode errorCode) {

        StompHeaderAccessor accessor = StompHeaderAccessor.create(StompCommand.ERROR);
        accessor.setMessage(errorCode.getStatus().getReasonPhrase());
        accessor.setLeaveMutable(true);

        String errorMessage = String.format("{\"code\":\"%s\", \"message\":\"%s\"}",
            errorCode.name(), errorCode.getMessage());

        return MessageBuilder.createMessage(
            errorMessage.getBytes(StandardCharsets.UTF_8),
            accessor.getMessageHeaders()
        );
    }
}
