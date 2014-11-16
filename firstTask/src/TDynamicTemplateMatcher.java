import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 16.11.14.
 */
public class TDynamicTemplateMatcher extends TStaticTemplateMatcher implements MetaTemplateMatcher {
    private ArrayList<Boolean> used = new ArrayList<Boolean>();
    private ArrayList<TStaticTemplateMatcher> staticTemplateMatchers = new ArrayList<TStaticTemplateMatcher>();

    @Override
    public int AddTemplate(String template) {
        TStaticTemplateMatcher newStaticTemplateMatcher = new TStaticTemplateMatcher();
        newStaticTemplateMatcher.AddTemplate(template);
        insert(newStaticTemplateMatcher);
        return template.hashCode();
    }

    private void insert(TStaticTemplateMatcher newStaticTemplateMatcher) {
        int size = 0;
        while (used.size() > size && used.get(size)) {
            used.set(size, false);
            for (String s : staticTemplateMatchers.get(size).alreadyExist) {
                newStaticTemplateMatcher.AddTemplate(s);
            }
            ++size;
        }
        if (size == used.size()) {
            used.add(true);
            staticTemplateMatchers.add(newStaticTemplateMatcher);
        } else {
            used.set(size, true);
            staticTemplateMatchers.set(size, newStaticTemplateMatcher);
        }
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        for (int i = 0; i < staticTemplateMatchers.size(); ++i) {
            if (used.get(i)) {
                staticTemplateMatchers.get(i).build();
            }
        }
        while (!stream.IsEmpty()) {
            char currentElement = stream.GetChar();
            for (int i = 0; i < staticTemplateMatchers.size(); ++i) {
                if (used.get(i)) {
                    staticTemplateMatchers.get(i).nextChar(currentElement);
                }
            }
        }
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < staticTemplateMatchers.size(); ++i) {
            if (used.get(i)) {
                answer.addAll(staticTemplateMatchers.get(i).getAnswer());
            }
        }
        return answer;
    }
}
