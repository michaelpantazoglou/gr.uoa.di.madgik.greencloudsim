package gr.uoa.di.madgik.greencloudsim;

/**
 * Execution point of the GrrenCloud Simulator.
 *
 * Created by michael on 17/11/14.
 */
@Deprecated
public class SimulatorApp
{
    /**
     * Main method.
     *
     * @param args
     * @throws Exception
     */
    public static void main(String[] args) throws Exception
    {
        Datacenter.$().init();
//        new ElasticityExperiment().run();
//        new ElasticityWithRandomWorkloadExperiment().run();
//        new ComparativePowerConsumptionExperiment(false, 10000).run();
//        new EquilibriumExperiment(0.5d).run();
//            new ElasticityWithRandomWorkloadAndVmDependenciesExperiment().run();

    }
}
