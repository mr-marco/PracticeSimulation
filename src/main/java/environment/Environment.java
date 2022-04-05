package environment;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Context;
import nl.uu.cs.iss.ga.sim2apl.core.deliberation.DeliberationResult;
import nl.uu.cs.iss.ga.sim2apl.core.tick.TickHookProcessor;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Environment implements Context, TickHookProcessor<String> {
    public List<List<AgentID>> envGrid;

    /**
     * Initialise the grid cells
     */
    public void initGrid(){
        this.envGrid = new ArrayList<>();

        for (int i = 0; i < 10; i++)  {
            this.envGrid.add(new ArrayList<AgentID>(Collections.nCopies(10, null)));
        }
    }

    public void processAction(List<DeliberationResult<String>> agentActions){
        for(DeliberationResult<String> agentAction: agentActions) {
            List<String> actions = agentAction.getActions();
            System.out.println(actions);
            /*
            for (String action: actions) {
                switch (action){
                    case "left":
                        envGrid
                    case "left":

                    case "up":

                    case "down":

                    default:
                        break;
                }
            }

             */
        }
    }

    /**
     * @param agentID: target agent to retrieve current coordinates
     * @return: coordinates x,y of the agent in the environment
     */
    public Point getPosition(AgentID agentID) {
        int row = 0;
        boolean found = false;
        Point point = new Point();
        while (row<envGrid.size() && !found){
            int col=0;
            while(col < envGrid.get(row).size() && !found) {
                if (envGrid.get(row).get(col)==agentID) {
                    found = true;
                    point.setLocation(row, col);
                }
            }
            row++;
        }

        if (found){
            return point;
        } else {
            return null;
        }
    }

    @Override
    public void tickPreHook(long l) {

    }

    @Override
    public void tickPostHook(long l, int i, List<Future<DeliberationResult<String>>> list) {
        System.out.println("post");
        List<DeliberationResult<String>> results = new ArrayList<>();
        for (int a = 0; a < list.size(); a++) {
            try {
                results.add(list.get(a).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }
        processAction(results);
    }

    @Override
    public void simulationFinishedHook(long l, int i) {

    }

    /**
     * add agent in the grid environment
     * @param agentID
     * @param point
     * @return if the placement was successful or not
     */

    public boolean addAgent(AgentID agentID, Point point) {
        if (envGrid.get(point.x).get(point.y) == null) {
            List<AgentID> row = envGrid.get(point.x);
            row.set(point.y, agentID);

            //System.out.println("Agent placed:");
            //envGrid.forEach(System.out::println);
            return true;
        } else {
            // cell occupied
            return false;
        }
    }
}
