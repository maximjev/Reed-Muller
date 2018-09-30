package reed.muller.encoding.service;

import org.springframework.stereotype.Service;

@Service
public class ChannelService {

    public static double PROBABILITY = 50.0;

    public int[] send(int[] message) {
        for (int i = 0; i < message.length; i++) {
            if ((Math.random() * 100) < PROBABILITY) {
                message[i] = (message[i] + 1) % 2;
            }
        }
        return message;
    }
}
