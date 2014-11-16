import com.sun.org.apache.xml.internal.security.signature.ReferenceNotInitializedException;
import javafx.util.Pair;

import java.util.ArrayList;

/**
 * Created by kagudkov on 16.11.14.
 */
public interface I2DTemplateMatcher {
    class matrix {
        private String[] strings;
        Integer size;
        private int primeForLine = 17971;
        private int primeForColumn = 10501;
        private Integer[][] hash = null;
        Integer[] powPrimeForLine = null;
        Integer[] powPrimeForColumn = null;

        public matrix(String[] stringsTmp, int sizeTmp) {
            size = sizeTmp;
            strings = stringsTmp;
            countHash();
        }

        private void countHash() {
            hash = new Integer[size][size];
            powPrimeForColumn = new Integer[size];
            powPrimeForLine = new Integer[size];
            powPrimeForLine[0] = 1;
            powPrimeForColumn[0] = 1;
            hash[0][0] = getInt(0, 0);
            for (int i = 1; i < size; ++i) {
                powPrimeForLine[i] = powPrimeForLine[i - 1] * primeForLine;
                powPrimeForColumn[i] = powPrimeForColumn[i - 1] * primeForColumn;
                hash[0][i] = hash[0][i - 1] + getInt(0, i) * powPrimeForColumn[i];
                hash[i][0] = hash[i - 1][0] + getInt(i, 0) * powPrimeForLine[i];
            }
            for (int i = 1; i < size; ++i) {
                for (int j = 1; j < size; ++j) {
                    hash[i][j] = getInt(i, j) * powPrimeForLine[i] * powPrimeForColumn[j] +
                            hash[i - 1][j] + hash[i][j - 1] - hash[i - 1][j - 1];
                }
            }

        }

        public char getChar(int stringNumber, int index) {
            return strings[stringNumber].charAt(index);
        }

        public int getInt(int stringNumber, int index) {
            return Integer.valueOf(getChar(stringNumber, index));
        }

        public int getHashForSubMatrix(int indexTopLine, int indexLeftColumn, int indexDownLine, int indexRightColumn) {
            int result = hash[indexDownLine][indexRightColumn];
            if (indexTopLine > 0) {
                result -= hash[indexTopLine - 1][indexRightColumn];
            }
            if (indexLeftColumn > 0) {
                result -= hash[indexDownLine][indexLeftColumn - 1];
            }
            if (indexLeftColumn > 0 && indexTopLine > 0) {
                result += hash[indexTopLine - 1][indexLeftColumn - 1];
            }
            return result;
        }

        public void write() {
//            for(int i = 0; i < size; ++i) {
//                System.out.println();
//                for(int j = 0; j < size; ++j) {
//                   System.out.print(getHashForSubMatrix(i, j, i, j) + " ");
//                }
//            }
            for(int i = 0; i < size; ++i) {
                System.out.println(strings[i]);
            }
            System.out.println();
            System.out.println();
        }
    }

    public void AddTemplate(matrix template);

    public ArrayList<Pair<Integer, Integer>> MatchStream(matrix text) throws ReferenceNotInitializedException;

}
