/**
 * Created by kagudkov on 10.10.14.
 */
public class main {
    public static void mainMethod(String[] args) {
        TSinglePrependTemplateMatcher prependTemplateMatcher = new TSinglePrependTemplateMatcher();
        prependTemplateMatcher.AddTemplate("bba");
        prependTemplateMatcher.MatchStream(new StringStream("bbabb"));
        prependTemplateMatcher.PrependCharToTemplate('a');
        prependTemplateMatcher.MatchStream(new StringStream("bbabb"));
        prependTemplateMatcher.PrependCharToTemplate('b');
        prependTemplateMatcher.MatchStream(new StringStream("bbabb"));
    }

}
