package agent;

import environment.Environment;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Goal;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.builtin.RunOncePlan;

import java.awt.*;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class KeepMovingPlan extends RunOncePlan<String> {

    public KeepMovingPlan() {
    }

    @Override
    public String executeOnce(PlanToAgentInterface<String> planToAgentInterface) throws PlanExecutionError {

        List<Goal> activeGoals = planToAgentInterface.getAgent().getGoals();
        for(Goal goal : activeGoals) {
            if (goal instanceof MoveToGoal) return null;
        }

        // Not actively moving to location, adopting new goal
        Agent agent = planToAgentInterface.getAgent();
        Environment env = planToAgentInterface.getContext(Environment.class);
        agent.adoptGoal(new MoveToGoal(new Point(ThreadLocalRandom.current().nextInt(0, env.envGrid.size()),ThreadLocalRandom.current().nextInt(0, env.envGrid.get(0).size()))));

        return null;
    }
}
