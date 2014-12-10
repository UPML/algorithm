import java.util.NoSuchElementException;
import java.util.Random;

/**
 * Created by kagudkov on 10.10.14.
 */
public class RandomStringStream implements ICharStream {
    private int currentIndex;
    private String currentString;

    public String getString() {
        return currentString;
    }

    public RandomStringStream(Integer numberOfLetter, Integer length, Integer parameterForRandom) {
        currentIndex = 0;
        currentString = RandomString(numberOfLetter, length, parameterForRandom);
    }

    private String RandomString(Integer numberOfLetter, Integer length, Integer parameterForRandom) {
        String s = "";
        if (numberOfLetter > 255 - 31) {
            System.err.println("numberOfLetter is excessive");
            throw new ExceptionInInitializerError();
        }
        Random random = new Random(parameterForRandom);
        for (int i = 0; i < length; ++i) {
            int p = Math.abs(random.nextInt(numberOfLetter)) + 'a';///+ 32
            s += (char) p;
        }
        return s;
    }

    @Override
    public char GetChar() {
        if (currentIndex < currentString.length()) {
            if (currentString.charAt(currentIndex) > 255 || currentString.charAt(currentIndex) < 32) {
                throw new ExceptionInInitializerError();
            }
            currentIndex++;
            return currentString.charAt(currentIndex - 1);
        } else {
            throw new NoSuchElementException();
        }

    }

    @Override
    public boolean IsEmpty() {
        return currentIndex >= currentString.length();
    }
}
