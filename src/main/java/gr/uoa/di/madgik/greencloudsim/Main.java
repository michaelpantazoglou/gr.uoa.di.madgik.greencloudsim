package gr.uoa.di.madgik.greencloudsim;

import gr.uoa.di.madgik.greencloudsim.experiments.ConvergenceExperiment;

/**
 * Implements the execution entry point of the simulator.
 *
 * Created by michael on 21/11/14.
 */
public class Main
{
    public static void main(String[] args) throws Exception
    {
        CloudSimulator simulator =
				new ConvergenceExperiment();
//				new GCLoadBalancingExperiment();
//              new RRLoadBalancingExperiment();
//              new ElasticityExperiment();
        simulator.run();

        System.out.println();

        simulator.printOutEnergyConsumption();
    }
}
