package from.web.code;

//Source: https://github.com/michaelgantman/Mgnt/blob/master/src/main/java/com/mgnt/utils/StringUnicodeEncoderDecoder.java
/**
 * This class provides Unicode conversion utility methods that allow to convert a string into Unicode sequence and vice-versa. (See methods
 * descriptions for details)
 *
 * @author Michael Gantman
 */
public class StringUnicodeEncoderDecoder {
    private final static String UNICODE_PREFIX = "\\u";
    private final static String UPPER_CASE_UNICODE_PREFIX = "\\U";
    private final static String UPPER_CASE_UNICODE_PREFIX_REGEX = "\\\\U";
    private final static String DELIMITER = "\\\\u";

    public static String encodeStringToUnicodeSequence(String txt) {
        StringBuilder result = new StringBuilder();
        if (txt != null && !txt.isEmpty()) {
            for (int i = 0; i < txt.length(); i++) {
                result.append(convertCodePointToUnicodeString(Character.codePointAt(txt, i)));
                if (Character.isHighSurrogate(txt.charAt(i))) {
                    i++;
                }
            }
        }
        return result.toString();
    }
     
    public static String decodeUnicodeSequenceToString(String unicodeSequence) throws IllegalArgumentException {
        StringBuilder result = new StringBuilder();
        try {
            unicodeSequence = replaceUpperCase_U_WithLoverCase(unicodeSequence);
            unicodeSequence = unicodeSequence.trim().substring(UNICODE_PREFIX.length());
            for (String codePointStr : unicodeSequence.split(DELIMITER)) {
                result.append(Character.toChars(Integer.parseInt(codePointStr.trim(), 16)));
            }
        } catch (Exception e) {
            throw new IllegalArgumentException("Error occurred while converting unicode sequence String to String", e);
        }
        return result.toString();
    }

    private static String replaceUpperCase_U_WithLoverCase(String unicodeSequence) {
        String result = unicodeSequence;
        if(unicodeSequence != null && unicodeSequence.contains(UPPER_CASE_UNICODE_PREFIX)) {
            result = unicodeSequence.replaceAll(UPPER_CASE_UNICODE_PREFIX_REGEX, DELIMITER);
        }
        return result;
    }

 
    private static String convertCodePointToUnicodeString(int codePoint) {
        StringBuilder result = new StringBuilder(UNICODE_PREFIX);
        String codePointHexStr = Integer.toHexString(codePoint);
        codePointHexStr = codePointHexStr.startsWith("0") ? codePointHexStr.substring(1) : codePointHexStr;
        if (codePointHexStr.length() <= 4) {
            result.append(getPrecedingZerosStr(codePointHexStr.length()));
        }
        result.append(codePointHexStr);
        return result.toString();
    }

    private static String getPrecedingZerosStr(int codePointStrLength) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < 4 - codePointStrLength; i++) {
            result.append("0");
        }
        return result.toString();
    }
}