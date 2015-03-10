package gr.uoa.di.madgik.greencloudsim.experiments;

import gr.uoa.di.madgik.greencloudsim.Datacenter;
import gr.uoa.di.madgik.greencloudsim.GreenCloudSimulator;

/**
 * Created by michael on 21/11/14.
 */
public class GCLoadBalancingExperiment extends GreenCloudSimulator
{
    private static final int INITIAL_WORKLOAD_SIZE = 0;

    private static final int WORKLOAD_UPDATE_PERIOD = 60;

    private static final int WORKLOAD_UPDATE_SIZE = 2;

//    private int[] randomSequence;

    /**
     * Constructor
     * @throws Exception
     */
    public GCLoadBalancingExperiment() throws Exception
    {
        super();
//        randomSequence = Util.loadRandomSequenceFromFile();
    }

    @Override
    protected void setInitialWorkload() throws Exception
    {
        // 0 vms
    }

    @Override
    protected void updateWorkload(int round) throws Exception
    {
        if (round % WORKLOAD_UPDATE_PERIOD != 0)
        {
            return;
        }

        for (int i=0; i<WORKLOAD_UPDATE_SIZE; i++)
        {
            Datacenter.$().addOneVm(false);
        }
    }
}
