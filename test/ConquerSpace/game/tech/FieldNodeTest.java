package ConquerSpace.game.tech;

import ConquerSpace.game.science.FieldNode;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author EhWhoAmI
 */
public class FieldNodeTest {

    FieldNode testCase;
    FieldNode child1;

    public FieldNodeTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
        testCase = new FieldNode("Test 1");
        child1 = new FieldNode("Child 1");
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testgetChildCount() {
        System.out.println("getChildCount");
        FieldNode instance = testCase;
        int expectedResult = 0;
        int result = instance.getChildCount();
        assertEquals(expectedResult, result);
        //fail("The test case is a prototype.");
        System.out.println("Success");
    }

    @Test
    public void testAddChild() {
        System.out.println("getChild");
        FieldNode n = child1;
        FieldNode instance = testCase;
        instance.addChild(n);
        int expectedResult = 1;
        int result = instance.getChildCount();
        assertEquals(expectedResult, result);
        System.out.println("Success - " + result);
    }

    @Test
    public void testHashCode() {
        System.out.println("hashCode");
        FieldNode instance = testCase;
        int expResult = "Test 1".hashCode();
        int result = instance.hashCode();
        assertEquals(expResult, result);
        System.out.println("Success");
    }

    @Test
    public void testGetParent() {
        System.out.println("getParent");
        FieldNode root = new FieldNode("Test");
        FieldNode instance = new FieldNode("Child");
        FieldNode expResult = root;
        root.addChild(instance);
        FieldNode result = instance.getParent();
        assertEquals(expResult, result);
        System.out.println("Success");
    }

    @Test
    public void testGetNode() {
        System.out.println("getNode");
        String name = "Child";
        FieldNode instance = new FieldNode("Test");
        
        FieldNode expResult = new FieldNode("Child");
        instance.addChild(expResult);
        FieldNode result = instance.getNode(name);
        System.out.println(result.getName());
        assertEquals(expResult, result);
        System.out.println("Success");
    }

}
