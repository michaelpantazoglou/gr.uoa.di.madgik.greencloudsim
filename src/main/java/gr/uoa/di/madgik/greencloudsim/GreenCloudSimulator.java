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
        System.out.println("Green cloud simulator! ");
    }

    @Override
    protected final LBResult performLoadBalancing() throws Exception {
        LBResult lbResult = new LBResult();
        LBResult partialVmMigrationResult;
        LBResult fullVmMigrationResult;
        LBResult OkVmMigrationResult;

        //ArrayList<ArrayList<String>> oldworklaod = Datacenter.$().getWorkloads();
        
        // retrieve overutilized compute nodes and attempt partial vm migration
        List<String> overutilized = new ArrayList<>();
        overutilized.addAll(Datacenter.$().getOverUtilizedComputeNodes());

        for (String nodeId : overutilized) {
            partialVmMigrationResult = Datacenter.$().doPartialVmMigration(nodeId);
            lbResult.merge(partialVmMigrationResult);
        }
        // retrieve underutilized compute nodes and attempt full vm migration
        List<String> underutilized = new ArrayList<>();
        underutilized.addAll(Datacenter.$().getUnderUtilizedComputeNodes());
        for (String nodeId : underutilized) {
            fullVmMigrationResult = Datacenter.$().doFullVmMigration(nodeId);
            lbResult.merge(fullVmMigrationResult);
        }

        List<String> ok = new ArrayList<>();
        ok.addAll(Datacenter.$().getOkComputeNodes());
        for (String nodeId : ok) {
            OkVmMigrationResult = Datacenter.$().doOkPackingMigrations(nodeId);
            lbResult.merge(OkVmMigrationResult);
        }

        int switchoffs = Datacenter.$().switchOffIdleNodes();
        lbResult.setSwitchOffs(lbResult.getSwitchOffs() + switchoffs);

//        ArrayList<ArrayList<String>> newworkloads = Datacenter.$().getWorkloads();
//        int migrations = Util.optimalDiff(oldworklaod, newworkloads);
//        lbResult.setVmMigrations(migrations);
        return lbResult;
    }
}
