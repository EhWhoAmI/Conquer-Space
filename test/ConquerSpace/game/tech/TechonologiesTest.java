package ConquerSpace.game.tech;

import ConquerSpace.game.science.tech.Technologies;
import java.io.File;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author EhWhoAmI
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
        Technologies.readTech();
    }

    @Test
    public void testReadTechFromFile() throws Exception {
        System.out.println("readTechFromFile");
        File file = new File("assets/tech/techs/default.json");
        //Technologies.readTechFromFile(file);
//        System.out.println(Technologies.techonologies.size());
//        Technologies.techonologies.forEach((s) -> {
//            System.out.println(s.getName());
//        });
    }
    
    @Test
    public void testgetTechByTag() {
        System.out.println("getTechByTag");
//        Technology[] techsByTag = Technologies.getTechsByTag("Starting");
//        for(Technology t : techsByTag) {
//            System.out.println(t.getName());
//        }
    }
    
    @Test
    public void testtechEquals() {
        System.out.println("testtechEquals");
        Technologies.readTech();
        //System.out.println(Technologies.techonologies.get(0));
//        if (Technologies.getTechByName("rocketery") == Technologies.getTechByName("rocketery")) {
//            System.out.println("Is equal");
//        } else {
//            System.out.println("Not equal");
//        }
        
    }
}