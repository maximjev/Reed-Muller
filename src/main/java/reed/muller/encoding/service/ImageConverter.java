package reed.muller.encoding.service;

import com.google.common.base.Splitter;
import com.google.common.primitives.Bytes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reed.muller.encoding.exception.EncodingException;
import reed.muller.encoding.exception.ParsingException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static reed.muller.encoding.utils.FileUtils.resolveExtension;

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

            BufferedImage image = ImageIO.read(file.getInputStream());
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            ImageIO.write(image, resolveExtension(file.getOriginalFilename()), stream);

            stream.flush();
            ByteBuffer buffer = ByteBuffer.wrap(stream.toByteArray());
            stream.close();

            return messageConverter.bytesToBits(buffer);
        } catch (IOException ex) {
            throw new ParsingException("Failed to convert image to bytes");
        }
    }

    public ByteArrayInputStream convertToImage(int[] bits) {
        LOG.debug("Will convert bits to image");
        Iterable<String> byteString = Splitter
                .fixedLength(32)
                .split(Arrays.stream(bits)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining()));

        List<Byte> bytes = new ArrayList<>();
        byteString.forEach(b -> bytes.add((byte) Integer.parseInt(b, 2)));

        return new ByteArrayInputStream(Bytes.toArray(bytes));
    }
}
