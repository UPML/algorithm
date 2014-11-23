import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 16.11.14.
 */
public class T2Naive implements I2DTemplateMatcher {
    private matrix template;

    @Override
    public void AddTemplate(matrix templateTmp) {
        template = templateTmp;
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(matrix text) throws ReferenceNotInitializedException {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        for (int i = 0; i < text.size - template.size; ++i) {
            for (int j = 0; j < text.size - template.size; ++j) {
                int k = template.size - 1;
                if (compare(0, 0, k, k, i, j, i + k, j + k, text)) {
                    answer.add(new Pair<Integer, Integer>(i, j));
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
