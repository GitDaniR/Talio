package server.database;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;

public class TestMessageChannel implements MessageChannel {
    @Override
    public boolean send(Message<?> message) {
        return false;
    }

    @Override
    public boolean send(Message<?> message, long timeout) {
        return false;
    }
}
