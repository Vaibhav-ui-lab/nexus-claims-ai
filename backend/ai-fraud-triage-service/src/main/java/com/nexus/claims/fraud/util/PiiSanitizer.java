package com.nexus.claims.fraud.util;

import java.util.regex.Pattern;

public class PiiSanitizer {
    private static final Pattern SSN_PATTERN = Pattern.compile("\\b\\d{3}-\\d{2}-\\d{4}\\b");
    private static final Pattern PHONE_PATTERN = Pattern.compile("\\b\\d{3}-\\d{3}-\\d{4}\\b");
    private static final Pattern EMAIL_PATTERN = Pattern.compile("[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}");

    public static String sanitize(String input) {
        if (input == null) return "";
        String clean = SSN_PATTERN.matcher(input).replaceAll("[REDACTED_SSN]");
        clean = PHONE_PATTERN.matcher(clean).replaceAll("[REDACTED_PHONE]");
        return EMAIL_PATTERN.matcher(clean).replaceAll("[REDACTED_EMAIL]");
    }
}
