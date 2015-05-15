package gr.uoa.di.madgik.greencloudsim;

import gr.uoa.di.madgik.greencloudsim.experiments.*;

/**
 * Implements the execution entry point of the simulator.
 *
 * Created by michael on 21/11/14.
 */
public class Main {

    public static void main(String[] args) throws Exception {
        CloudExperiment exp;
        exp = new CompactingExpirement();
//        exp = new GCLoadBalancingExperiment();
//        exp = new ConvergenceExperiment();
//         CloudExperiment exps[] = new CloudExperiment[]{new ConvergenceExperiment()};
//        CloudExperiment exps[] = new CloudExperiment[]{new ConvergenceExperiment()};
        CloudSimulator simulator = new GreenCloudSimulator(exp);
        simulator.run();
        simulator.printOutEnergyConsumption();

    }
}
