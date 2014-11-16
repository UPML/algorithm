import javafx.util.Pair;

import javax.management.openmbean.KeyAlreadyExistsException;
import java.util.ArrayList;

/**
 * Created by kagudkov on 12.10.14.
 */
public class TStaticTemplateMatcher implements MetaTemplateMatcher {

    private int numberOfTemplate = 0;
    ArrayList<String> alreadyExist = new ArrayList<String>();

    public TStaticTemplateMatcher() {
        init();
    }

    private class vertex {
        ArrayList<Integer> next = new ArrayList<Integer>();//[k]
        boolean leaf;
        int parent;
        char parenChar;
        int sufLink;
        int sufLinkAnswer = -1;
        ArrayList<Integer> go = new ArrayList<Integer>();
        public Integer numberOfTemplate = -1;

        public vertex(Integer parentTmp, Integer sufLinkTmp) {
            parent = parentTmp;
            sufLink = sufLinkTmp;
            sufLinkAnswer = -1;
            for (int i = 0; i < 255; ++i) {
                go.add(i, -1);
            }
            for (int i = 0; i < 255; ++i) {
                next.add(i, -1);
            }
        }
    }

    ArrayList<vertex> vertexes = new ArrayList<vertex>();
    int size;

    private void init() {
        vertexes.add(new vertex(-1, -1));
        size = 1;
    }


    private int get_link(int v) {
        if (vertexes.get(v).sufLink == -1)
            if (v == 0 || vertexes.get(v).parent == 0)
                vertexes.get(v).sufLink = 0;
            else {
                int nextStep = go(get_link(vertexes.get(v).parent), vertexes.get(v).parenChar);
                vertexes.get(v).sufLink = nextStep;
            }
        return vertexes.get(v).sufLink;
    }

    private int getAnswerLink(int v) {
        if (vertexes.get(v).leaf) {
            return v;
        }
        if (v == 0 || vertexes.get(v).parent == 0) {
            return 0;
        }
        return getAnswerLink(vertexes.get(v).sufLink);
    }

    private int go(int v, char c) {
        if (vertexes.get(v).go.get(c) == -1)
            if (vertexes.get(v).next.get(c) != -1)
                vertexes.get(v).go.set(c, vertexes.get(v).next.get(c));
            else if (v == 0) {
                vertexes.get(v).go.set(c, 0);
            } else {
                vertexes.get(v).go.set(c, go(get_link(v), c));
            }
        return vertexes.get(v).go.get(c);
    }

    @Override
    public int AddTemplate(String template) {
        if (alreadyExist.contains(template)) {
            throw new KeyAlreadyExistsException();
        }
        alreadyExist.add(template);
        if (template == null || template.isEmpty()) {
            throw new NullPointerException();
        }
        int v = 0;
        for (int i = 0; i < template.length(); ++i) {
            char c = template.charAt(i);// = s[i] - 'a';
            if (vertexes.get(v).next.get(c) == -1) {
                vertexes.add(new vertex(v, -1));

                vertexes.get(size).parenChar = c;
                vertexes.get(v).next.set(c, size++);
            }
            v = vertexes.get(v).next.get(c);
        }
        vertexes.get(v).numberOfTemplate = numberOfTemplate;
        vertexes.get(v).leaf = true;
        ++numberOfTemplate;
        return template.hashCode();
    }

    @Override
    public ArrayList<Pair<Integer, Integer>> MatchStream(ICharStream stream) {
        countAnswer(stream);
        return getAnswer();
    }
    private int currentVertex;
    private ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
    private int alreadyRead;

    private void countAnswer(ICharStream stream) {
        build();
        while (!stream.IsEmpty()) {
            nextChar(stream.GetChar());
        }
    }
    protected ArrayList<Pair<Integer, Integer>> getAnswer(){
        return answer;
    }

    protected void build() {
        int currentVertex = 0;
        ArrayList<Pair<Integer, Integer>> answer = new ArrayList<Pair<Integer, Integer>>();
        int alreadyRead = 0;
        for (int i = 1; i < size; ++i) {
            get_link(i);
        }
        for (int i = 1; i < size; ++i) {
            if (vertexes.get(i).sufLinkAnswer == -1) {
                vertexes.get(i).sufLinkAnswer = getAnswerLink(vertexes.get(i).sufLink);
            }
        }
    }

    protected void nextChar(char currentElement) {
        currentVertex = go(currentVertex, currentElement);
        if (currentVertex != -1) {
            int tmp = currentVertex;
            while (tmp != 0 && tmp != -1) {
                if (vertexes.get(tmp).leaf) {
                    answer.add(new Pair<Integer, Integer>(vertexes.get(tmp).numberOfTemplate, alreadyRead));
                }
                tmp = vertexes.get(tmp).sufLinkAnswer;
            }
        } else {
            currentVertex = 0;
        }
        ++alreadyRead;

    }


}
