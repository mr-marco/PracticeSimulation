package messages;

import environment.Environment;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.builtin.RunOncePlan;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;

import java.awt.*;
import java.util.List;
import java.util.logging.Level;

public class ReceiveConflictResolutionProposal extends RunOncePlan<String> {

    private ResolveGridConflictMessage message;

    public ReceiveConflictResolutionProposal(ResolveGridConflictMessage message) {
        this.message = message;
    }

    @Override
    public String executeOnce(PlanToAgentInterface<String> planToAgentInterface) throws PlanExecutionError {

        System.out.println(
                "Sender: "+message.getSender().toString().substring(2, 4)+"\n"+
                        "SenderLocation: "+message.getCurrentCoordinates()+"\n"+
                        "Cell occupied: "+message.getUnavailableCoordinates()+"\n"+
                        "SenderProposed: "+message.getProposedNextCoordinates()+"\n"
        );

        Point proposedPoint = new Point(message.getProposedNextCoordinates().x, message.getProposedNextCoordinates().y);
        AgentID meAgent = planToAgentInterface.getAgent().getAID();
        Environment env = planToAgentInterface.getContext(Environment.class);
        Point myLocation = env.getPosition(meAgent);

        if (proposedPoint.x > myLocation.x) return "down";
        else if (proposedPoint.x < myLocation.x) return "up";
        else if (proposedPoint.y > myLocation.y) return "right";
        else if (proposedPoint.y < myLocation.y) return "left";
        else return null;
    }
}
