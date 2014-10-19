/**
 * Created by kagudkov on 19.10.14.
 */
public class NaiveWild extends TNaiveTemplateMatcher {
    @Override
    public boolean MyEqualsForString(String template, String currentStringInStringBuilder) {
        if(template.length() != currentStringInStringBuilder.length()){
            return false;
        }
        for (int i = 0; i < template.length(); ++i){
            if(template.charAt(i) != '?' && template.charAt(i) != currentStringInStringBuilder.charAt(i)){
                return false;
            }
        }
        return true;
    }
}
