package agent.planfailure;

import Belief.Belief;
import agent.MoveToGoal;
import environment.DestinationUnavailableTrigger;
import environment.Environment;
import messages.ResolveGridConflictMessage;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Goal;
import nl.uu.cs.iss.ga.sim2apl.core.agent.PlanToAgentInterface;
import nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger.MessageReceiverNotFoundException;
import nl.uu.cs.iss.ga.sim2apl.core.plan.PlanExecutionError;
import nl.uu.cs.iss.ga.sim2apl.core.plan.builtin.RunOncePlan;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;
import nl.uu.cs.iss.ga.sim2apl.core.platform.PlatformNotFoundException;

import java.awt.*;
import java.util.ArrayList;

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
            Agent agent = planToAgentInterface.getAgent();
            Environment env = planToAgentInterface.getContext(Environment.class);

            // find the proposed point
            ArrayList<Point> possiblePoints = new ArrayList<Point>();
            Point unavailablePoint = trigger.getUnavailablePoint();

            Point proposedPoint = new Point(unavailablePoint.x-1, unavailablePoint.y);
            if (env.pointInGrid(proposedPoint) && env.envGrid.get(unavailablePoint.x-1).get(unavailablePoint.y)==null)
                sendMessage(platform, planToAgentInterface, myLocation, unavailablePoint, proposedPoint);
            else{
                proposedPoint = new Point(unavailablePoint.x + 1, unavailablePoint.y);
                if (env.pointInGrid(proposedPoint) && env.envGrid.get(unavailablePoint.x + 1).get(unavailablePoint.y) == null)
                    sendMessage(platform, planToAgentInterface, myLocation, unavailablePoint, proposedPoint);
                else {
                    proposedPoint = new Point(unavailablePoint.x, unavailablePoint.y-1);
                    if (env.pointInGrid(proposedPoint) && env.envGrid.get(unavailablePoint.x).get(unavailablePoint.y-1) == null)
                        sendMessage(platform, planToAgentInterface, myLocation, unavailablePoint, proposedPoint);
                    else {
                        proposedPoint = new Point(unavailablePoint.x, unavailablePoint.y+1);
                        if (env.pointInGrid(proposedPoint) && env.envGrid.get(unavailablePoint.x).get(unavailablePoint.y+1) == null);
                            sendMessage(platform, planToAgentInterface, myLocation, unavailablePoint, proposedPoint);
                    }
                }
            }

        } catch (PlatformNotFoundException | MessageReceiverNotFoundException e) {
            e.printStackTrace();
            throw new PlanExecutionError();
        }
        return null;
    }



    private void sendMessage(Platform platform, PlanToAgentInterface<String> planToAgentInterface, Point myLocation, Point unavailablePoint, Point proposedPoint) throws MessageReceiverNotFoundException {
        platform.getMessenger().deliverMessage(
                trigger.getOccupyingAgent(),
                new ResolveGridConflictMessage(
                        planToAgentInterface.getAgentID(),
                        myLocation,
                        unavailablePoint,
                        proposedPoint,
                        trigger.getOccupyingAgent()
                )
        );
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
