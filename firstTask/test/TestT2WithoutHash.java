import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import static org.junit.Assert.assertEquals;

/**
 * Created by kagudkov on 16.11.14.
 */
public class TestT2WithoutHash {
    @Test
    public void simpleTest() throws ReferenceNotInitializedException {
        for (int i = 1; i < 30; ++i) {
            I2DTemplateMatcherWithoutHash.matrix tmpString = randTable(i, i);
            for (int k = 0; k < 1; ++k) {
                for (int j = 1; j <= i; ++j) {
//                    System.out.println(i + " " + j + " " + k);
                    I2DTemplateMatcherWithoutHash.matrix tmpTemplate = randTable(j, k + j + i);
//                    tmpString.write();
//                    tmpTemplate.write();
                    T2NaiveWithoutHash naive = new T2NaiveWithoutHash();
                    naive.AddTemplate(tmpTemplate);
                    T2Matcher single = new T2Matcher();
                    single.AddTemplate(tmpTemplate);
               /* ArrayList<Pair<Integer, Integer>> answerNaive = naive.MatchStream(tmpString);
                for(Pair<Integer, Integer> pair : answerNaive) {
                    System.out.println(pair.toString());
                }
                ArrayList<Pair<Integer, Integer>> answer = single.MatchStream(tmpString);

                for(Pair<Integer, Integer> pair : answer) {
                    System.out.println(pair.toString());
                }*/
                    checkAnswer(naive.MatchStream(tmpString), single.MatchStream(tmpString));
                }

            }
        }
    }

    private I2DTemplateMatcherWithoutHash.matrix randTable(int size, int forRandom) {
        String[] arrayString = new String[size];
        for (int i = 0; i < size; ++i) {
            RandomStringStream stringStream = new RandomStringStream(10, size, i * size + forRandom);
            arrayString[i] = stringStream.getString();
        }
        return new I2DTemplateMatcherWithoutHash.matrix(arrayString, arrayString.length, arrayString.length);
    }

    class pairCompare implements Comparator<Pair<Integer, Integer>> {
        @Override
        public int compare(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
            if (a.getKey() > b.getKey()) {
                return 1;
            }
            if (a.getKey() < b.getKey()) {
                return -1;
            }
            if (a.getValue() > b.getValue()) {
                return 1;
            }
            if (a.getValue() < b.getValue()) {
                return -1;
            }
            return 0;
        }
    }

    private void checkAnswer(ArrayList<Pair<Integer, Integer>> naiveAnswer, ArrayList<Pair<Integer, Integer>> staticAnswer) {
        Collections.sort(naiveAnswer, new pairCompare());
        Collections.sort(staticAnswer, new pairCompare());
        assertEquals(naiveAnswer.size(), staticAnswer.size());
        for (int i = 0; i < naiveAnswer.size(); ++i) {
            assertEquals(naiveAnswer.get(i), staticAnswer.get(i));
        }
    }
}
