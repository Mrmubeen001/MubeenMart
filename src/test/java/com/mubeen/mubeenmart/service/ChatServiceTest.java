package com.mubeen.mubeenmart.service;

import com.mubeen.mubeenmart.ai.MockChatProvider;
import com.mubeen.mubeenmart.exception.AppException;
import com.mubeen.mubeenmart.exception.ValidationException;
import com.mubeen.mubeenmart.service.impl.ChatServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ChatService AI Guardrails and Cache Tests")
class ChatServiceTest {

    @Mock
    private HttpSession session;

    private ChatService chatService;

    @BeforeEach
    void setUp() {
        chatService = new ChatServiceImpl(new MockChatProvider());
    }

    @Test
    @DisplayName("Should answer order tracking domain question correctly")
    void testChatOrderTracking() throws Exception {
        Map<String, Object> sessionAttributes = new HashMap<>();
        when(session.getAttribute(anyString())).thenAnswer(invocation -> sessionAttributes.get(invocation.getArgument(0)));
        doAnswer(invocation -> {
            sessionAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(session).setAttribute(anyString(), any());

        String reply = chatService.processChat(session, "How do I track my order status?");
        assertNotNull(reply);
        assertTrue(reply.contains("Pending") || reply.contains("Orders tab") || reply.contains("Delivered"));
    }

    @Test
    @DisplayName("Should enforce input length cap guardrail per Section 17")
    void testChatLengthCap() {
        String longMessage = "a".repeat(501);
        assertThrows(ValidationException.class, () -> chatService.processChat(session, longMessage));
    }

    @Test
    @DisplayName("Should cache identical repeated questions in session per Section 17 Rule 4")
    void testChatQueryCaching() throws Exception {
        Map<String, Object> sessionAttributes = new HashMap<>();
        when(session.getAttribute(anyString())).thenAnswer(invocation -> sessionAttributes.get(invocation.getArgument(0)));
        doAnswer(invocation -> {
            sessionAttributes.put(invocation.getArgument(0), invocation.getArgument(1));
            return null;
        }).when(session).setAttribute(anyString(), any());

        String q = "What is the shipping policy?";
        String reply1 = chatService.processChat(session, q);
        String reply2 = chatService.processChat(session, q);

        assertEquals(reply1, reply2);
        assertTrue(sessionAttributes.containsKey("chatQueryCache"));
    }
}
