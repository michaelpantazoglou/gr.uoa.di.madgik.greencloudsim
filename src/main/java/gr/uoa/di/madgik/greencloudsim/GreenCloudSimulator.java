package gr.uoa.di.madgik.greencloudsim;

import static gr.uoa.di.madgik.greencloudsim.HyperCube.constructHypercubeTopology;
import gr.uoa.di.madgik.greencloudsim.experiments.CloudExperiment;
import java.util.ArrayList;
import java.util.List;

/**
 * Implements the green cloud simulator by extending the simple cloud simulator.
 * The green cloud simulator performs load balancing by attempting partial VM
 * migration for overutilized nodes, and full VM migration for underutilized
 * nodes.
 *
 * Created by michael on 21/11/14.
 */
public class GreenCloudSimulator extends CloudSimulator {

    protected GreenCloudSimulator(CloudExperiment exp) {
        super(constructHypercubeTopology(
                Environment.$().getHypercubeDimension() - 1,
                Environment.$().getUseRandomIds()), exp);
    }

    @Override
    protected final LBResult performLoadBalancing() throws Exception {
        LBResult lbResult = new LBResult();
        LBResult partialVmMigrationResult;
        LBResult fullVmMigrationResult;
        LBResult OkVmMigrationResult;
        ArrayList<ArrayList<String>> oldWorkloads = Datacenter.$().getWorkloads();
        // retrieve overutilized compute nodes and attempt partial vm migration
        List<String> overutilized = new ArrayList<>();
        overutilized.addAll(Datacenter.$().getOverUtilizedComputeNodes());
//        System.out.println("Overutilized balancing");
        for (String nodeId : overutilized) {
            partialVmMigrationResult = Datacenter.$().doPartialVmMigration(nodeId);
            lbResult.merge(partialVmMigrationResult);
        }
//        System.out.println("Underutilized balancing");
        // retrieve underutilized compute nodes and attempt full vm migration
        List<String> underutilized = new ArrayList<>();
        underutilized.addAll(Datacenter.$().getUnderUtilizedComputeNodes());
        for (String nodeId : underutilized) {
//            System.out.println(" id is "+nodeId);
            fullVmMigrationResult = Datacenter.$().doFullVmMigration(nodeId);
            lbResult.merge(fullVmMigrationResult);
        }

        List<String> ok = new ArrayList<>();
        ok.addAll(Datacenter.$().getOkComputeNodes());
        for (String nodeId : ok) {
//            System.out.println(" id is "+nodeId);
            OkVmMigrationResult = Datacenter.$().doOkPackingMigrations(nodeId);
            lbResult.merge(OkVmMigrationResult);
        }
//        System.out.println("results gathering");
        // switch off idle compute nodes
        ArrayList<ArrayList<String>> newWorkloads = Datacenter.$().getWorkloads();
        int migrations = Util.optimalDiff(oldWorkloads, newWorkloads);

        lbResult.setVmMigrations(migrations);
        int switchoffs = Datacenter.$().switchOffIdleNodes();
        lbResult.setSwitchOffs(lbResult.getSwitchOffs() + switchoffs);

//		lbResult.setVmMigrations(partialVmMigrationResult.getVmMigrations() + fullVmMigrationResult.getVmMigrations());
//		lbResult.setSwitchOffs(partialVmMigrationResult.getSwitchOffs() + fullVmMigrationResult.getSwitchOffs() + switchoffs);
//		lbResult.setSwitchOns(partialVmMigrationResult.getSwitchOns() + fullVmMigrationResult.getSwitchOns());
        return lbResult;
    }
}
