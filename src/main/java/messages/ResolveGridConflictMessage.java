package messages;

import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentID;
import nl.uu.cs.iss.ga.sim2apl.core.fipa.MessageInterface;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;

import java.awt.*;
import java.util.*;
import java.util.logging.Level;

/**
 * Simple implementation of the message interface that allows agents to negotiate a
 * conflict resolution when they want to enter the same square.
 *
 * An agent making a conflict resolution proposal proposes the next point (s)he will enter
 * into (adjacent to their current position), and a different point the other agent could
 * enter into (adjacent to their position). It also communicates its target location, in
 * case the other agent will reject the offer, and make a counter proposal.
 *
 * TODO for you: When and how is a conflict resolution proposal accepted or rejected, if both agents
 * send a proposal at the same time?
 */
public class ResolveGridConflictMessage implements MessageInterface {

    private final Map<String, String> parameters = new HashMap<>();
    private final Collection<AgentID> receivers;
    private final AgentID sender;

    public ResolveGridConflictMessage(
            AgentID sender,
            Point myProposedLocation,
            Point yourProposedLocation,
            Point myTarget,
            AgentID... receivers
    ) {
        this.sender = sender;
        this.receivers = Arrays.asList(receivers);
        addUserDefinedParameter("X-messageID", String.valueOf(UUID.randomUUID()));
        setMyProposedNextCoordinates(myProposedLocation);
        setYourProposedNextCoordinates(yourProposedLocation);
        setMyTargetCoordinates(myTarget);
    }

    public void setMyProposedNextCoordinates(Point proposedPoint) {
        pointToParams(proposedPoint, "myX", "myY");
    }

    public Point getMyProposedNextCoordinates() {
        return paramsToPoint("myX", "myY");
    }

    public void setYourProposedNextCoordinates(Point proposedPoint) {
        pointToParams(proposedPoint, "yourX", "yourY");
    }

    public Point getYourProposedNextCoordinates() {
        return paramsToPoint("yourX", "yourY");
    }

    public void setMyTargetCoordinates(Point target) {
        pointToParams(target, "targetX", "targetY");
    }

    public Point getMyTargetCoordinates() {
        return paramsToPoint("targetX", "targetY");
    }

    private void pointToParams(Point p, String keyX, String keyY) {
        if (p != null) {
            this.addUserDefinedParameter(keyX, Integer.toString(p.x));
            this.addUserDefinedParameter(keyY, Integer.toString(p.y));
        } else {
            Platform.getLogger().log(getClass(), Level.WARNING, String.format(
                    "Null point given for (%s, %s)",
                    keyX, keyY
                )
            );
        }
    }

    private Point paramsToPoint(String keyX, String keyY) {
        String X = getUserDefinedParameter(keyX);
        String Y = getUserDefinedParameter(keyY);

        if (!(X == null || Y == null)) {
            return new Point(
                    Integer.parseInt(getUserDefinedParameter(keyX)),
                    Integer.parseInt(getUserDefinedParameter(keyY))
            );
        } else {
            return null;
        }
    }

    @Override
    public Collection<AgentID> getReceiver() {
        return this.receivers;
    }

    @Override
    public AgentID getSender() {
        return this.sender;
    }

    @Override
    public void addUserDefinedParameter(String key, String value) {
        this.parameters.put(key, value);
    }

    @Override
    public String getUserDefinedParameter(String key) {
        return this.parameters.get(key);
    }

    @Override
    public String getContent() {
        return "ConflictResolutionProposal";
    }
}
