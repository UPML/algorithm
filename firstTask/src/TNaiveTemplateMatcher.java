import javafx.util.Pair;

import javax.management.openmbean.KeyAlreadyExistsException;
import javax.print.attribute.standard.Copies;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kagudkov on 10.10.14.
 */
public class TNaiveTemplateMatcher implements MetaTemplateMatcher {

    Map<Integer, String> currentTemplate = new HashMap<Integer, String>();
    Map<String, Integer> currentNumbers = new HashMap<String, Integer>();

    @Override
    public int AddTemplate(String template) {
        if (template == null || template.isEmpty()) {
            throw new NullPointerException();
        }

        if (currentTemplate.containsKey(template.hashCode())) {
            throw new KeyAlreadyExistsException();
        } else {
            currentTemplate.put(template.hashCode(), template);
            currentNumbers.put(template, currentNumbers.size());
        }

        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStram(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        Collection<String> list = currentTemplate.values();
        for (String s : list) {
            search(stream.clone(stream), s, answer);
        }
//        write(answer);
        return answer;
    }

    private void search(ICharStream stream, final String s, ArrayList<Pair<Integer, Integer>> answer) {
        StringBuilder builder = new StringBuilder();
        int index = 0;

        while (index < s.length() && !stream.IsEmpty()) {
            ++index;
            builder.append(stream.GetChar());
        }

        while (!stream.IsEmpty()) {
            if (s.equals(builder.toString())) {
                answer.add(new Pair<Integer, Integer>(currentNumbers.get(s), index - 1));
            }
            builder.deleteCharAt(0);
            builder.append(stream.GetChar());
            ++index;
        }

        if (s.equals(builder.toString())) {
            answer.add(new Pair<Integer, Integer>(currentNumbers.get(s), index - 1));
        }

    }

    private void write(ArrayList<Pair<Integer, Integer>> answer) {
        for (int i = 0; i < answer.size(); ++i) {
            System.out.println(answer.get(i));
        }
    }
}
