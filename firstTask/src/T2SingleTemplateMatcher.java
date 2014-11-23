import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 16.11.14.
 */
public class T2SingleTemplateMatcher implements I2DTemplateMatcher {
    matrix template = null;

    @Override
    public void AddTemplate(matrix templateTmp) {
        template = templateTmp;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(matrix text) throws ReferenceNotInitializedException {
        if (template == null) {
            throw new ReferenceNotInitializedException();
        }
        return countAnswer(text);
    }

    private ArrayList<Pair<Integer, Integer>> countAnswer(matrix text) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < text.size - template.size; ++i) {
            for (int j = 0; j < text.size - template.size; ++j) {
                if (template.getHashForSubMatrix(0, 0, template.size - 1, template.size - 1) *
                        text.powPrimeForLine[i] * text.powPrimeForColumn[j] ==
                        text.getHashForSubMatrix(i, j, i + template.size - 1, j + template.size - 1)) {
                    answer.add(new Pair<Integer, Integer>(i, j));
                }
            }
        }
        return answer;
    }


}
