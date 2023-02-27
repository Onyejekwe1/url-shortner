package com.irembo.urlshortner.utils;

import java.math.BigInteger;

public class Base62Utils {
    private static final String BASE62_CHARS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
    private static final int BASE62 = 62;

    public static String fromBase10(long i) {
        StringBuilder sb = new StringBuilder(1);
        while (i > 0) {
            sb.insert(0, BASE62_CHARS.charAt((int) (i % BASE62)));
            i /= BASE62;
        }
        return sb.toString();
    }

    public static long toBase10(String str) {
        BigInteger base10 = BigInteger.ZERO;
        for (int i = 0, len = str.length(); i < len; i++) {
            int charIndex = BASE62_CHARS.indexOf(str.charAt(i));
            base10 = base10.multiply(BigInteger.valueOf(BASE62)).add(BigInteger.valueOf(charIndex));
        }
        return base10.longValue();
    }

    public static String encodeBase62(long num) {
        return fromBase10(num);
    }

    public static long decodeBase62(String str) {
        return toBase10(str);
    }
}
