import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 19.10.14.
 */
public class TWildcardSingleTemplateMatcher extends TSingleTemplateMatcher {
    ArrayList<Integer> indexOfQuestion = new ArrayList<Integer>();

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        int alreadyRead = 0;
        int oldPrefixFunction = 0;
        StringBuilder currentString = new StringBuilder();
        while (!stream.IsEmpty()) {
            int currentPrefixFunction = oldPrefixFunction;
            char currentElement = stream.GetChar();
            if (currentString.length() == template.length() - 1) {
                currentString.deleteCharAt(0);
            }
            currentString.append(currentElement);
            boolean allBad = true;
            while (currentPrefixFunction > 0 && currentElement != template.charAt(currentPrefixFunction) && allBad
                    && template.charAt(currentPrefixFunction) != '?') {
                currentPrefixFunction = prefixFunctionForTemplate.get(currentPrefixFunction - 1);
                allBad = workWithQuestion(currentPrefixFunction, oldPrefixFunction, currentString);
            }
            if ((currentElement == template.charAt(currentPrefixFunction) || template.charAt(currentPrefixFunction) == '?')
                    && currentPrefixFunction < template.length()) {
                currentPrefixFunction++;
            }
            oldPrefixFunction = currentPrefixFunction;
            if (currentPrefixFunction == template.length() - 1) {
                answer.add(new Pair<Integer, Integer>(0, alreadyRead));
            }
            ++alreadyRead;
        }
        return answer;
    }

    @Override
    public int AddTemplate(String templateTmp) {
        for (Integer i = 0; i < templateTmp.length(); ++i) {
            if (templateTmp.charAt(i) == '?') {
                indexOfQuestion.add(i);
            }
        }
        return super.AddTemplate(templateTmp);
    }

    @Override
    protected ArrayList<Integer> calculatePrefix(String s) {
        int n = s.length();
        ArrayList<Integer> prefix = new ArrayList<Integer>();
        prefix.add(0);
        for (int i = 1; i < n; ++i) {
            int j = prefix.get(i - 1);
            while (j > 0 && s.charAt(i) != s.charAt(j) && s.charAt(i) != '?' && s.charAt(j) != '?')
                j = prefix.get(j - 1);
            if (s.charAt(i) == s.charAt(j) || s.charAt(i) == '?' || s.charAt(j) == '?') ++j;
            prefix.add(i, j);
        }
        return prefix;
    }

    private boolean workWithQuestion(int currentPrefixFunction, int oldPrefix, StringBuilder currentString) {
        for (int i = 0; i < indexOfQuestion.size() && i < currentPrefixFunction; ++i) {
            if (indexOfQuestion.get(i) > 0 &&
                    template.charAt(indexOfQuestion.get(i) + (oldPrefix - currentPrefixFunction)) !=
                            currentString.charAt(indexOfQuestion.get(i) - 1))
                return true;
        }
        return false;
    }

}
