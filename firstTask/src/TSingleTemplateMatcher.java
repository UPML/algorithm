import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 11.10.14.
 */
public class TSingleTemplateMatcher implements MetaTemplateMatcher {

    ArrayList<Integer> prefixFunctionForTemplate = new ArrayList<Integer>();
    String template = null;

    protected ArrayList<Integer> calculatePrefix(String s) {
        int n = (int) s.length();
        ArrayList<Integer> prefix = new ArrayList<Integer>();
        prefix.add(0);
        for (int i = 1; i < n; ++i) {
            int j = prefix.get(i - 1);
            while (j > 0 && s.charAt(i) != s.charAt(j))
                j = prefix.get(j - 1);
            if (s.charAt(i) == s.charAt(j)) ++j;
            prefix.add(i, j);
        }
        return prefix;
    }

    protected void calculatePrefixForAppend(String s, int alreadyCountPrefixFunction) {
        if (alreadyCountPrefixFunction == 0) {
            prefixFunctionForTemplate.add(0, 0);
        } else {
            int j = prefixFunctionForTemplate.get(alreadyCountPrefixFunction - 1);
            while (j > 0 && s.charAt(alreadyCountPrefixFunction) != s.charAt(j))
                j = prefixFunctionForTemplate.get(j - 1);
            if (s.charAt(alreadyCountPrefixFunction) == s.charAt(j)) ++j;
            if (prefixFunctionForTemplate.size() > alreadyCountPrefixFunction) {
                prefixFunctionForTemplate.set(alreadyCountPrefixFunction, j);
            } else {
                prefixFunctionForTemplate.add(alreadyCountPrefixFunction, j);
            }
        }
    }

    @Override
    public int AddTemplate(String templateTmp) {
        if (templateTmp == null || templateTmp.isEmpty()) {
            System.err.println("empty template");
            throw new ExceptionInInitializerError();
        }
        template = templateTmp + '$';
        prefixFunctionForTemplate = calculatePrefix(template);
        // write(prefixFunctionForTemplate);
        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        int alreadyRead = 0;
        int oldPrefixFunction = 0;
        while (!stream.IsEmpty()) {
            int currentPrefixFunction = oldPrefixFunction;
            char currentElement = stream.GetChar();
            while (currentPrefixFunction > 0 && currentElement != template.charAt(currentPrefixFunction)) {
                currentPrefixFunction = prefixFunctionForTemplate.get(currentPrefixFunction - 1);
            }
            if (currentElement == template.charAt(currentPrefixFunction) && currentPrefixFunction < template.length()) {
                currentPrefixFunction++;
            }
            oldPrefixFunction = currentPrefixFunction;
            if (currentPrefixFunction == template.length() - 1) {
                answer.add(new Pair<Integer, Integer>(0, alreadyRead));
            }
            ++alreadyRead;
        }
        //       writeAnswer(answer);
        return answer;
    }

    protected void writeAnswer(ArrayList<Pair<Integer, Integer>> answer) {
        for (int i = 0; i < answer.size(); ++i) {
            System.out.println(answer.get(i));
        }
    }

    protected void write(ArrayList<Integer> arrayList) {
        for (Integer i : arrayList) {
            System.out.print(i + " ");
        }
    }

    public void AppendCharToTemplate(char c) {
        if (c < 32 || c > 255) {
            throw new ExceptionInInitializerError();
        }
        if (template == null) {
            System.err.println("empty template");
            throw new ExceptionInInitializerError();
        }
        template.replace((char) 30, c);
        template += (char) 30;
        calculatePrefixForAppend(template, template.length() - 1);
    }

}
