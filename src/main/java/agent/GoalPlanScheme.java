package agent;


import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

public class GoalPlanScheme implements PlanScheme<String> {

    @Override
    public Plan<String> instantiate(Trigger goal, AgentContextInterface agentContextInterface) {

        if (goal instanceof MoveToGoal) {
            return new MoveToPlan((MoveToGoal) goal);
        } else if (goal instanceof KeepMovingGoal) {
            return null;
        }
        return Plan.UNINSTANTIATED();
    }
}