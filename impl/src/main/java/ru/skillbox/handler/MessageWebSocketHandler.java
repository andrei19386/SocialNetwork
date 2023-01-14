package ru.skillbox.handler;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.AbstractWebSocketHandler;
import ru.skillbox.model.Person;
import ru.skillbox.repository.PersonRepository;
import ru.skillbox.request.MessageRq;
import ru.skillbox.service.DialogService;


import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

@Log4j2
@Component
@RequiredArgsConstructor
public class MessageWebSocketHandler extends AbstractWebSocketHandler {

    private final DialogService dialogService;
    private final PersonRepository personRepository;
    Map<String, WebSocketSession> sessionMap = new ConcurrentHashMap<>();

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        String payload = message.getPayload();
        MessageRq messageRq = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .readValue(payload, MessageRq.class);

        Optional<Person> recipient = personRepository.findById(messageRq.getData().getRecipientId());

        for (Map.Entry<String, WebSocketSession> entry : sessionMap.entrySet()) {
            if(entry.getKey().equals(recipient.get().getEmail())) {
                entry.getValue().sendMessage(new TextMessage(payload));
            }
        }

        dialogService.saveMessage(messageRq);
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        if (session.getPrincipal() != null)
            sessionMap.put(session.getPrincipal().getName(), session);
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        if (session.getPrincipal() != null)
            sessionMap.remove(session.getPrincipal().getName());
    }
}
