package gr.uoa.di.madgik.greencloudsim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a number of utility methods.
 *
 * Created by michael on 17/11/14.
 */
public class Util
{
    public static List<ComputeNode> constructHypercubeTopology(Integer dimension, boolean useRandomIds)
    {
        List<ComputeNode> computeNodes = new ArrayList<>();

        if (dimension == 0) {

            ComputeNode n1 = new ComputeNode(useRandomIds?IdentityGenerator.newRandomUUID():IdentityGenerator.newComputeNodeId());
            computeNodes.add(n1);

            ComputeNode n2 = new ComputeNode(useRandomIds?IdentityGenerator.newRandomUUID():IdentityGenerator.newComputeNodeId());
            computeNodes.add(n2);

            n1.setNeighbor(dimension, n2.getId());
            n2.setNeighbor(dimension, n1.getId());

            return computeNodes;
        }

        List<ComputeNode> l1 = constructHypercubeTopology(dimension - 1, useRandomIds);
        List<ComputeNode> l2 = constructHypercubeTopology(dimension - 1, useRandomIds);

        for (int i = 0; i<Math.pow(2, dimension); i++) {
            l1.get(i).setNeighbor(dimension, l2.get(i).getId());
            l2.get(i).setNeighbor(dimension, l1.get(i).getId());
        }

        List<ComputeNode> l = new ArrayList<>(l1);
        l.addAll(l2);

        return l;
    }

    /**
     * Creates and returns a new VirtualMachineInstance.
     * Depending on whether the environment has been set to use random IDs or not,
     * the new VirtualMachineInstance is given either a random UUID or a sequential new VM instance ID.
     *
     * @return
     */
    public static VirtualMachineInstance newVm()
    {
        VirtualMachineInstance vm = new VirtualMachineInstance(
                Environment.$().getUseRandomIds()?IdentityGenerator.newRandomUUID():IdentityGenerator.newVmInstanceId());
        return vm;
    }

    /**
     * Loads the contents of file "random.txt" into an int[] array.
     * This array is expected to be used to simulate random increases or decreases of
     * workload ( 1 = increase, 0 = decrease ).
     *
     * @return
     * @throws Exception
     */
    public static int[] loadRandomSequenceFromFile() throws Exception
    {
        int[] result = new int[Environment.$().getNumberOfSimulationRounds()];
        BufferedReader br = null;
        try
        {
            br = new BufferedReader(new FileReader("random.txt"));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null)
            {
                result[i++] = Integer.valueOf(line);
            }
            return result;
        }
        finally
        {
            if (null != br)
            {
                br.close();
            }
        }
    }
}
