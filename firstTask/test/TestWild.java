import javafx.util.Pair;
import org.junit.Test;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;

import static org.junit.Assert.*;

/**
 * Created by kagudkov on 19.10.14.
 */
public class TestWild {
    @Test
    public void simpleTest() {
        tested("a??", "abacaba");
        tested("b?a", "abacaba");
        tested("a??ba", "abacabadabacadaba");
    }

    @Test
    public void  simpleRandomTest(){
        for(int i = 0; i < 100; ++i){
            tested((new RandomStringStream(2, 3, i)).getString(), new RandomStringStream(2, 100, i * 2).getString());
       }
    }

    @Test
    public void largeRandomTest(){
        for(int i = 0; i < 10; ++i){
            tested((new RandomStringStream(20, 30, i)).getString(), new RandomStringStream(20, 10000, i * i + 2).getString());
        }
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
        for(int i = 0; i < naiveAnswer.size(); ++i){
            assertEquals(naiveAnswer.get(i), singleWildAnswer.get(i));
        }
    }
}
