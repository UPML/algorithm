import org.junit.Test;

import javax.management.openmbean.KeyAlreadyExistsException;

/**
 * Created by kagudkov on 15.10.14.
 */

public class TestExceptions {



    @Test(expected = ExceptionInInitializerError.class)
    public void testExceptionAddEmptyToSingle(){
        TSingleTemplateMatcher single = new TSingleTemplateMatcher();
        single.AddTemplate("");
    }

    @Test(expected = NullPointerException.class)
    public void testExceptionAddEmptyToNaive(){
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate("");
    }

    @Test(expected = KeyAlreadyExistsException.class)
    public void testExceptionAlreadyExistNaive(){
        TNaiveTemplateMatcher naive = new TNaiveTemplateMatcher();
        naive.AddTemplate("a");
        naive.AddTemplate("a");
    }

}
