package reed.muller.encoding.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Stream.of;

@Service
public class BasicScenarioService {

    private Encoder encoder;

    private Decoder decoder;

    private MessageConverter messageConverter;

    private ChannelService channelService;

    @Autowired
    public BasicScenarioService(Encoder encoder,
                                Decoder decoder,
                                MessageConverter messageConverter,
                                ChannelService channelService) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.messageConverter = messageConverter;
        this.channelService = channelService;
    }

    public String encode(String message) {
        return of(message)
                .map(messageConverter::validateVector)
                .map(messageConverter::parseVectorBits)
                .map(encoder::encode)
                .map(encoder::truncateMessage)
                .flatMap(Stream::of)
                .map(messageConverter::parseBitsToVector)
                .collect(joining());
    }

    public String sendThroughChannel(String message) {
        return of(message)
                .map(messageConverter::validateVector)
                .map(messageConverter::parseVectorBits)
                .map(encoder::encode)
                .map(encoder::truncateMessage)
                .map(channelService::send)
                .flatMap(Stream::of)
                .map(messageConverter::parseBitsToVector)
                .collect(joining());
    }

    public String decode(String vector) {
        return of(vector)
                .map(messageConverter::validateVector)
                .map(messageConverter::parseVectorBits)
                .map(decoder::decodeLine)
                .map(messageConverter::parseBitsToVector)
                .collect(joining());
    }
}
