/**
 * Created by kagudkov on 10.10.14.
 */
public interface ICharStream {
    public char GetChar();
    //иногда нам очень хочется отправить копию в функцию
    public ICharStream clone(ICharStream f);

    public boolean IsEmpty();
}
