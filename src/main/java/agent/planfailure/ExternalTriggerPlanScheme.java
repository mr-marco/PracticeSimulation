package agent.planfailure;

import environment.DestinationUnavailableTrigger;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

public class ExternalTriggerPlanScheme implements PlanScheme<String> {

    @Override
    public Plan<String> instantiate(Trigger trigger, AgentContextInterface agentContextInterface) {

        if (trigger instanceof DestinationUnavailableTrigger) {
            return new CoordinateUnavailablePlan(((DestinationUnavailableTrigger) trigger));
        }

        return Plan.UNINSTANTIATED();
    }
}
