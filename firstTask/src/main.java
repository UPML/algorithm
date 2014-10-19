/**
 * Created by kagudkov on 10.10.14.
 */
public class main {
    public static void main(String[] args) {
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate("a");
        naive.AddTemplate("ab");
        naive.MatchStram(new StringStream("abacaba"));
    }

}
