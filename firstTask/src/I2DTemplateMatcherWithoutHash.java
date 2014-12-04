import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 03.12.14.
 */
public interface I2DTemplateMatcherWithoutHash {

    class matrix {
        private String[] strings;
        private int height, weight;

        public matrix(String[] strings, int height, int weight) {
            this.strings = strings;
            this.height = height;
            this.weight = weight;
        }

        public String getString(int index) {
            return strings[index];
        }

        public int getHeight() {
            return height;
        }

        public int getWeight() {
            return weight;
        }

        public char getChar(int stringNumber, int index) {
            return strings[stringNumber].charAt(index);
        }

        public void write() {
            for (String string : strings) {
                System.out.println(string);
            }
            System.out.println();
            System.out.println();
        }

    }

    public void AddTemplate(matrix template);

    public ArrayList<Pair<Integer, Integer>> MatchStream(matrix text) throws ReferenceNotInitializedException;



}

