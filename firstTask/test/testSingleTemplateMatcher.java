import javafx.util.Pair;
import org.junit.Test;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by kagudkov on 12.10.14.
 */
public class testSingleTemplateMatcher {
    @Test
    public void simpleTest() {
        tested("a", "abacaba");
        tested("ba", "abacaba");
        tested("aba", "abacabadabacadaba");
    }

    @Test
    public void simpleRandomTest() {
        testedPrepend((new RandomStringStream(2, 3, 1)).getString(), new RandomStringStream(2, 0, 2).getString());
        testedPrepend((new RandomStringStream(2, 3, 1)).getString(), new RandomStringStream(2, 1, 2).getString());
        testedPrepend((new RandomStringStream(2, 3, 1)).getString(), new RandomStringStream(2, 2, 2).getString());
        testedPrepend((new RandomStringStream(2, 3, 1)).getString(), new RandomStringStream(2, 3, 2).getString());
        for (int i = 0; i < 10000; ++i) {
            tested((new RandomStringStream(2, 3, i)).getString(), new RandomStringStream(2, 100, i * 2).getString());
            testedPrepend((new RandomStringStream(2, 3, i)).getString(), new RandomStringStream(2, 100, i * 2).getString());
        }
    }

    private void testedPrepend(String template, String text) {
//        System.out.println(template);
//        System.out.println(text);
        TSingleTemplateMatcher singleTemplateMatcher = new TSingleTemplateMatcher();
        TSinglePrependTemplateMatcher tSinglePrependTemplateMatcher = new TSinglePrependTemplateMatcher();
        singleTemplateMatcher.AddTemplate(template);
        tSinglePrependTemplateMatcher.AddTemplate(template);
        checkAnswer(singleTemplateMatcher.MatchStream(new StringStream(text)), tSinglePrependTemplateMatcher.MatchStream(new StringStream(text)));
        singleTemplateMatcher.AddTemplate("a" + template);
        tSinglePrependTemplateMatcher.PrependCharToTemplate('a');
        checkAnswer(singleTemplateMatcher.MatchStream(new StringStream(text)), tSinglePrependTemplateMatcher.MatchStream(new StringStream(text)));
        singleTemplateMatcher.AddTemplate("ba" + template);
        tSinglePrependTemplateMatcher.PrependCharToTemplate('b');
        checkAnswer(singleTemplateMatcher.MatchStream(new StringStream(text)), tSinglePrependTemplateMatcher.MatchStream(new StringStream(text)));
        singleTemplateMatcher.AddTemplate("bbba" + template);
        tSinglePrependTemplateMatcher.PrependCharToTemplate('b');
        tSinglePrependTemplateMatcher.PrependCharToTemplate('b');
        checkAnswer(singleTemplateMatcher.MatchStream(new StringStream(text)), tSinglePrependTemplateMatcher.MatchStream(new StringStream(text)));
    }

    @Test
    public void largeRandomTest() {
        for (int i = 0; i < 10; ++i) {
            tested((new RandomStringStream(20, 30, i)).getString(), new RandomStringStream(20, 10000, i * i + 2).getString());
            testedAppend((new RandomStringStream(20, 30, i * 3)).getString(), new RandomStringStream(20, 10000, i * i * 21).getString());
        }
    }

    private void testedAppend(String template, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        naive.AddTemplate(template);
        single.AddTemplate(template);
        checkAnswer(naive.MatchStream(new StringStream(text)), single.MatchStream(new StringStream(text)));
        naive = new TNaiveTemplateMatcher();
        naive.AddTemplate(template + "a");
        single.AppendCharToTemplate('a');
        checkAnswer(naive.MatchStream(new StringStream(text)), single.MatchStream(new StringStream(text)));
    }

    private void tested(String template, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        naive.AddTemplate(template);
        single.AddTemplate(template);
        checkAnswer(naive.MatchStream(new StringStream(text)), single.MatchStream(new StringStream(text)));
    }

    private void checkAnswer(ArrayList<Pair<Integer, Integer>> naiveAnswer, ArrayList<Pair<Integer, Integer>> singleAnswer) {
        assertEquals(naiveAnswer.size(), singleAnswer.size());
        for (int i = 0; i < naiveAnswer.size(); ++i) {
            assertEquals(naiveAnswer.get(i), singleAnswer.get(i));
        }
    }

}
