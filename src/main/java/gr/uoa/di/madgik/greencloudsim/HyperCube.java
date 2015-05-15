package gr.uoa.di.madgik.greencloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the Hypercube node.
 *
 * Created by michael on 17/11/14.
 */
public class HyperCube extends NetworkInterface {

    /**
     * hypercube neighbors
     */
    protected HashMap<Integer, String> neighbors;
//    private static int time = 0;

    /**
     * Constructor.
     *
     */
    protected HyperCube() {

        neighbors = new HashMap<>();
    }

    @Override
    public int getNeighborsMaxSize() {
        return Environment.$().getHypercubeDimension();
    }

    /**
     * Sets the neighbor at the specified dimension.
     *
     * @param dimension 0.. hypercube_dimension - 1
     * @param nodeId id of the node to become neighbor
     */
    private void setNeighbor(int dimension, String nodeId) {
        neighbors.put(dimension, nodeId);
    }

    /**
     * Creates hypercube Topology recursively
     *
     * @param dimension 0.. hypercube_dimension - 1
     * @param useRandomIds if true, the ids of nodes are random
     * @return a List of ComputeNode
     */
    public static List<ComputeNode> constructHypercubeTopology(Integer dimension,
            boolean useRandomIds) {
        List<ComputeNode> compute_node
                = constructHypercube(dimension, useRandomIds);
        for (ComputeNode node : compute_node) {
            ((HyperCube) node.getInterface()).setup();
        }
        return compute_node;
    }

    private static List<ComputeNode> constructHypercube(Integer dimension,
            boolean useRandomIds) {

        List<ComputeNode> computeNodes = new ArrayList<>();

        if (dimension == 0) {

            ComputeNode n1 = new ComputeNode(useRandomIds
                    ? IdentityGenerator.newRandomUUID()
                    : IdentityGenerator.newComputeNodeId(), new HyperCube());
            computeNodes.add(n1);

            ComputeNode n2 = new ComputeNode(useRandomIds
                    ? IdentityGenerator.newRandomUUID()
                    : IdentityGenerator.newComputeNodeId(), new HyperCube());
            computeNodes.add(n2);

            ((HyperCube) n1.getInterface()).setNeighbor(dimension, n2.getId());
            ((HyperCube) n2.getInterface()).setNeighbor(dimension, n1.getId());

            return computeNodes;
        }

        List<ComputeNode> l1 = constructHypercube(dimension - 1, useRandomIds);
        List<ComputeNode> l2 = constructHypercube(dimension - 1, useRandomIds);

        for (int i = 0; i < Math.pow(2, dimension); i++) {
            ((HyperCube) (l1.get(i).getInterface())).setNeighbor(dimension,
                    l2.get(i).getId());
            ((HyperCube) (l2.get(i).getInterface())).setNeighbor(dimension,
                    l1.get(i).getId());
        }

        List<ComputeNode> l = new ArrayList<>(l1);
        l.addAll(l2);

        return l;
    }

    /**
     * Simulates the end of each cycle, if the neighbors have changed this is
     * important, else it is redundant for the hypercube.
     */
    @Override
    public void update() {
//        this.listNeighbors = new ArrayList<>(neighbors.values());
    }

    /**
     * The neighbors of hypercube in the current implementation are static, so
     * only a setup is needed.
     */
    public void setup() {
        this.listNeighbors = new ArrayList<>(neighbors.values());
    }

}
