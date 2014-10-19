import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 19.10.14.
 */
public class TWildcardSingleTemplateMatcher extends TSingleTemplateMatcher {
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
            if ((currentElement == template.charAt(currentPrefixFunction) && currentPrefixFunction < template.length()) || (currentElement == '?')) {
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

}
