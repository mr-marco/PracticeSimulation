package agent;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Goal;

public class KeepMovingGoal extends Goal {
    @Override
    public boolean isAchieved(AgentContextInterface agentContextInterface) {
        return false;
    }
}
