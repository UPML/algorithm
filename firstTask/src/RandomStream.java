import sun.invoke.empty.Empty;

import java.security.PublicKey;
import java.util.EmptyStackException;
import java.util.Random;

/**
 * Created by kagudkov on 19.10.14.
 */
public class RandomStream implements ICharStream {

    private Integer length;
    public  RandomStream(Integer lengthTmp){
         length = lengthTmp;
    }
    @Override
    public char GetChar() {
        if(length < 0){
            throw new EmptyStackException();
        }
        length--;
        Random random = new Random();
        return (char)(random.nextInt(32) + 'a');
    }

    @Override
    public boolean IsEmpty() {
        return length == 0;
    }
}
