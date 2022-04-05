package environment;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Context;
import nl.uu.cs.iss.ga.sim2apl.core.deliberation.DeliberationResult;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;
import nl.uu.cs.iss.ga.sim2apl.core.tick.TickHookProcessor;

import java.awt.*;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class Environment implements Context, TickHookProcessor<String> {
    public List<List<AgentID>> envGrid;
    private Platform platform;

    public Environment(Platform platform) {
        this.platform = platform;
    }

    /**
     * Initialise the grid cells
     */
    public void initGrid(){
        this.envGrid = new ArrayList<>();

        for (int i = 0; i < 10; i++)  {
            this.envGrid.add(new ArrayList<AgentID>(Collections.nCopies(10, null)));
        }
    }

    public void processAction(DeliberationResult<String> agentAction){
            List<String> actions = agentAction.getActions();
            Point currentPos = getPosition(agentAction.getAgentID());

            for (String action: actions) {
                switch (action){
                    case "left": {
                        List<AgentID> row = envGrid.get(currentPos.x);
                        row.set(currentPos.y, null);
                        row.set(currentPos.y - 1, agentAction.getAgentID());
                        currentPos.y--;
                        break;
                    }
                    case "right": {
                        List<AgentID> row = envGrid.get(currentPos.x);
                        row.set(currentPos.y, null);
                        row.set(currentPos.y + 1, agentAction.getAgentID());
                        currentPos.y++;
                        break;
                    }
                    case "up": {
                        List<AgentID> row = envGrid.get(currentPos.x);
                        row.set(currentPos.y, null);
                        List<AgentID> newRow = envGrid.get(currentPos.x-1);
                        newRow.set(currentPos.y, agentAction.getAgentID());
                        currentPos.x--;
                        break;
                    }
                    case "down": {
                        List<AgentID> row = envGrid.get(currentPos.x);
                        row.set(currentPos.y, null);
                        List<AgentID> newRow = envGrid.get(currentPos.x + 1);
                        newRow.set(currentPos.y, agentAction.getAgentID());
                        currentPos.x++;
                        break;
                    }
                    default:
                        break;
                }

                if (false) { // TODO, if agent could not move
                    Point unavailablePoint = currentPos; // TODO, what point did the agent want to move into
                    AgentID occupyingAgent = envGrid.get(unavailablePoint.x).get(unavailablePoint.y);
                    if (occupyingAgent != null) {
                        try {
                            DestinationUnavailableTrigger warningTrigger = new DestinationUnavailableTrigger(
                                    unavailablePoint, occupyingAgent
                            );
                            platform.getLocalAgent(agentAction.getAgentID()).addExternalTrigger(warningTrigger);
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Move agent :"+agentAction.getAgentID() +"  "+action);
                //envGrid.forEach(System.out::println);
                printEnv();
            }

    }

    /**
     * @param agentID: target agent to retrieve current coordinates
     * @return: coordinates x,y of the agent in the environment
     */
    public Point getPosition(AgentID agentID) {
        for (int i = 0; i < envGrid.size(); i++) {
            for (int j = 0; j < envGrid.get(i).size(); j++) {
                if (agentID.equals(envGrid.get(i).get(j))) {
                    return new Point(i, j);
                }
            }
        }
        return null;
    }

    @Override
    public void tickPreHook(long l) {
    }

    @Override
    public void tickPostHook(long l, int i, List<Future<DeliberationResult<String>>> list) {
        for (int a = 0; a < list.size(); a++) {
            try {
                processAction(list.get(a).get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        }

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

            System.out.println("Agent placed:");
            printEnv();
            return true;
        } else {
            // cell occupied
            return false;
        }
    }


    private void printEnv(){
        for (int i = 0; i < envGrid.size(); i++) {
            for (int j = 0; j < envGrid.get(i).size(); j++) {
                if (envGrid.get(i).get(j) != null)
                    System.out.print(envGrid.get(i).get(j).toString().substring(3, 5)+" ");
                else
                    System.out.print("-- ");
            }
            System.out.println();
        }
    }
}
