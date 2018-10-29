package reed.muller.encoding.service;


import com.google.common.base.Splitter;
import com.google.common.primitives.Bytes;
import org.springframework.stereotype.Service;
import reed.muller.encoding.exception.EncodingException;

import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageConverter {

    /*
    * atlieka: konvertuoja žinutę į bitų masyvą
    * ima: žinutė
    * grąžina: bitų masyvas
    */
    public int[] convertToBits(String message) {
        return bytesToBits(ByteBuffer.wrap(message.getBytes(Charset.defaultCharset())));
    }

    /*
    * atlieka: konvertuoja baitų buferį į bitų masyvą
    * ima: baitų buferis
    * grąžina: bitų masyvas
    */
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

    /*
    * atlieka: konvertuoja bitų masyvą į žinutę
    * ima: bitų masyvas
    * grąžina: žinutė
    */
    public String convertToMessage(int[] bits) {
        return new String(Bytes.toArray(parseBits(bits)), Charset.defaultCharset());
    }

    /*
    * atlieka: konvertuoja bitų masyvą į baitų sąrašą
    * ima: bitų masyvas
    * grąžina: baitų sąrašas
    * */
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

    /*
    * atlieka: konvertuoja bitų vektorių į bitų masyvą
    */
    public int[] parseVectorBits(String vector) {
        String[] numbers = vector.split("");
        int[] bits = new int[numbers.length];

        for (int i = 0; i < bits.length; i++) {
            bits[i] = Integer.parseInt(numbers[i]);
        }
        return bits;
    }

    /*
    * atlieka: konvertuoja bitų seką į bitų vektorių
    */
    public String parseBitsToVector(int[] bits) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < bits.length; i++) {
            builder.append(bits[i]);
        }
        return builder.toString();
    }

    /*
    * atlieka: patikrina ar vektorius priklauso kūnui F(2)
    */
    public String validateVector(String vector) {
        String regex = "[0-1]+";
        if(vector.matches(regex)) {
            return vector;
        } else {
            throw new EncodingException("Invalid vector elements");
        }
    }
}
