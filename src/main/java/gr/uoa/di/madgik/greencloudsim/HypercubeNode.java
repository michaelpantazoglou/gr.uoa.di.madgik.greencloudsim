package gr.uoa.di.madgik.greencloudsim;

import java.util.HashMap;

/**
 * Implements the Hypercube node.
 *
 * Created by michael on 17/11/14.
 */
public abstract class HypercubeNode extends Node
{
    /** hypercube neighbors */
    protected HashMap<Integer,String> neighbors;

    /**
     * Constructor.
     *
     * @param id
     */
    protected HypercubeNode(String id)
    {
        super(id);
        neighbors = new HashMap<>();
    }

    /**
     * Gets the neighbor at the specified dimension.
     *
     * @param dimension 0.. hypercube_dimension - 1
     * @return
     */
    public final String getNeighbor(int dimension)
    {
        return neighbors.get(dimension);
    }

    /**
     * Sets the neighbor at the specified dimension.
     *
     * @param dimension 0.. hypercube_dimension - 1
     * @param nodeId id of the node to become neighbor
     */
    public final void setNeighbor(int dimension, String nodeId)
    {
        neighbors.put(dimension, nodeId);
    }
}
