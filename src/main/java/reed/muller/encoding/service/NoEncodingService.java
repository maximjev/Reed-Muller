package reed.muller.encoding.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.stream.Collectors;
import java.util.stream.Stream;

import static reed.muller.encoding.utils.FileUtils.appendPostfixWithoutEncoding;

@Service
public class NoEncodingService {

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    private ImageConverter imageConverter;

    private ChannelService channelService;

    private StorageService storageService;

    private MessageConverter messageConverter;

    @Autowired
    public NoEncodingService(ImageConverter imageConverter,
                             ChannelService channelService,
                             StorageService storageService,
                             MessageConverter messageConverter) {
        this.imageConverter = imageConverter;
        this.channelService = channelService;
        this.storageService = storageService;
        this.messageConverter = messageConverter;
    }

    public String processImage(MultipartFile file) {
        LOG.debug("Will send through channel image: " + file.getOriginalFilename());
        String processedFilename = appendPostfixWithoutEncoding(file.getOriginalFilename());
        //String extension = resolveExtension(file.getOriginalFilename());
        Stream.of(file)
                .map(imageConverter::convertToBits)
                .map(channelService::send)
                .map(bits -> imageConverter.convertToImage(bits))
                .forEach(bytes ->
                        storageService.store(bytes, processedFilename));

        LOG.debug("process without encoding finished finished");
        return processedFilename;
    }

    public String processMessage(String message) {
        return Stream.of(message)
                .map(messageConverter::convertToBits)
                .map(channelService::send)
                .map(messageConverter::convertToMessage)
                .collect(Collectors.joining());
    }
}
