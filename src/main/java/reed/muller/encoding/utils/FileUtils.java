package reed.muller.encoding.utils;

public class FileUtils {

    private static String PROCESSED_IMAGE_POSTFIX = "-processed";

    private static String WITHOUT_ENCODING_POSTFIX = "-without-encoding";

    private static byte[] HEADER_INFO;

    public static String appendPostfix(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(0, lastDot) + PROCESSED_IMAGE_POSTFIX + filename.substring(lastDot);
    }

    public static String appendPostfixWithoutEncoding(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(0, lastDot) + WITHOUT_ENCODING_POSTFIX + filename.substring(lastDot);
    }

    public static String resolveExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(lastDot + 1);
    }

    public static void saveHeader(byte[] bytes) {
        HEADER_INFO = bytes;
    }

    public static void appendHeader(byte[] bytes) {
        for (int i = 0; i < HEADER_INFO.length; i++) {
            bytes[i] = HEADER_INFO[i];
        }
    }
}
