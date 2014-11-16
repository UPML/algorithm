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
        template = c + template;
    }

    @Override
    public int AddTemplate(String templateTmp) {
        alreadyCountPrefixFunction = templateTmp.length() + 1;
        return super.AddTemplate(templateTmp);
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        int alreadyRead = 0;
        int oldPrefixFunction = 0;
        while (!stream.IsEmpty()) {
            if (alreadyCountPrefixFunction < template.length()) {
                calculatePrefixForAppend(template, alreadyCountPrefixFunction);
                ++alreadyCountPrefixFunction;
            }
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
}
