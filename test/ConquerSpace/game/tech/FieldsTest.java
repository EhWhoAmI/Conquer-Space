package ConquerSpace.game.tech;

import ConquerSpace.game.science.FieldNode;
import ConquerSpace.game.science.Fields;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author Zyun
 */
public class FieldsTest {

    public FieldsTest() {
    }

    @BeforeClass
    public static void setUpClass() {
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    @Test
    public void testReadFields() {
        System.out.println("readFields");
        Fields.readFields();
        for (int i = 0; i < Fields.fieldNodeRoot.getChildCount(); i++) {
            FieldNode n = Fields.fieldNodeRoot.getNode(i);
            System.out.println(n.getName());
            for (int s = 0; s < n.getChildCount(); s++) {
                FieldNode node = n.getNode(s);
                System.out.println("-" + node.getName());
            }
        }
        //fail("The test case is a prototype.");
    }

}
