package ExpressionEvaluator;

//https://stackoverflow.com/questions/19330731/tree-implementation-in-java-root-parents-and-children
import java.util.ArrayList;
import java.util.List;

public class Tree<T> {
    private List<Tree<T>> children = new ArrayList<Tree<T>>();
    private Tree<T> parent = null;
    private T data = null;

    public Tree(T data) {
        this.data = data;
    }

    public Tree(T data, Tree<T> parent) {
        this.data = data;
        this.parent = parent;
    }

    public List<Tree<T>> getChildren() {
        return children;
    }

    public void setParent(Tree<T> parent) {
        parent.addChild(this);
        this.parent = parent;
    }

    public void addChild(T data) {
        Tree<T> child = new Tree<T>(data);
        child.setParent(this);
        this.children.add(child);
    }

    public void addChild(Tree<T> child) {
        child.setParent(this);
        this.children.add(child);
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isRoot() {
        return (this.parent == null);
    }

    public boolean isLeaf() {
        return this.children.size() == 0;
    }

    public void removeParent() {
        this.parent = null;
    }
}

