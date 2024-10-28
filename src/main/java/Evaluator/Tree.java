package Evaluator;

import java.io.Serializable;
//https://stackoverflow.com/questions/19330731/tree-implementation-in-java-root-parents-and-children
import java.util.ArrayList;
import java.util.List;

public class Tree<T> implements Serializable {
    private List<Tree<T>> children = new ArrayList<Tree<T>>();
    private Tree<T> parent = null;
    private T data = null;

    public Tree() {}

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

    public Tree<T> getParent() {
        return this.parent;
    }

    public void setParent(Tree<T> parent) {
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

    public void print() {
        print("", true);
    }

    private void print(String prefix, boolean isTail) {
        System.out.println(prefix + (isTail ? "└── " : "├── ") + data);
        List<Tree<T>> children = this.getChildren();
        for (int i = 0; i < children.size() - 1; i++) {
            children.get(i).print(prefix + (isTail ? "    " : "│   "), false);
        }
        if (!children.isEmpty()) {
            children.get(children.size() - 1).print(prefix + (isTail ? "    " : "│   "), true);
        }
    }

    private StringBuilder treeToString(StringBuilder prefix, boolean isTail) {
        StringBuilder sb = new StringBuilder();
        sb.append(prefix).append(isTail ? "└── " : "├── ").append(data).append("\n");
    
        List<Tree<T>> children = this.getChildren();
        for (int i = 0; i < children.size() - 1; i++) {
            sb.append(children.get(i).treeToString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), false));
        }
    
        if (!children.isEmpty()) {
            sb.append(children.get(children.size() - 1).treeToString(new StringBuilder().append(prefix).append(isTail ? "    " : "│   "), true));
        }
    
        return sb;
    }
    
    public String toString() {
        return treeToString(new StringBuilder(), true).toString();
    }
    
}

