import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 03.12.14.
 */
public class T2Matcher implements I2DTemplateMatcherWithoutHash {
    matrix template;
    ArrayList<Integer> templateForColumn = new ArrayList<Integer>();
    TStaticTemplateMatcher staticTemplateMatcher = new TStaticTemplateMatcher();

    @Override
    public void AddTemplate(matrix template) {
        this.template = template;
        bor myBor = new bor();
        for (int i = 0; i < template.getHeight(); ++i) {
            int number = myBor.addString(template.getString(i), i);
            if (number == i) {
                staticTemplateMatcher.AddTemplate(template.getString(i));
            }
            if (i == 0) {
                templateForColumn.clear();
                templateForColumn.add(0);
            } else {
                templateForColumn.add(number);
            }
        }
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(matrix text) throws ReferenceNotInitializedException {
        int[][] answerFromStatic = new int[text.getHeight()][text.getWeight()];
    /*    for (int[] i : answerFromStatic) {
            for (int j : i) {
                j = -1;
            }
        }
      */
        for (int i = 0; i < text.getHeight(); ++i) {
            for (int j = 0; j < text.getWeight(); ++j) {
                answerFromStatic[i][j] = -1;
            }
        }
        for (int i = 0; i < text.getHeight(); ++i) {
            ArrayList<Pair<Integer, Integer>> needToPaint = staticTemplateMatcher.MatchStream(new StringStream(text.getString(i)));
            for (Pair<Integer, Integer> pair : needToPaint) {
                answerFromStatic[i][pair.getValue()] = pair.getKey();
            }
        }
        TSingleTemplateMatcherForInteger singleTemplateMatcher = new TSingleTemplateMatcherForInteger();
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        singleTemplateMatcher.AddTemplate(templateForColumn);
        for (int i = template.getWeight() - 1; i < text.getWeight(); ++i) {
            ArrayList<Integer> column = new ArrayList<Integer>();
            for (int j = 0; j < text.getHeight(); ++j) {
                column.add(answerFromStatic[j][i]);
            }
            ArrayList<Pair<Integer, Integer>> answerSingle = singleTemplateMatcher.MatchStream(new IIntegerStream(column));
            for (Pair<Integer, Integer> pair : answerSingle) {
                answer.add(new Pair<Integer, Integer>(pair.getValue(), i));
            }
        }
        return answer;
    }

    class node {
        node[] next = new node[50];
        int thereEndTemplateNumberOf = -1;
    }

    class bor {
        node head;

        bor() {
            head = new node();
        }

        int addString(String s, int numberOfTemplate) {
            node current = head;
            for (int i = 0; i < s.length(); ++i) {
                if (current.next[s.charAt(i) - 'a'] != null) {
                    current = current.next[s.charAt(i) - 'a'];
                } else {
                    node newNode = new node();
                    current.next[s.charAt(i) - 'a'] = newNode;
                    current = newNode;
                }

            }
            if (current.thereEndTemplateNumberOf == -1) {
                current.thereEndTemplateNumberOf = numberOfTemplate;
            }
            return current.thereEndTemplateNumberOf;
        }
    }

}
