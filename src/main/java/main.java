import Belief.Belief;
import agent.GoalPlanScheme;
import agent.MoveToGoal;
import environment.Environment;
import nl.uu.cs.iss.ga.sim2apl.core.agent.Agent;
import nl.uu.cs.iss.ga.sim2apl.core.agent.AgentArguments;
import nl.uu.cs.iss.ga.sim2apl.core.defaults.messenger.DefaultMessenger;
import nl.uu.cs.iss.ga.sim2apl.core.platform.Platform;
import nl.uu.cs.iss.ga.sim2apl.core.tick.DefaultBlockingTickExecutor;
import nl.uu.cs.iss.ga.sim2apl.core.tick.DefaultSimulationEngine;
import nl.uu.cs.iss.ga.sim2apl.core.tick.TickExecutor;

import java.util.concurrent.ThreadLocalRandom;


import java.awt.*;
import java.net.URISyntaxException;

public class main {

    private static Environment environment;

    public static void main(String[] args) {
        TickExecutor<String> tickExecutor = new DefaultBlockingTickExecutor<>(1);
        DefaultMessenger<String> messenger = new DefaultMessenger<>();

        environment = new Environment();
        environment.initGrid(); // initialise grid environment
        Platform platform = Platform.newPlatform(tickExecutor, messenger);

        // add agents
        createAgent(platform, new Point(3,3));
        createAgent(platform, new Point(5,5));

        // create and start simulation
        DefaultSimulationEngine<String> simulationEngine = new DefaultSimulationEngine<>(platform, 100, environment);
        simulationEngine.start();

    }

    /**
     * create agent on the platform with the goal of going to x,y
     * @param platform
     * @param destinationGoal
     */
    private static void createAgent(Platform platform, Point destinationGoal) {
        Belief belief = new Belief();   // belief to access the AgentID externally
        AgentArguments<String> args = new AgentArguments<>();   // to incorporate environment, belief and plan scheme
        args.addContext(environment);
        args.addContext(belief);
        args.addGoalPlanScheme(new GoalPlanScheme());
        Agent<String> agent;
        try {
            agent = new Agent<>(platform, args);    // create agent
            belief.setAgentID(agent.getAID());  // set agentID of the belief
            agent.adoptGoal(new MoveToGoal(destinationGoal));   // add goal to destination point

            // initialise agent to random location. Try till a new free cell is found.
            boolean agentInEnvironment = false;
            while (!agentInEnvironment) {
                agentInEnvironment = environment.addAgent(agent.getAID(), new Point(ThreadLocalRandom.current().nextInt(0, environment.envGrid.size()),ThreadLocalRandom.current().nextInt(0, environment.envGrid.get(0).size())));
            }

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

    }

}
