import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 11.10.14.
 */
public class TSingleTemplateMatcher implements MetaTemplateMatcher {

    ArrayList<Integer> prefixFunctionForTemplate = new ArrayList<Integer>();
    String template = null;

    private ArrayList<Integer> calculatePrefix(String s) {
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

    private void calculatePrefixForAppend(String s) {
        int n = (int) s.length();
        for (int i = n - 2; i < n; ++i) {
            int j = prefixFunctionForTemplate.get(i - 1);
            while (j > 0 && s.charAt(i) != s.charAt(j))
                j = prefixFunctionForTemplate.get(j - 1);
            if (s.charAt(i) == s.charAt(j)) ++j;
            prefixFunctionForTemplate.add(i, j);
        }
    }

    @Override
    public int AddTemplate(String templateTmp) {
        if (templateTmp == null || templateTmp.isEmpty()) {
            System.err.println("empty template");
            throw new ExceptionInInitializerError();
        }
        template = templateTmp + (char) (30);
        prefixFunctionForTemplate = calculatePrefix(template);
        // write(prefixFunctionForTemplate);
        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStram(ICharStream stream) {
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

    private void writeAnswer(ArrayList<Pair<Integer, Integer>> answer) {
        for (int i = 0; i < answer.size(); ++i) {
            System.out.println(answer.get(i));
        }
    }

    private void write(ArrayList<Integer> arrayList) {
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
        calculatePrefixForAppend(template);
    }

}
