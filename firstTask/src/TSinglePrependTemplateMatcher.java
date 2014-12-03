import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 15.11.14.
 */
public class TSinglePrependTemplateMatcher extends TSingleTemplateMatcher {
    int alreadyCountPrefixFunction = 0;

    public void PrependCharToTemplate(char c) {
        if (template == null) {
            throw new ExceptionInInitializerError();
        }
        alreadyCountPrefixFunction = 0;
        template = template + c;
    }

    @Override
    public int AddTemplate(String templateTmp) {
        alreadyCountPrefixFunction = templateTmp.length() + 1;
        StringBuilder newTemplateTmp = new StringBuilder();
        for (int i = templateTmp.length() - 1; i >= 0; --i) {
            newTemplateTmp.append(templateTmp.charAt(i));
        }
        template = "$" + newTemplateTmp.toString();
        for (int i = 0; i < template.length(); ++i) {
            calculatePrefixForPrepend(template, i);
        }
        return templateTmp.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        int alreadyRead = 0;
        int oldPrefixFunction = 0;
        while (!stream.IsEmpty()) {
            if (alreadyCountPrefixFunction < template.length()) {
                calculatePrefixForPrepend(template, alreadyCountPrefixFunction);
                ++alreadyCountPrefixFunction;
            }
            int currentPrefixFunction = oldPrefixFunction;
            char currentElement = stream.GetChar();
            while (currentPrefixFunction > 0 && currentElement != template.charAt(inReverse(template, currentPrefixFunction))) {
                currentPrefixFunction = prefixFunctionForTemplate.get(currentPrefixFunction - 1);
            }
            if (currentElement == template.charAt(inReverse(template, currentPrefixFunction))
                    && currentPrefixFunction < template.length()) {
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

    private int inReverse(String s, int index) {
        return s.length() - index - 1;
    }

    protected void calculatePrefixForPrepend(String s, int alreadyCountPrefixFunction) {
        if (alreadyCountPrefixFunction == 0) {
            prefixFunctionForTemplate.add(0, 0);
        } else {
            int j = prefixFunctionForTemplate.get(alreadyCountPrefixFunction - 1);
            while (j > 0 && s.charAt(inReverse(s, alreadyCountPrefixFunction)) != s.charAt(inReverse(s, j)))
                j = prefixFunctionForTemplate.get(j - 1);
            if (s.charAt(inReverse(s, alreadyCountPrefixFunction)) == s.charAt(inReverse(s, j))) {
                ++j;
            }
            if (prefixFunctionForTemplate.size() > alreadyCountPrefixFunction) {
                prefixFunctionForTemplate.set(alreadyCountPrefixFunction, j);
            } else {
                prefixFunctionForTemplate.add(alreadyCountPrefixFunction, j);
            }
        }
    }
}
