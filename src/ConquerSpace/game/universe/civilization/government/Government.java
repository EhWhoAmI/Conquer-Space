package ConquerSpace.game.universe.civilization.government;

import ConquerSpace.game.people.Person;

/**
 *
 * @author zyunl
 */
public class Government {
    Person leader;
    String leaderTitle;

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
