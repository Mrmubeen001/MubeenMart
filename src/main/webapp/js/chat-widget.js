/**
 * MubeenMart AI Chat Widget Integration
 * Conforms to Section 11 & 17 specifications:
 * - Floating trigger and responsive panel
 * - fetch('/api/v1/chat', POST {message})
 * - Handles rate limit and degraded fallbacks seamlessly
 */
document.addEventListener('DOMContentLoaded', () => {
    const chatBtn = document.getElementById('chatWidgetBtn');
    const chatPanel = document.getElementById('chatPanel');
    const chatClose = document.getElementById('chatCloseBtn');
    const chatForm = document.getElementById('chatForm');
    const chatInput = document.getElementById('chatInput');
    const chatMessages = document.getElementById('chatMessages');
    const chipButtons = document.querySelectorAll('.chip-btn');

    if (!chatBtn || !chatPanel) return;

    // Toggle Chat Panel
    chatBtn.addEventListener('click', () => {
        const isOpen = chatPanel.style.display === 'flex';
        chatPanel.style.display = isOpen ? 'none' : 'flex';
        if (!isOpen) {
            chatInput.focus();
        }
    });

    chatClose.addEventListener('click', () => {
        chatPanel.style.display = 'none';
    });

    // Handle Quick FAQ chips
    chipButtons.forEach(btn => {
        btn.addEventListener('click', () => {
            const query = btn.getAttribute('data-query');
            if (query) {
                sendMessage(query);
            }
        });
    });

    // Handle User Input Submission
    chatForm.addEventListener('submit', (e) => {
        e.preventDefault();
        const text = chatInput.value.trim();
        if (text) {
            sendMessage(text);
            chatInput.value = '';
        }
    });

    function appendMessage(text, sender) {
        const msgDiv = document.createElement('div');
        msgDiv.className = `chat-msg ${sender}`;
        msgDiv.textContent = text;
        chatMessages.appendChild(msgDiv);
        chatMessages.scrollTop = chatMessages.scrollHeight;
        return msgDiv;
    }

    async function sendMessage(userText) {
        appendMessage(userText, 'user');

        const typingIndicator = appendMessage('Thinking...', 'bot');

        try {
            const contextPath = window.CONTEXT_PATH || '';
            const response = await fetch(`${contextPath}/api/v1/chat`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'Accept': 'application/json'
                },
                body: JSON.stringify({ message: userText })
            });

            const result = await response.json();

            if (result.success && result.data && result.data.reply) {
                typingIndicator.textContent = result.data.reply;
            } else if (result.error && result.error.message) {
                typingIndicator.textContent = result.error.message;
            } else {
                typingIndicator.textContent = "I'm having trouble connecting right now. Please feel free to ask about our shipping, payment, or selling policies!";
            }
        } catch (error) {
            console.error('Chatbot fetch error:', error);
            typingIndicator.textContent = "I am currently running in offline support mode. You can ask about product categories, shipping terms, returns, or seller registration!";
        }
        chatMessages.scrollTop = chatMessages.scrollHeight;
    }
});
