package server.database;

import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.ArrayList;
import java.util.List;

public class TestSimpMessagingTemplate extends SimpMessagingTemplate {

    private List<String> destinations;

    /**
     * Create a new {@link SimpMessagingTemplate} instance.
     *
     * @param messageChannel the message channel (never {@code null})
     */
    public TestSimpMessagingTemplate(MessageChannel messageChannel) {
        super(messageChannel);
        this.destinations = new ArrayList<>();
    }

    @Override
    public void convertAndSend(String destination, Object payload) throws MessagingException {
        this.destinations.add(destination);
    }

    public List<String> getDestinations() {
        return destinations;
    }

    public void setDestinations(List<String> destinations) {
        this.destinations = destinations;
    }
}
