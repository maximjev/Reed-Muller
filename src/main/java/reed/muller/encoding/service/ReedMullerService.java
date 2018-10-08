package reed.muller.encoding.service;

import org.springframework.stereotype.Service;
import reed.muller.encoding.exception.EncodingException;

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ReedMullerService {

    private MessageConverter messageConverter;

    private Encoder encoder;

    private Decoder decoder;

    private ChannelService channelService;

    public ReedMullerService(MessageConverter messageConverter,
                             Encoder encoder,
                             Decoder decoder,
                             ChannelService channelService) {
        this.messageConverter = messageConverter;
        this.encoder = encoder;
        this.decoder = decoder;
        this.channelService = channelService;
    }

    public String process(String message) {
        return Stream
                .of(message)
                .map(this::handleMessage)
                .map(encoder::encode)
                .map(encoder::truncateMessage)
                .map(channelService::send)
                .map(decoder::decode)
                .map(this::handleBits)
                .collect(Collectors.joining());
    }


    private String handleBits(int[] bits) {
        try {
            return messageConverter.convertToMessage(bits);
        } catch (UnsupportedEncodingException ex) {
            throw new EncodingException();
        }
    }

    private int[] handleMessage(String message) {
        try {
            return messageConverter.convertToBits(message);
        } catch (UnsupportedEncodingException ex) {
            throw new EncodingException();
        }
    }
}
