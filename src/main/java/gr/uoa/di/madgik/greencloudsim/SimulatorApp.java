package gr.uoa.di.madgik.greencloudsim;

import static gr.uoa.di.madgik.greencloudsim.HyperCube.constructHypercubeTopology;
import java.util.List;

/**
 * Execution point of the GrrenCloud Simulator.
 *
 * Created by michael on 17/11/14.
 */
@Deprecated
public class SimulatorApp {

    /**
     * Main method.
     *
     * @param args
     * @throws Exception
     */
    static void load(String[] args) throws Exception {
        List<ComputeNode> compute_nodes = constructHypercubeTopology(
                Environment.$().getHypercubeDimension() - 1, Environment.$().getUseRandomIds());
        Datacenter.$().init(compute_nodes);
//        new ElasticityExperiment().run();
//        new ElasticityWithRandomWorkloadExperiment().run();
//        new ComparativePowerConsumptionExperiment(false, 10000).run();
//        new EquilibriumExperiment(0.5d).run();
//            new ElasticityWithRandomWorkloadAndVmDependenciesExperiment().run();

    }
}
