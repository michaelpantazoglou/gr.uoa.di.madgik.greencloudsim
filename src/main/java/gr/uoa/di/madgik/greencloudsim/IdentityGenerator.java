package gr.uoa.di.madgik.greencloudsim;

import java.util.UUID;

/**
 * Implements a simple, dummy as f@ck identity generator.
 *
 * Created by michael on 17/11/14.
 */
public class IdentityGenerator
{

    // counter of compute nodes, used for generation of sequential compute node ids.
    private static int computeNodesCounter = 0;

    // counter of vm instances, used for generation of sequential vm instance ids.
    private static int vmInstancesCounter = 0;

    /**
     * Generates and returns a random UUID.
     *
     * @return
     */
    public static String newRandomUUID()
    {
        return UUID.randomUUID().toString();
    }

    /**
     * Generates and returns a new sequential compute node id.
     *
     * @return
     */
    public static String newComputeNodeId()
    {
        return String.valueOf(computeNodesCounter++);
    }

    /**
     * Generates and returns a new sequential vm instance id.
     * @return
     */
    public static String newVmInstanceId()
    {
        return String.valueOf(vmInstancesCounter++);
    }
}
