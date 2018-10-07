package reed.muller.encoding.service;


import com.google.common.base.Splitter;
import com.google.common.primitives.Bytes;
import org.springframework.stereotype.Service;
import org.thymeleaf.util.ArrayUtils;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class MessageConverter {

    private static String CHARSET = "UTF-8";

    public int[] convertToBits(String message) throws UnsupportedEncodingException {
        IntBuffer buffer = ByteBuffer.wrap(message.getBytes(CHARSET)).asIntBuffer();

        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < buffer.remaining(); i++) {
            builder.append(String.format("%32s",
                    Integer.toBinaryString(buffer.get(i))).replace(' ', '0'));
        }

        char[] bits = builder.toString().toCharArray();

        int[] result = new int[bits.length];

        for (int i = 0; i < bits.length; i++) {
            result[i] = Character.getNumericValue(bits[i]);
        }
        return result;
    }

    public String convertToMessage(int[] bits) throws UnsupportedEncodingException {
        Iterable<String> byteString = Splitter
                .fixedLength(8)
                .split(Arrays.stream(bits)
                .mapToObj(Integer::toString)
                .collect(Collectors.joining()));

        List<Byte> bytes = new ArrayList<>();
        byteString.forEach(b -> bytes.add((byte)Integer.parseInt(b, 2)));

        return new String(Bytes.toArray(bytes), CHARSET);
    }
}
