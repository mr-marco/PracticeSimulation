package agent;

import Belief.Belief;
import environment.Environment;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Goal;

import java.awt.*;

public class MoveToGoal extends Goal {

    public Point destinationPoint;

    public MoveToGoal(Point destinationPoint) {
        this.destinationPoint = destinationPoint;
    }

    @Override
    public boolean isAchieved(AgentContextInterface agentContextInterface) {
        // retrieve current position from the environmen
        Environment env = (Environment) agentContextInterface.getContext(Environment.class);

        Belief belief = ((Belief) agentContextInterface.getContext(Belief.class));
        AgentID agentID = belief.getAgentID();

        Point currentPos = env.getPosition(agentID);

        return (currentPos.x == this.destinationPoint.x && currentPos.y == this.destinationPoint.y);
    }
}
