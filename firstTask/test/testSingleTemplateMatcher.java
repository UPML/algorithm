import javafx.util.Pair;
import jdk.nashorn.internal.ir.annotations.Ignore;
import org.junit.Assert;
import org.junit.Test;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;
import java.util.Random;
import java.util.SortedMap;

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
    public void  simpleRandomTest(){
        for(int i = 0; i < 100; ++i){
            tested((new RandomStringStream(2, 3)).getString(), new RandomStringStream(2, 100).getString());
//            System.out.println(new RandomStringStream(10, 100).getString());
        }
    }

    @Ignore
    public void largeRandomTest(){
        for(int i = 0; i < 10; ++i){
            tested((new RandomStringStream(20, 30)).getString(), new RandomStringStream(20, 100000).getString());
            testedAppend((new RandomStringStream(20, 30)).getString(), new RandomStringStream(20, 100000).getString());
        }
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void testExceptionAppendToEmpty(){
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        single.AppendCharToTemplate('z');
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void testExceptionWrongAppend(){
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        single.AppendCharToTemplate((char) 20);
    }

    @Test(expected = ExceptionInInitializerError.class)
    public void testExceptionAddEmptyToSingle(){
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        single.AddTemplate("");
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionAddEmptyToNaive(){
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate("");
    }

    @Test(expected = KeyAlreadyExistsException.class)
    public void testExceptionAlreadyExistNaive(){
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate("a");
        naive.AddTemplate("a");
    }

    private void testedAppend(String template, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        naive.AddTemplate(template);
        single.AddTemplate(template);
        checkAnswer(naive.MatchStram(new StringStream(text)), single.MatchStram(new StringStream(text)));
        naive = new TNaiveTemplateMatcher();
        naive.AddTemplate(template + "a");
        single.AppendCharToTemplate('a');
        checkAnswer(naive.MatchStram(new StringStream(text)), single.MatchStram(new StringStream(text)));
    }

    private void tested(String template, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        naive.AddTemplate(template);
        single.AddTemplate(template);
        checkAnswer(naive.MatchStram(new StringStream(text)), single.MatchStram(new StringStream(text)));
    }

    private void checkAnswer(ArrayList<Pair<Integer, Integer>> naiveAnswer, ArrayList<Pair<Integer, Integer>> singleAnswer) {
        assertEquals(naiveAnswer.size(), singleAnswer.size());
        for(int i = 0; i < naiveAnswer.size(); ++i){
            assertEquals(naiveAnswer.get(i), singleAnswer.get(i));
        }
    }

}
