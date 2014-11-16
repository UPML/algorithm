import javafx.util.Pair;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;
/**
 * Created by kagudkov on 16.11.14.
 */
public class TestDynamicTemplateMatcher {
    Map<String, Integer> alreadyExistTemplate = new HashMap<String, Integer>();
    TDynamicTemplateMatcher tDynamicTemplateMatcher = new TDynamicTemplateMatcher();
    ArrayList<String> allTemplates = new ArrayList<String>();
    @Test
    public void simpleRandomTest() {
        for (int i = 0; i < 100; ++i) {
            ArrayList<String> templates = new ArrayList<String>();
            for (int j = 0; j < 10; ++j) {
                String s = new RandomStringStream(5, j + 10, i*1000 + j).getString();
                if(!alreadyExistTemplate.containsKey(s)){
                    templates.add(s);
                    allTemplates.add(s);
                    alreadyExistTemplate.put(s, 1);
                }
            }
            testedList(templates, new RandomStringStream(2, 1000, i).getString());
        }
    }
    private void testedList(ArrayList<String> templates, String text) {
        TStaticTemplateMatcher staticTemplateMatcher = new TStaticTemplateMatcher();
        for (String s : templates) {
            tDynamicTemplateMatcher.AddTemplate(s);
        }
        for (String s : allTemplates) {
            staticTemplateMatcher.AddTemplate(s);
        }

        checkAnswer(tDynamicTemplateMatcher.MatchStream(new StringStream(text)), staticTemplateMatcher.MatchStream(new StringStream(text)));

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
