package springpart5.utils;

import java.util.Random;

public class RandomStringGenerator {
    private static final String SYMBOLS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefgijklmnopqrstuvwxyz1234567890 !,.?-";
    private static final int SYMBOLS_NUMBER = SYMBOLS.length();
    private static final Random rnd = new Random();

    public String generateRandomString() {
        int size = rnd.nextInt(50) + 1;
        char[] chars = new char[size];
        for (int i = 0; i < size; i++) {

            chars[i] = SYMBOLS.charAt(rnd.nextInt(SYMBOLS_NUMBER));
        }
        return new String(chars);
    }
}
