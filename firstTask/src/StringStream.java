import java.rmi.UnexpectedException;
import java.security.cert.Extension;
import java.util.NoSuchElementException;

/**
 * Created by kagudkov on 10.10.14.
 */
public class StringStream implements ICharStream {
    private int currentIndex;
    private String currentString;

    public StringStream(String s) {
        currentIndex = 0;
        currentString = s;
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
    public ICharStream clone(ICharStream f) {
        return new StringStream(currentString);
    }

    @Override
    public boolean IsEmpty() {
        return currentIndex >= currentString.length();
    }
}