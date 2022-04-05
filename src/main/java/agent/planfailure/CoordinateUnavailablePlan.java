package agent.planfailure;

import Belief.Belief;
import agent.MoveToGoal;
import environment.DestinationUnavailableTrigger;
import environment.Environment;
import messages.ResolveGridConflictMessage;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Goal;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger.MessageReceiverNotFoundException;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.builtin.RunOncePlan;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;
import nl.uu.cs.iss.ga.sim2apl.core.platform.PlatformNotFoundException;

import java.awt.*;

/**
 * This plan is executed if the environment lets the agent know a previously requested action failed,
 * because another agent was already occupying the target square
 */
public class CoordinateUnavailablePlan extends RunOncePlan<String> {

    private final DestinationUnavailableTrigger trigger;

    public CoordinateUnavailablePlan(DestinationUnavailableTrigger trigger) {
        this.trigger = trigger;
    }

    @Override
    public String executeOnce(PlanToAgentInterface<String> planToAgentInterface) throws PlanExecutionError {
        try {
            Platform platform = planToAgentInterface.getAgent().getPlatform();
            Point myLocation = planToAgentInterface.getContext(Environment.class).getPosition(planToAgentInterface.getAgentID());

            Point myTarget = getCurrentDestination(planToAgentInterface);
            // TODO, maybe you can come up with a strategy, so both agents will propose the same conflict resolution?


            platform.getMessenger().deliverMessage(
                    trigger.getOccupyingAgent(),
                    new ResolveGridConflictMessage(
                            planToAgentInterface.getAgentID(),
                            myLocation, // TODO
                            myLocation, // TODO
                            myTarget,
                            trigger.getOccupyingAgent()
                    )
            );

        } catch (PlatformNotFoundException | MessageReceiverNotFoundException e) {
            e.printStackTrace();
            throw new PlanExecutionError();
        }
        return null;
    }

    private Point getCurrentDestination(PlanToAgentInterface<String> planToAgentInterface) {
        for(Goal goal : planToAgentInterface.getAgent().getGoals()) {
            if (goal instanceof MoveToGoal) {
                return ((MoveToGoal) goal).destinationPoint;
            }
        }
        return null;
    }
}
