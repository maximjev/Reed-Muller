package reed.muller.encoding.utils;

public class FileUtils {

    private static String PROCESSED_IMAGE_POSTFIX = "-processed";

    public static String appendPostfix(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(0, lastDot) + PROCESSED_IMAGE_POSTFIX + filename.substring(lastDot);
    }

    public static String resolveExtension(String filename) {
        int lastDot = filename.lastIndexOf('.');
        return filename.substring(lastDot + 1);
    }
}
