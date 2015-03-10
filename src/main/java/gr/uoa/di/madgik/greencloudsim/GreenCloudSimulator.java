package gr.uoa.di.madgik.greencloudsim;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the green cloud simulator by extending the simple cloud simulator.
 * The green cloud simulator performs load balancing by attempting partial VM migration
 * for overutilized nodes, and full VM migration for underutilized nodes.
 *
 * Created by michael on 21/11/14.
 */
public abstract class GreenCloudSimulator extends CloudSimulator
{
    protected GreenCloudSimulator()
    {
        super();
    }

    @Override
    protected final LBResult performLoadBalancing() throws Exception
    {
        LBResult lbResult = new LBResult();
		LBResult partialVmMigrationResult = new LBResult();
		LBResult fullVmMigrationResult = new LBResult();

        // retrieve overutilized compute nodes and attempt partial vm migration
        List<String> overutilized = new ArrayList<>();
        overutilized.addAll(Datacenter.$().getOverUtilizedComputeNodes());
        for (String nodeId : overutilized)
        {
            partialVmMigrationResult = Datacenter.$().doPartialVmMigration(nodeId);
			lbResult.setVmMigrations(lbResult.getVmMigrations() + partialVmMigrationResult.getVmMigrations());
			lbResult.setSwitchOffs(lbResult.getSwitchOffs() + partialVmMigrationResult.getSwitchOffs());
			lbResult.setSwitchOns(lbResult.getSwitchOns() + partialVmMigrationResult.getSwitchOns());
        }

        // retrieve underutilized compute nodes and attempt full vm migration
        List<String> underutilized = new ArrayList<>();
        underutilized.addAll(Datacenter.$().getUnderUtilizedComputeNodes());
        for (String nodeId : underutilized)
        {
            fullVmMigrationResult = Datacenter.$().doFullVmMigration(nodeId);
			lbResult.setVmMigrations(lbResult.getVmMigrations() + fullVmMigrationResult.getVmMigrations());
			lbResult.setSwitchOffs(lbResult.getSwitchOffs() + fullVmMigrationResult.getSwitchOffs());
			lbResult.setSwitchOns(lbResult.getSwitchOns() + fullVmMigrationResult.getSwitchOns());
        }

        // switch off idle compute nodes
        int switchoffs = Datacenter.$().switchOffIdleNodes();
		lbResult.setSwitchOffs(lbResult.getSwitchOffs() + switchoffs);

//		lbResult.setVmMigrations(partialVmMigrationResult.getVmMigrations() + fullVmMigrationResult.getVmMigrations());
//		lbResult.setSwitchOffs(partialVmMigrationResult.getSwitchOffs() + fullVmMigrationResult.getSwitchOffs() + switchoffs);
//		lbResult.setSwitchOns(partialVmMigrationResult.getSwitchOns() + fullVmMigrationResult.getSwitchOns());

        return lbResult;
    }
}
