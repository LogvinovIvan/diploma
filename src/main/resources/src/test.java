package by.bsuir.fksis.poit.obfuscator;

/**
 * Created by Ivan on 08.04.2017.
 */
public class test {




    // test integer


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

    private Integer i;

    @Override
    public int hashCode() {
        return i != null ? i.hashCode() : 0;
    }





}


class A{
    @Override
    protected Object clone() throws CloneNotSupportedException {
        return super.clone();
    }
}


