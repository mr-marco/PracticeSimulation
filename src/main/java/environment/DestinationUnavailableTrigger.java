package environment;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;

import java.awt.*;

public class DestinationUnavailableTrigger implements Trigger {

    private Point unavailablePoint;
    private Point proposedPoint;
    private AgentID occupyingAgent;

    public DestinationUnavailableTrigger(AgentID occupyingAgent, Point unavailablePoint) {
        this.unavailablePoint = unavailablePoint;
        this.occupyingAgent = occupyingAgent;
    }

    public Point getUnavailablePoint() {
        return unavailablePoint;
    }

    public void setUnavailablePoint(Point unavailablePoint) {
        this.unavailablePoint = unavailablePoint;
    }

    public AgentID getOccupyingAgent() {
        return occupyingAgent;
    }

    public void setOccupyingAgent(AgentID occupyingAgent) {
        this.occupyingAgent = occupyingAgent;
    }
}
