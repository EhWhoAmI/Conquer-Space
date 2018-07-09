package ConquerSpace.game.tech;

import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author Zyun
 */
public class TechonologiesTest {
    
    public TechonologiesTest() {
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
    public void testReadTech() {
        System.out.println("readTech");
        Techonologies.readTech();
        fail("The test case is a prototype.");
    }

    @Test
    public void testReadTechFromFile() throws Exception {
        System.out.println("readTechFromFile");
        File file = new File("assets/tech/techs/default.json");
        Techonologies.readTechFromFile(file);
        System.out.println(Techonologies.techonologies.size());
        Techonologies.techonologies.forEach((s) -> {
            System.out.println(s.getName());
        });
    }
    
    @Test
    public void testgetTechByTag() {
        System.out.println("getTechByTag");
        Techonology[] techsByTag = Techonologies.getTechsByTag("Starting");
        for(Techonology t : techsByTag) {
            System.out.println(t.getName());
        }
    }
}