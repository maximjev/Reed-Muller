package reed.muller.encoding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reed.muller.encoding.config.EncodingConfiguration;

@Service
public class ChannelService {

    private double probability;

    @Autowired
    public ChannelService(EncodingConfiguration configuration) {
        this.probability = configuration.getChannelProbability();
    }

    public int[] send(int[] message) {
        for (int i = 0; i < message.length; i++) {
            if ((Math.random() * 100) < probability) {
                message[i] = (message[i] + 1) % 2;
            }
        }
        return message;
    }

    public int[][] send(int[][] message) {
        for (int i = 0; i < message.length; i++) {
            message[i] = send(message[i]);
        }
        return message;
    }
}
