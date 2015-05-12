package gr.uoa.di.madgik.greencloudsim;

import gr.uoa.di.madgik.greencloudsim.experiments.*;

/**
 * Implements the execution entry point of the simulator.
 *
 * Created by michael on 21/11/14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CloudExperiment exps[] = new CloudExperiment[]{new ElasticityExperiment()};
//            new GCLoadBalancingExperiment(), new RRLoadBalancingExperiment(), new ElasticityExperiment()};

        for (CloudExperiment exp : exps) {
            CloudSimulator simulator = new VManCloudSimulator(exp);
            simulator.run();

            simulator.printOutEnergyConsumption();
        }

    }
}
