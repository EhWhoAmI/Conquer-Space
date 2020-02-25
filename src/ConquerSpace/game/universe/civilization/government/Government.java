package ConquerSpace.game.universe.civilization.government;

import ConquerSpace.game.people.Person;
import java.util.HashMap;

/**
 *
 * @author zyunl
 */
public class Government {
    public HashMap<GovernmentPosition, Person> officials;
    public PoliticalPowerSource politicalPowerSource;
    Person leader;
    String leaderTitle;
    
    //Important governemnt positions. All governments need these
    public GovernmentPosition headofState;
    public GovernmentPosition headofGovernment;

    public Government() {
        officials = new HashMap<>();
    }

    public Person getLeader() {
        return leader;
    }

    public void setLeader(Person leader) {
        this.leader = leader;
    }

    public String getLeaderTitle() {
        return leaderTitle;
    }

    public void setLeaderTitle(String leaderTitle) {
        this.leaderTitle = leaderTitle;
    }
}
