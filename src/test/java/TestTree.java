import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import Evaluator.Tree;

public class TestTree {

    private Tree<String> root;

    @Before
    public void setUp() {
        root = new Tree<>("Root");
        Tree<String> child1 = new Tree<>("Child 1");
        Tree<String> child2 = new Tree<>("Child 2");
        Tree<String> subChild1 = new Tree<>("Subchild 1");

        root.addChild(child1);
        root.addChild(child2);
        child2.addChild(subChild1);
    }

    @Test
    public void testIsRoot() {
        assertTrue(root.isRoot());
        assertFalse(root.getChildren().get(0).isRoot());
    }

    @Test
    public void testIsLeaf() {
        assertFalse(root.isLeaf());
        assertTrue(root.getChildren().get(0).isLeaf());
        assertFalse(root.getChildren().get(1).isLeaf());
        assertTrue(root.getChildren().get(1).getChildren().get(0).isLeaf());
    }

    @Test
    public void testAddChildWithData() {
        Tree<String> newChild = new Tree<>("New Child");
        root.addChild(newChild);
        assertEquals(newChild, root.getChildren().get(root.getChildren().size() - 1));
        assertEquals(root, newChild.getParent());
    }

    @Test
    public void testRemoveParent() {
        Tree<String> child = root.getChildren().get(0);
        child.removeParent();
        assertNull(child.getParent());
    }

    @Test
    public void testToString() {
        String expected =
        """
        └── Root 
            ├── Child 1
            └── Child 2
                └── Subchild 1
        """;
        assertEquals(expected, root.toString());
    }
}

