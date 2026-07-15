package dev.jackraidenph;

import com.sun.security.auth.module.NTSystem;
import dev.jackraidenph.Constants.Salts;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class Utils {

    private static final MessageDigest SHA1;

    static {
        try {
            SHA1 = MessageDigest.getInstance("SHA-1");
        } catch (NoSuchAlgorithmException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    public static String GJP2(String plain) {
        if (plain == null || plain.isBlank()) {
            return "";
        }

        String salted = plain + Salts.GJP2;
        SHA1.reset();
        SHA1.update(salted.getBytes(StandardCharsets.UTF_8));

        StringBuilder hexString = new StringBuilder();
        for (byte b : SHA1.digest()) {
            hexString.append(String.format("%02x", b));
        }
        return hexString.toString();
    }

    //https://boomlings.dev/topics/encryption/id#udid
    private static String weirdRandomStr() {
        Random random = new Random();
        return String.valueOf(random.nextInt(100_000, 100_000_000));
    }

    public static String UDID() {
        try {
            return new NTSystem().getUserSID().replace("-", "");
        } catch (UnsatisfiedLinkError e) {
            return "S15" + weirdRandomStr() + weirdRandomStr() + weirdRandomStr() + weirdRandomStr();
        }
    }

    public static String acceptStringInput(Scanner scanner, String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    public static List<Map<Integer, String>> splitGDMultiResponse(String body) {
        if (body.indexOf('|') < 0) {
            return List.of(splitGDResponse(body));
        }

        List<Map<Integer, String>> responses = new ArrayList<>();
        for (String resp : body.split("\\|")) {
            responses.add(splitGDResponse(resp));
        }
        return responses;
    }

    public static Map<Integer, String> splitGDResponse(String body) {
        Map<Integer, String> res = new HashMap<>();

        StringBuilder builder = new StringBuilder();

        boolean nextKey = true;
        int key = -1;
        String value;
        for (char ch : body.toCharArray()) {
            if (ch != ':') {
                builder.append(ch);
                continue;
            }

            if (nextKey) {
                key = Integer.parseInt(builder.toString());
            } else {
                value = builder.toString();
                res.put(key, value);
            }

            nextKey = !nextKey;
            builder.setLength(0);
        }

        value = builder.toString();
        res.put(key, value);

        return res;
    }

    public static String mapToUrlEncoded(Map<String, Object> params) {
        return params.entrySet()
                .stream()
                .map(e -> e.getKey() + "=" + URLEncoder.encode(String.valueOf(e.getValue()), StandardCharsets.UTF_8))
                .collect(Collectors.joining("&"));
    }

    public static URI makeURI(String path) {
        return URI.create(Constants.GD_BASE_URL + path);
    }

    public static Integer parseIntSafe(String str) {
        try {
            return Integer.parseInt(str);
        } catch (NumberFormatException e) {
            if (str != null && !str.isBlank() && !str.equals("null")) {
                System.err.printf("Failed to parse [%s] into Integer%n", str);
            }
            return null;
        }
    }

    public static Boolean parseBooleanSafe(String str) {
        try {
            return Boolean.parseBoolean(str);
        } catch (NumberFormatException e) {
            if (str != null && !str.isBlank() && !str.equals("null")) {
                System.err.printf("Failed to parse [%s] into Boolean%n", str);
            }
            return null;
        }
    }
}
