package UI;

import java.util.Collection;
import java.util.HashSet;

public class RunnableSet<T> extends HashSet<T> {
    Runnable toRunFunc;
    Runnable toSaveFunc;

    public void setToRunFunc(Runnable toRunFunc) {
        this.toRunFunc = toRunFunc;
    }

    public void setToSaveFunc(Runnable toSaveFunc) {
        this.toSaveFunc = toSaveFunc;
    }

    @Override
    public boolean add(T t) {
        boolean added = super.add(t);
        update();
        return added;
    }

    @Override
    public boolean remove(Object o) {
        boolean removed = super.remove(o);
        update();
        return removed;
    }

    @Override
    public boolean addAll(Collection<? extends T> c) {
        boolean added = super.addAll(c);
        update();
        return added;
    }

    private void update() {
        if (toRunFunc != null) {
            toRunFunc.run();
        }
        if (toSaveFunc != null) {
            toSaveFunc.run();
        }
    }
}

