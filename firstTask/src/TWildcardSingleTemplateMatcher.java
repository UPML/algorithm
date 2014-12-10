import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 19.10.14.
 */
public class TWildcardSingleTemplateMatcher implements MetaTemplateMatcher {
    String templateNew;

    class oneSubstring {
        int start, end;
        int questionBefore;
        int questionAfter;

        oneSubstring(int startTmp, int endTmp, int questionBeforeTmp, int questionAfterTmp) {
            start = startTmp;
            end = endTmp;
            questionAfter = questionAfterTmp;
            questionBefore = questionBeforeTmp;
        }
    }

    ArrayList<oneSubstring> templates = new ArrayList<oneSubstring>();
    ArrayList<TSingleTemplateMatcher> matchers = new ArrayList<TSingleTemplateMatcher>();

    @Override
    public int AddTemplate(String template) {
        templateNew = template;
        templates.clear();
        int currentPosition = 0;
        while (template.length() > currentPosition && template.charAt(currentPosition) == '?') {
            ++currentPosition;
        }
        int questionBefore = currentPosition;
        int start = currentPosition;
        int end;
        for (; currentPosition < template.length(); ++currentPosition) {
            if (template.charAt(currentPosition) == '?') {
                int questionAfter = 0;
                end = currentPosition;
                while (template.length() > currentPosition && template.charAt(currentPosition) == '?') {
                    ++currentPosition;
                    ++questionAfter;
                }
                templates.add(new oneSubstring(start, end, questionBefore, questionAfter));
                start = currentPosition;
                questionBefore = questionAfter;
            }
        }
        if (template.charAt(template.length() - 1) != '?') {
            templates.add(new oneSubstring(start, template.length(), questionBefore, 0));
        }
        for (int i = 0; i < templates.size(); ++i) {
            matchers.add(new TSingleTemplateMatcher());
            matchers.get(i).AddTemplate(template.substring(templates.get(i).start, templates.get(i).end));
        }
        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        ArrayList<Pair<Integer, Integer>> answer = answer(stream);
        for (int i = 0; i < answer.size(); ++i) {
            answer.set(i, new Pair<Integer, Integer>(answer.get(i).getKey(), answer.get(i).getValue()
                    + templates.get(templates.size() - 1).questionAfter));
        }
        while (answer.size() > 0 && answer.get(0).getValue() < templateNew.length() - 1) {
            answer.remove(0);
        }
        while (answer.size() > 0 && answer.get(answer.size() - 1).getValue() >= inputSize) {
            answer.remove(answer.size() - 1);
            answer.remove(answer.size() - 1);
        }
        return answer;
    }

    private int inputSize;

    private ArrayList<Pair<Integer, Integer>> answer(ICharStream stream) {
        for (TSingleTemplateMatcher matcher : matchers) {
            matcher.beforeNewMatchStream();
        }
        while (!stream.IsEmpty()) {
            char nextChar = stream.GetChar();
            ++inputSize;
            for (TSingleTemplateMatcher matcher : matchers) {
                matcher.nextChar(nextChar);
            }
        }
        for (int i = 0; i < matchers.size(); ++i) {
            TSingleTemplateMatcher matcher = matchers.get(i);
            while (matcher.answer.size() > 0 &&
                    matcher.answer.get(matcher.answer.size() - 1).getValue()
                            + templates.get(i).questionAfter >= matcher.getAlreadyRead()) {
                matcher.answer.remove(matcher.answer.size() - 1);
            }
            while (matcher.answer.size() > 0 && matcher.answer.get(0).getValue() <
                    templates.get(i).questionBefore + templates.get(i).end - templates.get(i).start - 1) {
                matcher.answer.remove(0);
            }
        }
        ArrayList<Pair<Integer, Integer>> answer;
        answer = matchers.get(0).answer;
        for (int i = 1; i < matchers.size(); ++i) {
            answer = mergeAnswer(answer, matchers.get(i).answer, i);
            templates.set(i, new oneSubstring(templates.get(i - 1).start, templates.get(i).end,
                    templates.get(i - 1).questionBefore, templates.get(i).questionAfter));
        }
        return answer;

    }

    private ArrayList<Pair<Integer, Integer>> mergeAnswer(ArrayList<Pair<Integer, Integer>> answerFirst,
                                                          ArrayList<Pair<Integer, Integer>> answerSecond,
                                                          int numberOfSecondTemplates) {
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        int indexFromFirst = 0;
        int indexFromSecond = 0;
        for (; indexFromFirst < answerFirst.size(); ++indexFromFirst) {
            while (indexFromSecond < answerSecond.size() && answerFirst.get(indexFromFirst).getValue() +
                    (templates.get(numberOfSecondTemplates).end - templates.get(numberOfSecondTemplates).start
                            + templates.get(numberOfSecondTemplates).questionBefore)
                    > answerSecond.get(indexFromSecond).getValue()) {
                ++indexFromSecond;
            }
            if (indexFromSecond < answerSecond.size() && answerFirst.get(indexFromFirst).getValue() +
                    (templates.get(numberOfSecondTemplates).end - templates.get(numberOfSecondTemplates).start
                            + templates.get(numberOfSecondTemplates).questionBefore)
                    == answerSecond.get(indexFromSecond).getValue()) {
                answer.add(answerSecond.get(indexFromSecond));
            }
        }
        return answer;
    }
}
