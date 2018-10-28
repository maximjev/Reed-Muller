package reed.muller.encoding.service;


import com.google.common.base.Splitter;
import com.google.common.primitives.Bytes;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageConverter {

    public int[] convertToBits(String message) {
        return bytesToBits(ByteBuffer.wrap(message.getBytes(Charset.defaultCharset())));
    }

    public int[] bytesToBits(ByteBuffer buffer) {
        buffer.rewind();
        StringBuilder builder = new StringBuilder();
        while (buffer.hasRemaining()) {
            builder.append(String.format("%32s",
                    Integer.toBinaryString(buffer.get())).replace(' ', '0'));
        }

        char[] bits = builder.toString().toCharArray();

        int[] result = new int[bits.length];

        for (int i = 0; i < bits.length; i++) {
            result[i] = Character.getNumericValue(bits[i]);
        }
        return result;
    }

    public String convertToMessage(int[] bits) {
        return new String(Bytes.toArray(parseBits(bits)), Charset.defaultCharset());
    }

    public List<Byte> parseBits(int[] bits) {
        Iterable<String> byteString = Splitter
                .fixedLength(32)
                .split(Arrays.stream(bits)
                        .mapToObj(Integer::toString)
                        .collect(Collectors.joining()));

        List<Byte> bytes = new ArrayList<>();
        byteString.forEach(b -> bytes.add(parseBits(b)));
        return bytes;
    }

    private byte parseBits(String bits) {
        return (byte) new BigInteger(bits, 2).intValue();
    }
}
