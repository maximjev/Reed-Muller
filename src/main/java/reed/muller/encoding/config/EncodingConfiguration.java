package reed.muller.encoding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class EncodingConfiguration {

    @Value("encoding.m")
    private int m;

    @Value("encoding.channel-probability")
    private int channelProbability;

    public int getM() {
        return m;
    }

    public int getChannelProbability() {
        return channelProbability;
    }
}
