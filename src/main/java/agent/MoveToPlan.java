package agent;

import Belief.Belief;
import environment.Environment;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.builtin.RunOncePlan;

import java.awt.*;

public class MoveToPlan extends RunOncePlan<String> {

    private final MoveToGoal goal;

    public MoveToPlan(MoveToGoal goal) {
        this.goal = goal;   // return the first move to do
    }

    @Override
    public String executeOnce(PlanToAgentInterface<String> planToAgentInterface) throws PlanExecutionError {
        // retrieve current position from the environment
        Environment env = planToAgentInterface.getContext(Environment.class);   // retrieve environement
        Point currentPos = env.getPosition(planToAgentInterface.getContext(Belief.class).getAgentID()); // retrieve position from the environment with the subject agentID

        // calculate action
        if (currentPos.x != goal.destinationPoint.x) {
            if (currentPos.x < goal.destinationPoint.x) {
                return "down";
            } else {
                return "up";
            }
        } else {
            if (currentPos.y < goal.destinationPoint.y) {
                return "right";
            } else {
                return "left";
            }
        }
    }
}
