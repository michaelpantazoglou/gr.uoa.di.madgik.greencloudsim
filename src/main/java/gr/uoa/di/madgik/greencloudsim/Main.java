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
        CloudExperiment exps[] = new CloudExperiment[]{ new ConvergenceExperiment()};//,
//            new GCLoadBalancingExperiment(), new RRLoadBalancingExperiment(), new ElasticityExperiment()};
        for (CloudExperiment exp : exps) {
            CloudSimulator simulator = new GreenCloudSimulator(exp);
            simulator.run();

            System.out.println();

            simulator.printOutEnergyConsumption();
        }
    }
}
