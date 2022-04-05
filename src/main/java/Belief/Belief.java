package Belief;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Context;

public class Belief implements Context {
    private AgentID agentID;

    public Belief() {}

    public AgentID getAgentID() {
        return agentID;
    }

    public void setAgentID(AgentID agentID) {
        this.agentID = agentID;
    }
}
