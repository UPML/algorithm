import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 16.11.14.
 */
public class T2NaiveWithoutHash implements I2DTemplateMatcherWithoutHash {
    private matrix template;

    @Override
    public void AddTemplate(matrix templateTmp) {
        template = templateTmp;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(matrix text) throws ReferenceNotInitializedException {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i <= text.getHeight() - template.getHeight(); ++i) {
            for (int j = 0; j <= text.getWeight() - template.getWeight(); ++j) {
                int height = template.getHeight() - 1;
                int weight = template.getWeight() - 1;
                if (compare(0, 0, height, weight, i, j, i + height, j + weight, text)) {
                    answer.add(new Pair<Integer, Integer>(i + height, j + weight ));
                }
            }
        }
        return answer;
    }

    private boolean compare(int y1, int x1, int y2, int x2, int y3, int x3, int y4, int x4, matrix text) {
        for (int i = y1; i <= y2; ++i) {
            for (int j = x1; j <= x2; ++j) {
                if (template.getChar(i, j) != text.getChar(y3 + i, x3 + j)) {
                    return false;
                }
            }
        }
        return true;
    }
}
