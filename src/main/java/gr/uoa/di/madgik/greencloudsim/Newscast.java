package gr.uoa.di.madgik.greencloudsim;

import java.util.ArrayList;
import java.util.List;
// The base of this code is taken from peersim.example.simplenewscast 
// http://peersim.sourceforge.net/doc/

public class Newscast extends NetworkInterface {

// =============== static fields =======================================
// =====================================================================
// We are using static temporary arrays to avoid garbage collection
// of them. these are used by all Newscast protocols included
// in the protocol array so its size is the maximum of the cache sizes
    /**
     * Temp array for merging. Its size is the same as the cache size.
     */
    private static ComputeNode[] tn;
    /**
     * Random values generator
     */
    private static final ExtendedRandom randomGenerator = new ExtendedRandom(0);
    /**
     * Temp array for merging. Its size is the same as the cache size.
     */
    private static int[] ts;

// =================== fields ==========================================
// =====================================================================
    /**
     * Neighbors currently in the cache
     */
    private ComputeNode[] cache;
    /**
     * the compute node which has this interface, it is needed for the topology
     * update!
     */
    private ComputeNode thisNode;
    /**
     * Time stamps currently in the cache
     */
    private int[] tstamps;

// ====================== initialization ===============================
// =====================================================================
    public Newscast(int size) {

        final int cachesize = size;
        if (Newscast.tn == null || Newscast.tn.length < cachesize) {
            Newscast.tn = new ComputeNode[cachesize];
            Newscast.ts = new int[cachesize];
        }

        cache = new ComputeNode[cachesize];
        tstamps = new int[cachesize];
    }

// ====================== helper methods ==============================
// ====================================================================
    /**
     * Returns a peer node which is not switched off. This implementation starts
     * with a random node. If that is not reachable, proceed first towards the
     * older and then younger nodes until the first reachable is found.
     *
     * @return null if no accessible peers are found, the peer otherwise.
     */
    public ComputeNode getPeer() {

        final int d = degree();
        if (d == 0) {
            return null;
        }
        int index = randomGenerator.nextInt(d);
        ComputeNode result = cache[index];

        if (!result.isSwitchedOff()) {
            return result;
        }

        // proceed towards older entries
        for (int i = index + 1; i < d; ++i) {
            if (!cache[i].isSwitchedOff()) {
                return cache[i];
            }
        }

        // proceed towards younger entries
        for (int i = index - 1; i >= 0; --i) {
            if (!cache[i].isSwitchedOff()) {
                return cache[i];
            }
        }

        // no accessible peer
        return null;
    }

// --------------------------------------------------------------------
    /**
     * Merge the content of two nodes and adds a new version of the identifier.
     * The result is in the static temporary arrays. The first element is not
     * defined, it is reserved for the freshest new updates so it will be
     * different for peer and this. The elements of the static temporary arrays
     * will not contain neither peerComputeNode nor thisComputeNode.
     *
     * @param thisComputeNode the node that hosts this newscast protocol
     * instance (process)
     * @param peer The peer with which we perform cache exchange
     * @param peerComputeNode the node that hosts the peer newscast protocol
     * instance
     */
    private void merge(ComputeNode thisComputeNode, Newscast peer, ComputeNode peerComputeNode) {
        int i1 = 0; /* Index first cache */

        int i2 = 0; /* Index second cache */

        boolean first;
        boolean lastTieWinner = randomGenerator.nextBoolean();
        int i = 1; // Index new cache. first element set in the end
        // Newscast.tn[0] is always null. it's never written anywhere
        final int d1 = degree();
        final int d2 = peer.degree();
	// cachesize is cache.length

        // merging two arrays
        while (i < cache.length && i1 < d1 && i2 < d2) {
            if (tstamps[i1] == peer.tstamps[i2]) {
                lastTieWinner = first = !lastTieWinner;
            } else {
                first = tstamps[i1] > peer.tstamps[i2];
            }

            if (first) {
                if (cache[i1] != peerComputeNode && !Newscast.contains(i, cache[i1])) {
                    Newscast.tn[i] = cache[i1];
                    Newscast.ts[i] = tstamps[i1];
                    i++;
                }
                i1++;
            } else {
                if (peer.cache[i2] != thisComputeNode
                        && !Newscast.contains(i, peer.cache[i2])) {
                    Newscast.tn[i] = peer.cache[i2];
                    Newscast.ts[i] = peer.tstamps[i2];
                    i++;
                }
                i2++;
            }
        }

        // if one of the original arrays got fully copied into
        // tn and there is still place, fill the rest with the other
        // array
        if (i < cache.length) {
            // only one of the for cycles will be entered

            for (; i1 < d1 && i < cache.length; ++i1) {
                if (cache[i1] != peerComputeNode && !Newscast.contains(i, cache[i1])) {
                    Newscast.tn[i] = cache[i1];
                    Newscast.ts[i] = tstamps[i1];
                    i++;
                }
            }

            for (; i2 < d2 && i < cache.length; ++i2) {
                if (peer.cache[i2] != thisComputeNode
                        && !Newscast.contains(i, peer.cache[i2])) {
                    Newscast.tn[i] = peer.cache[i2];
                    Newscast.ts[i] = peer.tstamps[i2];
                    i++;
                }
            }
        }

        // if the two arrays were not enough to fill the buffer
        // fill in the rest with nulls
        if (i < cache.length) {
            for (; i < cache.length; ++i) {
                Newscast.tn[i] = null;
            }
        }
    }

// --------------------------------------------------------------------
    private static boolean contains(int size, ComputeNode peer) {
        for (int i = 0; i < size; i++) {
            if (Newscast.tn[i] == peer) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a NewsCast topology, each node is initiated with only one
     * neighbor, the previous constructed node. The number of nodes is
     * 2^dimension
     *
     * @param dimension
     * @param useRandomIds
     * @return
     */
    public static List<ComputeNode> constructNewsCastTopology(Integer dimension, boolean useRandomIds) {
        ArrayList<ComputeNode> nodes = new ArrayList<>();
        Newscast network = new Newscast(dimension * 2);
        ComputeNode previous = new ComputeNode(useRandomIds ? IdentityGenerator.newRandomUUID()
                : IdentityGenerator.newComputeNodeId(), network);
        network.init(previous);
        nodes.add(previous);
        network.refreshNeighborsNames();
        for (int i = 1; i < Math.pow(2, dimension); ++i) {
            Newscast tempNetwork = new Newscast(dimension * 2);
            ComputeNode temp = new ComputeNode(useRandomIds ? IdentityGenerator.newRandomUUID()
                    : IdentityGenerator.newComputeNodeId(), tempNetwork);
            tempNetwork.init(temp);
            tempNetwork.addNeighbor(previous);
            tempNetwork.refreshNeighborsNames();
            nodes.add(temp);
            previous = temp;
        }
        for (ComputeNode node : nodes) {
            node.switchOn();
        }

        for (int i = 0; i < 3; ++i) {

            SimulationTime.timePlus();

            for (ComputeNode node : nodes) {
                node.topologyUpdate();
            }

        }
        for (ComputeNode node : nodes) {
            node.switchOff();
        }
        return nodes;
    }

    /**
     * Debug method which prints the ids of all neighbors
     */
    public void printNeighbors() {

        System.out.print("\n[" + thisNode.getId() + "] :");
        for (ComputeNode node : cache) {
            if (node != null) {
                System.out.print(" " + node.getId());
            }
        }

    }

    /**
     * Return the number of neighbors, which might be less than cache size.
     */
    private int degree() {

        int len = cache.length - 1;
        while (len >= 0 && cache[len] == null) {
            len--;
        }
        return len + 1;
    }

// --------------------------------------------------------------------
    /**
     * Adds a new neighbor if its not already in the current cache. If cache is
     * full it throws IndexOutOfBoundException!
     *
     * @param node
     * @return
     * @exception IndexOutOfBoundsException
     */
    public boolean addNeighbor(ComputeNode node) {

        int i;
        for (i = 0; i < cache.length && cache[i] != null; i++) {
            if (cache[i] == node) {
                return false;
            }
        }

        if (i < cache.length) {
            if (i > 0 && tstamps[i - 1] < SimulationTime.getTime()) {
                // we need to insert to the first position
                for (int j = cache.length - 2; j >= 0; --j) {
                    cache[j + 1] = cache[j];
                    tstamps[j + 1] = tstamps[j];
                }
                i = 0;
            }
            cache[i] = node;
            tstamps[i] = SimulationTime.getTime();
            return true;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

// --------------------------------------------------------------------
//    private boolean contains(ComputeNode n) {
//        for (int i = 0; i < cache.length; i++) {
//            if (cache[i] == n) {
//                return true;
//            }
//        }
//        return false;
//    }
// --------------------------------------------------------------------
    /**
     * Simulates the run of newscast protocol of node. A random peer will be
     * selected and node and peer will merge their neighbors' lists. Both node's
     * and peer's network interface's neighbors' name lists will be updated.
     *
     * @param node
     */
    public void nextCycle(ComputeNode node) {
        ComputeNode peerComputeNode = getPeer();
        if (peerComputeNode == null) {
//            System.err.println("Newscast: no accessible peer");
            return;
        }

        Newscast peer = (Newscast) peerComputeNode.getInterface();
        merge(node, peer, peerComputeNode);

        // set new cache in this and peer
        System.arraycopy(Newscast.tn, 0, cache, 0, cache.length);
        System.arraycopy(Newscast.ts, 0, tstamps, 0, tstamps.length);
        System.arraycopy(Newscast.tn, 0, peer.cache, 0, cache.length);
        System.arraycopy(Newscast.ts, 0, peer.tstamps, 0, tstamps.length);

        // set first element
        tstamps[0] = peer.tstamps[0] = SimulationTime.getTime();
        cache[0] = peerComputeNode;
        peer.cache[0] = node;
        refreshNeighborsNames();
        peer.refreshNeighborsNames();
    }

    public void refreshNeighborsNames() {

        ArrayList<String> neighbors = new ArrayList<>();

        for (int i = 0; i < cache.length; ++i) {
            if (cache[i] != null) {
                neighbors.add(cache[i].getId());
            }
        }

        this.listNeighbors = neighbors;
    }

    @Override
    public void update() {
        nextCycle(thisNode);
    }

    public void init(ComputeNode node) {
        thisNode = node;

    }
}
