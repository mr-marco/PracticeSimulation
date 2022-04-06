package environment;

import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
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

        for (int i = 0; i < 2; i++)  {
            this.envGrid.add(new ArrayList<AgentID>(Collections.nCopies(2, null)));
        }
    }

    public void processAction(DeliberationResult<String> agentAction) {
            List<String> actions = agentAction.getActions();

            String agentID = agentAction.getAgentID().toString().substring(2,4);

            for (String action: actions) {
                Point unavailablePoint = null;
                Point currentPos = getPosition(agentAction.getAgentID());

                List goals = null;
                try {
                    goals = platform.getLocalAgent(agentAction.getAgentID()).getGoals();
                } catch (URISyntaxException e) {
                    e.printStackTrace();
                }

                switch (action) {
                    case "left" -> {
                        if ((envGrid.get(currentPos.x).get(currentPos.y - 1)) == null) {
                            List<AgentID> row = envGrid.get(currentPos.x);
                            row.set(currentPos.y, null);
                            row.set(currentPos.y - 1, agentAction.getAgentID());
                        } else {
                            unavailablePoint = new Point(currentPos.x, currentPos.y - 1);
                        }
                    }
                    case "right" -> {
                        if ((envGrid.get(currentPos.x).get(currentPos.y + 1)) == null) {
                            List<AgentID> row = envGrid.get(currentPos.x);
                            row.set(currentPos.y, null);
                            row.set(currentPos.y + 1, agentAction.getAgentID());
                        } else {
                            unavailablePoint = new Point(currentPos.x, currentPos.y + 1);
                        }
                    }
                    case "up" -> {
                        if ((envGrid.get(currentPos.x - 1).get(currentPos.y)) == null) {
                            List<AgentID> row = envGrid.get(currentPos.x);
                            row.set(currentPos.y, null);
                            List<AgentID> newRow = envGrid.get(currentPos.x - 1);
                            newRow.set(currentPos.y, agentAction.getAgentID());
                        } else {
                            unavailablePoint = new Point(currentPos.x - 1, currentPos.y);
                        }
                    }
                    case "down" -> {
                        if ((envGrid.get(currentPos.x + 1).get(currentPos.y)) == null) {
                            List<AgentID> row = envGrid.get(currentPos.x);
                            row.set(currentPos.y, null);
                            List<AgentID> newRow = envGrid.get(currentPos.x + 1);
                            newRow.set(currentPos.y, agentAction.getAgentID());
                        } else {
                            unavailablePoint = new Point(currentPos.x + 1, currentPos.y);
                        }
                    }
                    default -> {
                    }
                }

                // if cell is occupied
                if (unavailablePoint != null) {
                    AgentID occupyingAgent = envGrid.get(unavailablePoint.x).get(unavailablePoint.y);
                    if (occupyingAgent != null) {
                        try {
                            DestinationUnavailableTrigger warningTrigger = new DestinationUnavailableTrigger(
                                    occupyingAgent,
                                    unavailablePoint
                            );
                            platform.getLocalAgent(agentAction.getAgentID()).addExternalTrigger(warningTrigger);
                            System.out.println("Message sent");
                        } catch (URISyntaxException e) {
                            e.printStackTrace();
                        }
                    }
                }

                System.out.println("Move agent "+agentAction.getAgentID().toString().substring(2, 4) +"  "+action);
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
                    System.out.print(envGrid.get(i).get(j).toString().substring(2, 4)+" ");
                else
                    System.out.print("-- ");
            }
            System.out.println();
        }
    }

    public boolean pointInGrid(Point p){
        return p.x >= 0 && p.x <= envGrid.size()-1 && p.y >= 0 && p.y <= envGrid.get(0).size()-1;
    }
}
