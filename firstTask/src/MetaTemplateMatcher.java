import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 10.10.14.
 */
public interface MetaTemplateMatcher {
    public int AddTemplate(String template);

    public ArrayList<Pair<Integer, Integer>> MatchStram(ICharStream stream);

}
