/**
 * Created by Иван on 08.04.2017.
 */
public class test {

    private Integer i;

    public test() {
    }

    public test(Integer i) {
        this.i = i;
    }

    public Integer getI() {
        return i;
    }

    public void setI(Integer i) {
        this.i = i;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        test test = (test) o;

        return i != null ? i.equals(test.i) : test.i == null;

    }

    @Override
    public int hashCode() {
        return i != null ? i.hashCode() : 0;
    }
}
