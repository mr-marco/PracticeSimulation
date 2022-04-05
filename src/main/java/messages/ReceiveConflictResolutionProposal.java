package messages;

import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.builtin.RunOncePlan;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;

import java.util.logging.Level;

public class ReceiveConflictResolutionProposal extends RunOncePlan<String> {

    private ResolveGridConflictMessage message;

    public ReceiveConflictResolutionProposal(ResolveGridConflictMessage message) {
        this.message = message;
    }

    @Override
    public String executeOnce(PlanToAgentInterface<String> planToAgentInterface) throws PlanExecutionError {
        Platform.getLogger().log(getClass(), Level.INFO, String.format(
                "Agent %s received a message from %s with the proposal it moves into square (%d, %d) while they" +
                        "move to (%d, %d) to reach their target of (%d, %d)",
                planToAgentInterface.getAgentID(),
                message.getSender(),
                message.getYourProposedNextCoordinates() != null ? message.getYourProposedNextCoordinates().x : -1,
                message.getYourProposedNextCoordinates() != null ? message.getYourProposedNextCoordinates().y : -1,
                message.getMyProposedNextCoordinates() != null ? message.getMyProposedNextCoordinates().x : -1,
                message.getMyProposedNextCoordinates() != null ? message.getMyProposedNextCoordinates().y : -1,
                message.getMyTargetCoordinates() != null ? message.getMyTargetCoordinates().x : -1,
                message.getMyTargetCoordinates() != null ? message.getMyTargetCoordinates().y : -1
                )
        );

        // TODO, what do you want to do with the proposed solution

        return null;
    }
}
