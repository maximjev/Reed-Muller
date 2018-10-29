package reed.muller.encoding.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static reed.muller.encoding.utils.FileUtils.appendPostfix;

@Service
public class ReedMullerService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private MessageConverter messageConverter;

    private ImageConverter imageConverter;

    private Encoder encoder;

    private Decoder decoder;

    private ChannelService channelService;

    private StorageService storageService;

    public ReedMullerService(MessageConverter messageConverter,
                             ImageConverter imageConverter,
                             Encoder encoder,
                             Decoder decoder,
                             ChannelService channelService,
                             StorageService storageService) {
        this.messageConverter = messageConverter;
        this.encoder = encoder;
        this.decoder = decoder;
        this.channelService = channelService;
        this.imageConverter = imageConverter;
        this.storageService = storageService;
    }

    /*
     * atlieka: siunčia užkoduotą failą kanalu
     * ima: failas
     * grąžina: dekoduoto failo pavadinimas
     */
    public String processImage(MultipartFile file) {
        LOG.debug("Will process image: " + file.getOriginalFilename());
        storageService.store(file);
        String processedFilename = appendPostfix(file.getOriginalFilename());
        Stream.of(file)
                .map(imageConverter::convertToBits)
                .map(encoder::encode)
                .map(encoder::truncateMessage)
                .map(channelService::send)
                .map(decoder::decode)
                .map(bits -> imageConverter.convertToImage(bits, file.getOriginalFilename()))
                .forEach(bytes ->
                        storageService.store(bytes, processedFilename));

        LOG.debug("process finished");
        return processedFilename;
    }

    /*
     * atlieka: siunčia užkoduota žinutę kanalu
     * ima: žinutė
     * grąžina: dekoduota žinutė
     */
    public String process(String message) {
        LOG.debug("Will process message");
        return Stream.of(message)
                .map(messageConverter::convertToBits)
                .map(encoder::encode)
                .map(encoder::truncateMessage)
                .map(channelService::send)
                .map(decoder::decode)
                .map(messageConverter::convertToMessage)
                .collect(Collectors.joining());
    }
}
