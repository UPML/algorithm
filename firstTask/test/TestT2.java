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
public class TestT2 {
    @Test
    public void simpleTest() throws ReferenceNotInitializedException {
        for (int i = 1; i < 100; ++i) {
            I2DTemplateMatcher.matrix tmpString = randTable(i);
            for (int j = 1; j <= i; ++j) {
                I2DTemplateMatcher.matrix tmpTemplate = randTable(j);
//                tmpString.write();
//                tmpTemplate.write();
                T2Naive naive = new T2Naive();
                naive.AddTemplate(tmpTemplate);
                T2SingleTemplateMatcher single = new T2SingleTemplateMatcher();
                single.AddTemplate(tmpTemplate);
                checkAnswer(naive.MatchStream(tmpString), single.MatchStream(tmpString));
            }

        }
    }

    private I2DTemplateMatcher.matrix randTable(int size) {
        String[] arrayString = new String[size];
        for (int i = 0; i < size; ++i) {
            RandomStringStream stringStream = new RandomStringStream(10, size, i * size);
            arrayString[i] = stringStream.getString();
        }
        return new I2DTemplateMatcher.matrix(arrayString, arrayString.length);
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
