import javafx.util.Pair;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Random;

import static org.junit.Assert.assertEquals;

/**
 * Created by kagudkov on 19.10.14.
 */
public class TestWild {
    @Test
    public void simpleTest() {
        tested("a??", "abacaba");
        tested("?a", "abacaba");
        tested("a??ba", "abacabadabacadaba");
    }

    @Test
    public void simpleRandomTest() {
        for (int i = 0; i < 100; ++i) {
            testedQuestion((new RandomStringStream(2, 3, i)).getString(), new RandomStringStream(2, 5, i * 2).getString());
        }
    }

    @Test
    public void largeRandomTest() {
        for (int i = 0; i < 100; ++i) {
            testedQuestion((new RandomStringStream(20, 30, i)).getString(), new RandomStringStream(20, 10000, i * i + 2).getString());
        }
    }

    private void testedQuestion(String template, String text) {
        int numberOfQuestion = (new Random()).nextInt(template.length());
        StringBuilder newString = new StringBuilder(template);
        for (int i = 0; i < numberOfQuestion; ++i) {
            newString.insert((new Random()).nextInt(newString.length()), '?');
        }
        tested(newString.toString(), text);
    }

    private void tested(String template, String text) {
        NaiveWild naive = new NaiveWild();
        TWildcardSingleTemplateMatcher wildcardSingleTemplateMatcher = new TWildcardSingleTemplateMatcher();
        naive.AddTemplate(template);
        wildcardSingleTemplateMatcher.AddTemplate(template);
        checkAnswer(naive.MatchStream(new StringStream(text)), wildcardSingleTemplateMatcher.MatchStream(new StringStream(text)));
    }

    private void checkAnswer(ArrayList<Pair<Integer, Integer>> naiveAnswer, ArrayList<Pair<Integer, Integer>> singleWildAnswer) {
        assertEquals(naiveAnswer.size(), singleWildAnswer.size());
        for (int i = 0; i < naiveAnswer.size(); ++i) {
            assertEquals(naiveAnswer.get(i), singleWildAnswer.get(i));
        }
    }
}
