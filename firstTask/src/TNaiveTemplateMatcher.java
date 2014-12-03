import javafx.util.Pair;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;

/**
 * Created by kagudkov on 10.10.14.
 */

public class TNaiveTemplateMatcher implements MetaTemplateMatcher {

    ArrayList<String> templates = new ArrayList<String>();

    @Override
    public int AddTemplate(String template) {
        if (template == null || template.isEmpty()) {
            throw new NullPointerException();
        }

        if (templates.contains(template)) {
            throw new KeyAlreadyExistsException();
        } else {
            templates.add(template);
        }

        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        if (templates.size() != 0) {
            search(stream, templates, answer);
        }
//        write(answer);
        return answer;
    }

    private void search(ICharStream stream, final ArrayList<String> arrayTemplates, ArrayList<Pair<Integer, Integer>> answer) {
        StringBuilder[] builderForTemplatesCollections = new StringBuilder[arrayTemplates.size()];
        int alreadyReaded = 0;
        while (!stream.IsEmpty()) {
            char nextCharFromStream = stream.GetChar();
            ++alreadyReaded;
            for (int i = 0; i < builderForTemplatesCollections.length; ++i) {
                if (builderForTemplatesCollections[i] == null) {
                    builderForTemplatesCollections[i] = new StringBuilder();
                }
                if (builderForTemplatesCollections[i].length() == arrayTemplates.get(i).length()) {
                    builderForTemplatesCollections[i].deleteCharAt(0);
                }
                builderForTemplatesCollections[i].append(nextCharFromStream);

                String currentStringInStringBuilder = builderForTemplatesCollections[i].toString();

                if (MyEqualsForString(arrayTemplates.get(i), currentStringInStringBuilder)) {
                    answer.add(new Pair<Integer, Integer>(i, alreadyReaded - 1));
                }

            }
        }
    }

    public boolean MyEqualsForString(String template, String currentStringInStringBuilder) {
        return template.equals(currentStringInStringBuilder);
    }

    private void write(ArrayList<Pair<Integer, Integer>> answer) {
        for (Pair<Integer, Integer> anAnswer : answer) {
            System.out.println(anAnswer);
        }
    }
}
