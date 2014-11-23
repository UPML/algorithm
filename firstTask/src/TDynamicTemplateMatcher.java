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
        ++time;
        TStaticTemplateMatcher newStaticTemplateMatcher = new TStaticTemplateMatcher();
        newStaticTemplateMatcher.AddTemplate(template);
        insert(newStaticTemplateMatcher);
        return template.hashCode();
    }

    private void insert(TStaticTemplateMatcher newStaticTemplateMatcher) {
        int size = 0;
        while (used.size() > size && used.get(size)) {
            ++time;
            used.set(size, false);
            for (String s : staticTemplateMatchers.get(size).alreadyExist) {
                time -= newStaticTemplateMatcher.getTime();
                newStaticTemplateMatcher.AddTemplate(s);
                time += newStaticTemplateMatcher.getTime();
            }
            ++size;
        }
        if (size == used.size()) {
            ++time;
            used.add(true);
            staticTemplateMatchers.add(newStaticTemplateMatcher);
        } else {
            ++time;
            used.set(size, true);
            staticTemplateMatchers.set(size, newStaticTemplateMatcher);
        }
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        for (int i = 0; i < staticTemplateMatchers.size(); ++i) {
            ++time;
            if (used.get(i)) {
                time -= staticTemplateMatchers.get(i).getTime();
                staticTemplateMatchers.get(i).build();
                time += staticTemplateMatchers.get(i).getTime();
            }
        }
        while (!stream.IsEmpty()) {
            char currentElement = stream.GetChar();
            ++time;
            for (int i = 0; i < staticTemplateMatchers.size(); ++i) {
                ++time;
                if (used.get(i)) {
                    time -= staticTemplateMatchers.get(i).getTime();
                    staticTemplateMatchers.get(i).nextChar(currentElement);
                    time += staticTemplateMatchers.get(i).getTime();
                }
            }
        }
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < staticTemplateMatchers.size(); ++i) {
            ++time;
            if (used.get(i)) {
                answer.addAll(staticTemplateMatchers.get(i).getAnswer());
            }
        }
        time += answer.size();
        return answer;
    }

    private int time;

    public int getTime() {
        return time;
    }

    public void setTime(int timeTmp) {
        time = timeTmp;
    }
}
