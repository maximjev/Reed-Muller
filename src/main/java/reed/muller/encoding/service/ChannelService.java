package reed.muller.encoding.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reed.muller.encoding.config.EncodingConfiguration;

@Service
public class ChannelService {

    private double probability;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ChannelService(EncodingConfiguration configuration) {
        this.probability = configuration.getChannelProbability();
    }

    /*
    * atlieka: siuntimas triukšmingu kanalu
    * ima: vektorius
    * grąžina: iškraipytas vektorius
    */
    public int[] send(int[] message) {
        for (int i = 0; i < message.length; i++) {
            if (isError()) {
                message[i] = (message[i] + 1) % 2;
            }
        }
        return message;
    }

    /*
     * atlieka: siuntimas triukšmingu kanalu
     * ima: vektorių masyvas
     * grąžina: iškraipytų vektorių masyvas
     */
    public int[][] send(int[][] message) {
        LOG.debug("Will send message through noisy channel");
        for (int i = 0; i < message.length; i++) {
            message[i] = send(message[i]);
        }
        LOG.debug("sending finished");
        return message;
    }

    private boolean isError() {
        return Math.random() < probability;
    }
}
