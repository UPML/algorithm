import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 04.12.14.
 */
public class TSingleTemplateMatcherForInteger {
    ArrayList<Integer> prefixFunctionForTemplate = new ArrayList<Integer>();
    ArrayList<Integer> template = null;

    protected ArrayList<Integer> calculatePrefix(ArrayList<Integer> s) {
        int n = s.size();
        ArrayList<Integer> prefix = new ArrayList<Integer>();
        prefix.add(0);
        for (int i = 1; i < n; ++i) {
            int j = prefix.get(i - 1);
            while (j > 0 && !s.get(i).equals(s.get(j)))
                j = prefix.get(j - 1);
            if (s.get(i).equals(s.get(j))) ++j;
            prefix.add(i, j);
        }
        return prefix;
    }


    public int AddTemplate(ArrayList<Integer> templateTmp) {
        if (templateTmp == null || templateTmp.isEmpty()) {
            System.err.println("empty template");
            throw new ExceptionInInitializerError();
        }
        template = templateTmp;
        template.add(-2);
        prefixFunctionForTemplate = calculatePrefix(template);
        // write(prefixFunctionForTemplate);
        return template.hashCode();
    }

    ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
    private int alreadyRead;
    private int oldPrefixFunction;

    public ArrayList<Pair<Integer, Integer>> MatchStream(IIntegerStream stream) {
        beforeNewMatchStream();
        while (!stream.IsEmpty()) {
            int currentElement = stream.GetInt();
            nextChar(currentElement);
        }
        return answer;
    }

    public void nextChar(int currentElement) {
        int currentPrefixFunction = oldPrefixFunction;
        while (currentPrefixFunction > 0 && currentElement != template.get(currentPrefixFunction)) {
            currentPrefixFunction = prefixFunctionForTemplate.get(currentPrefixFunction - 1);
        }
        if (currentElement == template.get(currentPrefixFunction) && currentPrefixFunction < template.size()) {
            currentPrefixFunction++;
        }
        oldPrefixFunction = currentPrefixFunction;
        if (currentPrefixFunction == template.size() - 1) {
            answer.add(new Pair<Integer, Integer>(0, alreadyRead));
        }
        ++alreadyRead;
    }

    public void beforeNewMatchStream() {
        answer.clear();
        alreadyRead = 0;
        oldPrefixFunction = 0;
    }


}


