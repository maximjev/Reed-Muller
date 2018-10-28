package reed.muller.encoding.service;

import com.google.common.primitives.Bytes;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reed.muller.encoding.exception.ParsingException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import static reed.muller.encoding.utils.FileUtils.appendHeader;
import static reed.muller.encoding.utils.FileUtils.saveHeader;

@Service
public class ImageConverter {

    private MessageConverter messageConverter;

    private final Logger LOG = LoggerFactory.getLogger(this.getClass());

    @Autowired
    public ImageConverter(MessageConverter messageConverter) {
        this.messageConverter = messageConverter;
    }

    public int[] convertToBits(MultipartFile file) {
        try {
            LOG.debug("Will convert to bits file: " + file.getOriginalFilename());
            byte[] bytes = IOUtils.toByteArray(file.getInputStream());
            ByteBuffer buffer = ByteBuffer.wrap(bytes);

            saveHeader(Arrays.copyOfRange(bytes, 0, 54));
            return messageConverter.bytesToBits(buffer);
        } catch (IOException ex) {
            throw new ParsingException("Failed to convert image to bytes");
        }
    }

    public byte[] convertToImage(int[] bits) {
        LOG.debug("Will convert bits to image");
        List<Byte> bytes = messageConverter.parseBits(bits);

        byte[] byteArray = Bytes.toArray(bytes);
        appendHeader(byteArray);
        return byteArray;
    }
}
