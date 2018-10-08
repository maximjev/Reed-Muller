package reed.muller.encoding.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
public class EncodingConfiguration {

    @Value("${encoding.m}")
    private int m;

    @Value("${encoding.channel-probability}")
    private double channelProbability;

    public int getM() {
        return m;
    }

    public double getChannelProbability() {
        return channelProbability;
    }
}
