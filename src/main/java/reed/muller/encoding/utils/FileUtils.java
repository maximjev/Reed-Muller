package reed.muller.encoding.utils;

import java.util.HashMap;
import java.util.Map;

import static java.lang.System.arraycopy;

public class FileUtils {

    private static String PROCESSED_IMAGE_POSTFIX = "-processed";

    private static String WITHOUT_ENCODING_POSTFIX = "-without-encoding";

    private static Map<String, byte[]> headerInfo = new HashMap<>();

    public static String appendPostfix(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(0, lastDot) + PROCESSED_IMAGE_POSTFIX + filename.substring(lastDot);
    }

    public static String appendPostfixWithoutEncoding(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(0, lastDot) + WITHOUT_ENCODING_POSTFIX + filename.substring(lastDot);
    }

    /*
    * atlieka: išsaugo failo viršelio informaciją
    */
    public static void saveHeader(byte[] bytes, String filename) {
        headerInfo.put(filename, bytes);
    }

    /*
    * atlieka: prijungia failo viršelio informaciją prie apdoroto failo
    */
    public static void appendHeader(byte[] bytes, String filename) {
        byte[] header = headerInfo.get(filename);
        arraycopy(header, 0, bytes, 0, header.length);
    }
}
