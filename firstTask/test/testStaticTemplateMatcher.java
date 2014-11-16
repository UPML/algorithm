import javafx.util.Pair;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by kagudkov on 14.10.14.
 */
public class testStaticTemplateMatcher {
    @Test
    public void simpleTest() {
        tested(new String[]{"a"}, "abacaba");
        tested(new String[]{"ba"}, "abacaba");
        tested(new String[]{"aba"}, "abacabadabacadaba");
        tested(new String[]{"a!", "!"}, "a!");
    }

    @Test
    public void simpleTestWithSomeTemplate() {
        tested(new String[]{"a", "b", "ba"}, "abacaba");
        tested(new String[]{"ba"}, "abacaba");
        tested(new String[]{"aba", "ab", "abab", "bab"}, "abacabadabacadaba");
    }

    @Test
    public void simpleRandomTest() {
        for (int i = 0; i < 1000; ++i) {
            Map<String, Integer> alreadyExistTemplate = new HashMap<String, Integer>();
            ArrayList<String> templates = new ArrayList<String>();
            for (int j = 0; j < 10; ++j) {
                String s = new RandomStringStream(2, j + 10, i * 1000 + j).getString();
                if (!alreadyExistTemplate.containsKey(s)) {
                    templates.add(s);
                    alreadyExistTemplate.put(s, 1);
                }
            }
            testedList(templates, new RandomStringStream(2, 100, i).getString());
        }
    }

    @Test
    public void largeRandomTest() {
        for (int i = 0; i < 100; ++i) {
            Map<String, Integer> alreadyExistTemplate = new HashMap<String, Integer>();
            ArrayList<String> templates = new ArrayList<String>();
            for (int j = 0; j < 50; ++j) {
                String s = new RandomStringStream((i + j + 31) % 31 + 2, (i + j + 100) % 100 + 1, i * 1000 + j).getString();
                if (!alreadyExistTemplate.containsKey(s)) {
                    templates.add(s);
                    alreadyExistTemplate.put(s, 1);
                }
            }
            testedList(templates, new RandomStringStream(2, 10000, i).getString());
        }
    }

    private void testedList(ArrayList<String> templates, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TStaticTemplateMatcher staticTemplateMatcher = new TStaticTemplateMatcher();
        for (String s : templates) {
            naive.AddTemplate(s);
            staticTemplateMatcher.AddTemplate(s);
        }
        checkAnswer(naive.MatchStream(new StringStream(text)), staticTemplateMatcher.MatchStream(new StringStream(text)));

    }

    private void tested(String[] template, String text) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        TStaticTemplateMatcher staticTemplateMatcher = new TStaticTemplateMatcher();
        for (String s : template) {
            naive.AddTemplate(s);
            staticTemplateMatcher.AddTemplate(s);
        }
        checkAnswer(naive.MatchStream(new StringStream(text)), staticTemplateMatcher.MatchStream(new StringStream(text)));
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
