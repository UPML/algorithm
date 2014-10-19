/**
 * Created by kagudkov on 10.10.14.
 */
public class main {
    public static void main(String[] args) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate("a");
        naive.AddTemplate("abdsfsdf");
        naive.MatchStram(new RandomStream(1000000));
    }

}
