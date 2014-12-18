import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Random;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Created by kagudkov on 16.11.14.
 */
public class TestT2WithoutHash {
    Random rand = new Random();


    @Test(expected = IllegalArgumentException.class)
    public void nullTemplateTest() throws ReferenceNotInitializedException {
        T2Matcher single = new T2Matcher();
        single.AddTemplate(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void emptyTemplateTest() throws ReferenceNotInitializedException {
        T2Matcher single = new T2Matcher();
        single.AddTemplate(randTable(0, 0, 0));
    }

    @Test
    public void simpleTest() throws ReferenceNotInitializedException {
        for (int i = 1; i < 100; ++i) {
            I2DTemplateMatcherWithoutHash.matrix tmpString = randTable(i, rand.nextInt(i * 2) + 1, i);
            for (int k = 0; k < 1; ++k) {
                for (int j = 1; j <= i; j += 5) {
//                    System.out.println(i + " " + j + " " + k);
                    I2DTemplateMatcherWithoutHash.matrix tmpTemplate = randTable(j, rand.nextInt(j * 2) + 1, k + j + i);
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

    public void maxTestTime() throws ReferenceNotInitializedException {
        int sizeForMatrix = 1000;
        int wideOfMatrix = rand.nextInt(sizeForMatrix * 2) + 1;
        I2DTemplateMatcherWithoutHash.matrix tmpString =
                randTable(sizeForMatrix, wideOfMatrix, sizeForMatrix);
        I2DTemplateMatcherWithoutHash.matrix tmpTemplate = randTable(20, rand.nextInt(20 * 2) + 1, 12);
        T2Matcher single = new T2Matcher();
        single.AddTemplate(tmpTemplate);
        int timeToAdd = single.getTime();
        single.MatchStream(tmpString);
        int timeToMatchStream = single.getTime() - timeToAdd;
        System.out.println("ADD " + timeToAdd);
        System.out.println("match " + timeToMatchStream);
        assertTrue(timeToAdd < 10 * 20 * 40);
        assertTrue(timeToMatchStream < 10 * (sizeForMatrix * wideOfMatrix));
    }


    private I2DTemplateMatcherWithoutHash.matrix randTable(int heightOfMatrix, int wideOfMatrix, int forRandom) {
        String[] arrayString = new String[heightOfMatrix];
        for (int i = 0; i < heightOfMatrix; ++i) {
            RandomStringStream stringStream = new RandomStringStream(10, wideOfMatrix, i * wideOfMatrix + forRandom);
            arrayString[i] = stringStream.getString();
        }
        return new I2DTemplateMatcherWithoutHash.matrix(arrayString, heightOfMatrix, wideOfMatrix);
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
