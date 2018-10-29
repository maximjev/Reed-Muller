package reed.muller.encoding.utils;

import reed.muller.encoding.exception.EncodingException;

public class CustomStringUtils {

    private static int[] compare(CharSequence str1, CharSequence str2) {
        if (str1.length() != str2.length()) {
            throw new EncodingException("Comparing error, strings differ in length");
        }
        int[] diff = new int[str1.length()];
        for (int i = 0; i < str1.length(); i++) {
            if (str1.charAt(i) != str2.charAt(i)) {
                diff[i] = i;
            }
        }
        return diff;
    }

    private static String buildDifferenceString(int[] diff) {
        boolean error = false, errors = false;
        StringBuilder builder = new StringBuilder();
        builder.append("Klaidos įvyko: ");
        for (int i = 0; i < diff.length; i++) {
            if (diff[i] > 0) {
                if(error) {
                    errors = true;
                }
                error = true;
                builder.append(i + 1).append(' ');
            }
        }

        if (errors) {
            builder.append("pozicijose");
        } else {
            builder.append("pozicijoje");
        }
        if (error) {
            return builder.toString();
        } else {
            return "Klaidų neįvyko";
        }
    }

    /*
    * atlieka: paskaičiuoja klaidas, skirtumus tarp eilučių ir jų pozicijas
    */
    public static String calculateErrors(String str1, String str2) {
        return buildDifferenceString(compare(str1, str2));
    }
}
