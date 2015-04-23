package gr.uoa.di.madgik.greencloudsim.experiments;

import gr.uoa.di.madgik.greencloudsim.CloudSimulator;
import gr.uoa.di.madgik.greencloudsim.ComputeNode;
import gr.uoa.di.madgik.greencloudsim.Datacenter;
import gr.uoa.di.madgik.greencloudsim.Environment;
import gr.uoa.di.madgik.greencloudsim.LBResult;

import java.util.List;

/**
 * Created by michael on 21/11/14.
 */
//public class RRLoadBalancingExperiment extends CloudSimulator {
//
//    private static final int INITIAL_WORKLOAD_SIZE = 0;
//
//    private static final int WORKLOAD_UPDATE_PERIOD = 60;
//
//    private static final int WORKLOAD_UPDATE_SIZE = 2;
//
//    private int roundRobinTicket = 0;
//
//    public RRLoadBalancingExperiment(List<ComputeNode> compute_nodes) {
//        super(compute_nodes);
//    }
//
//    @Override
//    protected void setInitialWorkload() throws Exception {
//        List<String> active = Datacenter.$().getActiveComputeNodes(false);
//
//        // evenly distribute workload
//        for (int i = 0; i < INITIAL_WORKLOAD_SIZE; i++) {
//            // at the initial stage, all nodes are idle so there is no need to check their state
//            Datacenter.$().addOneVmToComputeNode(roundRobinTicket++);
//            if (roundRobinTicket == Environment.$().getInitialClusterSize()) {
//                roundRobinTicket = 0;
//            }
//        }
//    }
//
//    @Override
//    protected void updateWorkload(int round) throws Exception {
//        // add workload only every N seconds
//        if (round % WORKLOAD_UPDATE_PERIOD != 0) {
//            return;
//        }
//
//        for (int i = 0; i < WORKLOAD_UPDATE_SIZE; i++) {
//            boolean vmAdded = Datacenter.$().addOneVmToComputeNode(roundRobinTicket++);
//            if (!vmAdded) {
//                System.out.println(takeMeasurements(round / 3600, 0));
//                System.out.println();
//                printOutEnergyConsumption();
//                System.err.println("Datacenter is exhausted. Exiting...");
//                System.exit(-1);
//            }
//
//            if (roundRobinTicket == Math.pow(2, Environment.$().getHypercubeDimension())) {
//                roundRobinTicket = 0;
//            }
//        }
//    }
//
//    @Override
//    protected LBResult performLoadBalancing() throws Exception {
//        return new LBResult();
//    }
//}
