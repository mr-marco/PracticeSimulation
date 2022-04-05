package messages;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentContextInterface;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Trigger;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.Plan;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanScheme;

public class MessagePlanScheme implements PlanScheme<String> {

    @Override
    public Plan<String> instantiate(Trigger trigger, AgentContextInterface<String> agentContextInterface) {
        if (trigger instanceof MessageInterface) {
            MessageInterface message = (MessageInterface) trigger;

            if ("ConflictResolutionProposal".equals(message.getContent())) {
                return new ReceiveConflictResolutionProposal((ResolveGridConflictMessage) message);
            } else {
                // ...
                // TODO, maybe you want to have other messages?
            }

        }
        return null;
    }
}
