package gr.uoa.di.madgik.greencloudsim;

import static gr.uoa.di.madgik.greencloudsim.HyperCube.constructHypercubeTopology;
import gr.uoa.di.madgik.greencloudsim.experiments.*;
import java.util.List;

/**
 * Implements the execution entry point of the simulator.
 *
 * Created by michael on 21/11/14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        List<ComputeNode> compute_nodes = constructHypercubeTopology(
                Environment.$().getHypercubeDimension() - 1,
                Environment.$().getUseRandomIds());
        CloudSimulator simulator  = 
                new ConvergenceExperiment(compute_nodes);
                //new GCLoadBalancingExperiment(compute_nodes);
                //new RRLoadBalancingExperiment(compute_nodes);
//                new ElasticityExperiment(compute_nodes);
        simulator.run();

        System.out.println();

        simulator.printOutEnergyConsumption();
    }
}
