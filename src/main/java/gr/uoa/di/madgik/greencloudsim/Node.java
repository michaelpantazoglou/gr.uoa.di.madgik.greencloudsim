package gr.uoa.di.madgik.greencloudsim;

/**
 * Abstract base Node class definition.
 *
 * Created by michael on 17/11/14.
 */
public abstract class Node {

    protected String id;

    /**
     * Constructor.
     *
     * @param id the node id
     */
    protected Node(String id) {
        this.id = id;
    }

    /**
     * Gets the node id.
     *
     * @return
     */
    public final String getId() {
        return id;
    }
}
