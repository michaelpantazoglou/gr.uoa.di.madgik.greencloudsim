package gr.uoa.di.madgik.greencloudsim.experiments;

import gr.uoa.di.madgik.greencloudsim.ComputeNode;
import gr.uoa.di.madgik.greencloudsim.Datacenter;
import gr.uoa.di.madgik.greencloudsim.Environment;
import gr.uoa.di.madgik.greencloudsim.GreenCloudSimulator;
import java.util.List;

/**
 * Implements the convergence experiment. With this experiment, we
 * want to assess how fast the datacenter converges to a state where
 * its energy consumption is reduced and no more VM migrations are
 * needed.
 *
 * Created by michael on 25/11/14.
 */
public class ConvergenceExperiment extends CloudExperiment
{
    /** Determines the underutilized - overutilized ratio */
    private static final double underutilizedPercentage = 0.25d;
     public ConvergenceExperiment(){
         
     }
    @Override
    public void setInitialWorkload() throws Exception
    {
        // initially, all compute nodes are in idle state
        int all = Datacenter.$().getCurrentSize();
        int underutilized = (int) Math.round(all * underutilizedPercentage);

        // add vms to the first set of idle compute nodes until they become underutilized
		int numberOfVmsToAdd = Double.valueOf(Environment.$().getComputeNodeMinPowerConsumption() - Environment.$().getComputeNodeIdlePowerConsumption()).intValue();
		numberOfVmsToAdd /= Environment.$().getDefaultVmPowerConsumption();
		System.out.format("Adding %d VMs to each one of the %d underutilized nodes...\r\n", numberOfVmsToAdd, underutilized);
        for (int i=0; i<underutilized; i++)
        {
            for (int j=0; j<numberOfVmsToAdd; j++)
            {
                Datacenter.$().addOneVmToComputeNode(i);
            }
        }

        // add vms to the remaining idle compute nodes until they become overutilized
		numberOfVmsToAdd = Double.valueOf(Environment.$().getComputeNodeMaxPowerConsumption() - Environment.$().getComputeNodeIdlePowerConsumption()).intValue();
		int additionalVms = 0;
		if (numberOfVmsToAdd % Environment.$().getDefaultVmPowerConsumption() > 0)
		{
			additionalVms += 1;
		}
		numberOfVmsToAdd /= Environment.$().getDefaultVmPowerConsumption();
		numberOfVmsToAdd += additionalVms;
		System.out.format("Adding %d VMs to each one of the %d overutilized nodes...\r\n", numberOfVmsToAdd, all - underutilized);
        for (int i=underutilized; i<all; i++)
        {
			for (int j=0; j<numberOfVmsToAdd; j++)
            {
                Datacenter.$().addOneVmToComputeNode(i);
            }
        }
    }

    @Override
    public void updateWorkload(int round) throws Exception
    {

    }
}
