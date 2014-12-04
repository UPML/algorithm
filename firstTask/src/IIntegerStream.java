import java.util.ArrayList;

/**
 * Created by kagudkov on 04.12.14.
 */
public class IIntegerStream {
    private ArrayList<Integer> arrayList = new ArrayList<Integer>();
    private int current;
    IIntegerStream(ArrayList<Integer> arrayList) {
        this.arrayList = arrayList;
        current = 0;
    }
    public boolean IsEmpty() {
        return current == arrayList.size();
    }

    public int GetInt() {
        current++;
        return arrayList.get(current - 1);
    }
}
