package gr.uoa.di.madgik.greencloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * The Datacenter consists of a number of compute nodes organized in a hypercube
 * topology.
 *
 * Created by michael on 17/11/14.
 */
public class Datacenter {

    /**
     * singleton
     */
    private static final Datacenter sharedInstance = new Datacenter();

    /**
     * the compute nodes
     */
    private HashMap<String, ComputeNode> computeNodes;

    /**
     * indices
     */
    private List<String> switchedOffNodes;
    private List<String> idleNodes;
    private List<String> underutilizedNodes;
    private List<String> okNodes;
    private List<String> overutilizedNodes;
    private int switchONs;
    private int switchOffs;

    public void resetSwitchCounts() {
        switchOffs = 0;
        switchONs = 0;
    }

    public int getSwitchONs() {
        return switchONs;
    }

    public int getSwitchOffs() {
        return switchOffs;
    }

    /**
     * Private constructor.
     */
    private Datacenter() {
        switchOffs = 0;
        switchONs = 0;
        computeNodes = new HashMap<>();
        switchedOffNodes = new ArrayList<>();
        idleNodes = new ArrayList<>();
        underutilizedNodes = new ArrayList<>();
        okNodes = new ArrayList<>();
        overutilizedNodes = new ArrayList<>();

//        init();
    }

    /**
     * Returns the shared instance.
     *
     * @return
     */
    public static Datacenter $() {
//        if (null == sharedInstance)
//        {
//            sharedInstance = new GreenCloud();
//        }
        return sharedInstance;
    }

    public ArrayList<ArrayList<String>> getWorkloads() {

        ArrayList<ArrayList<String>> workloads = new ArrayList<>();
        for (int i = 0; i < computeNodes.size(); ++i) {
            workloads.add(new ArrayList<String>());
        }
        int i = 0;
        for (ComputeNode node : computeNodes.values()) {
            ArrayList<String> vms = workloads.get(i++);
            for (Object id : node.getWorkload().getVMS()) {
                vms.add(((VirtualMachineInstance) id).getId());
            }
        }
        return workloads;
    }

    /**
     * Make topology updates if the topology is dynamically changed
     */
    public void topologyUpdates() {
        for (ComputeNode node : computeNodes.values()) {
            if (!node.isSwitchedOff()) {
                node.topologyUpdate();
            }
        }
    }

    /**
     * private initializer.
     *
     * @param compute_nodes
     */
    public final void init(List<ComputeNode> compute_nodes) {
        computeNodes.clear();
        switchedOffNodes.clear();
        idleNodes.clear();
        underutilizedNodes.clear();
        okNodes.clear();
        overutilizedNodes.clear();

        /**
         * construct hypercube topology
         */
        //TOGO to Hypercube{
        // System.out.format("Constructing the %d-dimensional hypercube...\r\n", Environment.$().getHypercubeDimension());
        /**
         * set up compute nodes
         */
        System.out.println("Setting up compute nodes...");
        int numberOfSwitchedOnComputeNodes = 0;
        for (ComputeNode computeNode : compute_nodes) {
            if (numberOfSwitchedOnComputeNodes < Environment.$().getInitialClusterSize()) {
                computeNode.switchOn();
                numberOfSwitchedOnComputeNodes++;
            }
            computeNodes.put(computeNode.getId(), computeNode);
//            System.out.println(computeNode);
        }

        System.out.format("Cloud initialized with %d/%d switched on node(s)\r\n",
                numberOfSwitchedOnComputeNodes, computeNodes.size());
    }

    /**
     * Gets the current power consumption of this cloud.
     *
     * @return
     */
    public final double getCurrentPowerConsumption() {
        double powerConsumption = 0d;
        for (ComputeNode computeNode : computeNodes.values()) {
            powerConsumption += computeNode.getCurrentPowerConsumption();
        }
        return powerConsumption;
    }

    public final void addVirtualMachineInstance(VirtualMachineInstance vm, String nodeId) {
        computeNodes.get(nodeId).add(vm);
    }

    public final void removeVirtualMachineInstance(VirtualMachineInstance vm, String nodeId) {
        computeNodes.get(nodeId).remove(vm);
    }

    /**
     * Adds a new VM instance to a random compute node that is not overutilized.
     * Only active (idle, underutilized, ok) nodes are considered unless
     * otherwise specified.
     *
     * @param includeSwitchedOffComputeNodes
     */
    public final void addOneVm(boolean includeSwitchedOffComputeNodes) {
        // get all compute nodes that are not switched off or overutilized
        List<ComputeNode> active = new ArrayList<>();
        for (String id : getActiveComputeNodes(false)) {
            ComputeNode computeNode = computeNodes.get(id);
            if (computeNode.isSwitchedOff() && !includeSwitchedOffComputeNodes) {
                continue;
            }
            active.add(computeNode);
        }

        // select a random compute node that is not switched off or overutilized
        ComputeNode computeNode;
        int random;

        if (active.size() == 0) {
            // no active nodes, so switch one one random compute node
            switchONs++;
            if (switchedOffNodes.size() == 0) {
                // no switched off nodes either. Seems all nodes are overutilized...
                System.err.println("Cloud resources exhausted! Consider increasing the Hypercube dimension.");
                System.err.println("Number of switched off nodes  : " + switchedOffNodes.size());
                System.err.println("Number of idle nodes          : " + idleNodes.size());
                System.err.println("Number of underutilized nodes : " + underutilizedNodes.size());
                System.err.println("Number of ok nodes            : " + okNodes.size());
                System.err.println("Number of overutilized nodes  : " + overutilizedNodes.size());
                System.err.println("Terminating...");
                System.exit(-1);
            }
            random = Double.valueOf(Math.random() * switchedOffNodes.size()).intValue();
            computeNode = computeNodes.get(getSwitchedOffComputeNodes().get(random));
        } else {
            random = Double.valueOf(Math.random() * active.size()).intValue();
            computeNode = active.get(random);
        }

        String vmId = Environment.$().getUseRandomIds() ? IdentityGenerator.newRandomUUID() : IdentityGenerator.newVmInstanceId();
        addVirtualMachineInstance(new VirtualMachineInstance(vmId), computeNode.getId());
    }

    public final void removeOneVm() {
        /**
         * get all compute nodes that are not switched off or idle
         */
        List<ComputeNode> active = new ArrayList<>();
        for (ComputeNode computeNode : computeNodes.values()) {
            if (!computeNode.isSwitchedOff() && !computeNode.isIdle()) {
                active.add(computeNode);
            }
        }

        /**
         * select a random compute node from the list and remove a random vm
         * instance from its workload
         */
        if (active.isEmpty()) {
            return;
        }

        if (getNumberOfSwitchedOffComputeNodes() == computeNodes.size()) {
            return;
        }

        int random = Double.valueOf(Math.random() * active.size()).intValue();
//        System.out.println(active.size());
        ComputeNode computeNode = active.get(random);
        computeNode.removeRandomVm();
        if (computeNode.isSwitchedOff()) {
            switchOffs++;
            System.out.println("not useless!");
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        for (ComputeNode computeNode : computeNodes.values()) {
            sb.append(computeNode).append("\r\n");
        }

        return sb.toString();
    }

//    public final int getNumberOfActiveComputeNodes()
//    {
////        int n = 0;
////        for (ComputeNode computeNode : computeNodes.values())
////        {
////            if (!computeNode.isSwitchedOff())
////            {
////                n++;
////            }
////        }
////        return n;
//        return idleNodes.size() + underutilizedNodes.size() + okNodes.size() + overutilizedNodes.size();
//    }
    public final int getNumberOfSwitchedOffComputeNodes() {
//        int n = 0;
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isSwitchedOff())
//            {
//                n++;
//            }
//        }
//        return n;
        return switchedOffNodes.size();
    }

    public final int getNumberOfOverUtilizedComputeNodes() {
//        int n = 0;
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isOverUtilized())
//            {
//                n++;
//            }
//        }
//        return n;
        return overutilizedNodes.size();
    }

    public final int getNumberOfIdleComputeNodes() {
//        int n = 0;
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isIdle())
//            {
//                n++;
//            }
//        }
//        return n;
        return idleNodes.size();
    }

    /**
     * Calculates and returns the overall number of VM instances in this cloud.
     *
     * @return
     */
    public final int getNumberOfVmInstances() {
        int n = 0;
        for (ComputeNode computeNode : computeNodes.values()) {
            n += computeNode.getWorkload().size();
        }
        return n;
    }

    public final List<String> getOverUtilizedComputeNodes() {
//        List<String> result = new ArrayList<>();
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isOverUtilized())
//            {
//                result.add(computeNode.getId());
//            }
//        }
//        return result;
        return overutilizedNodes;
    }

    public final List<String> getIdleComputeNodes() {
//        List<String> result = new ArrayList<>();
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isIdle())
//            {
//                result.add(computeNode.getId());
//            }
//        }
//        return result;
        return idleNodes;
    }

    public final List<String> getOkComputeNodes() {
//        List<String> result = new ArrayList<>();
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isOk())
//            {
//                result.add(computeNode.getId());
//            }
//        }
//        return result;
        return okNodes;
    }

    /**
     * Gets all Ok, underutilized and idle computeNodes, if flag is true it will
     * return overutilized nodes too.
     *
     * @param includeOverutilized
     * @return
     */
    public final List<String> getActiveComputeNodes(boolean includeOverutilized) {
        List<String> result = new ArrayList<>();
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (!computeNode.isSwitchedOff())
//            {
//                if (!includeOverutilized)
//                {
//                    if (!computeNode.isOverUtilized())
//                    {
//                        result.add(computeNode.getId());
//                    }
//                }
//                else
//                {
//                    result.add(computeNode.getId());
//                }
//            }
//        }
        result.addAll(getIdleComputeNodes());
        result.addAll(getUnderUtilizedComputeNodes());
        result.addAll(getOkComputeNodes());
        if (includeOverutilized) {
            result.addAll(getOverUtilizedComputeNodes());
        }
        return result;
    }

    public final List<String> getSwitchedOffComputeNodes() {
//        List<String> result = new ArrayList<>();
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isSwitchedOff())
//            {
//                result.add(computeNode.getId());
//            }
//        }
//        return result;
        return switchedOffNodes;
    }

    public final List<String> getUnderUtilizedComputeNodes() {
//        List<String> result = new ArrayList<>();
//        for (ComputeNode computeNode : computeNodes.values())
//        {
//            if (computeNode.isUnderUtilized())
//            {
//                result.add(computeNode.getId());
//            }
//        }
//        return result;
        return underutilizedNodes;
    }

    /**
     * Attempts to perform partial migration of the VM instances hosted by the
     * specified compute node.
     *
     * @param thisNodeID
     * @return
     */
    public final LBResult doPartialVmMigration(String thisNodeID) {
        LBResult lbResult = new LBResult();

        ComputeNode computeNode = computeNodes.get(thisNodeID);
        if (!computeNode.isOverUtilized()) {
            return lbResult;
        }
        List<String> ok = Datacenter.$().okNodes;
        List<String> underutilized = Datacenter.$().underutilizedNodes;
        List<String> idle = Datacenter.$().idleNodes;
        List<String> switchedOff = Datacenter.$().switchedOffNodes;
        List<String> targetNeighbors = new ArrayList<>();
        for (String s : ok) {
            targetNeighbors.add(s);
        }
        for (String s : underutilized) {
            targetNeighbors.add(s);
        }
        for (String s : idle) {
            targetNeighbors.add(s);
        }
        for (String s : switchedOff) {
            targetNeighbors.add(s);
        }
        targetNeighbors.remove(thisNodeID);
        /**
         * loop for each neighbor
         */
        for (String id : targetNeighbors) {
            ComputeNode neighbor = computeNodes.get(id);

            /**
             * ignore neighbor if it has become overutilized too
             */
            if (neighbor.isOverUtilized()) {
                continue;
            }

            /**
             * start migrating as many vms as possible to this neighbor
             */
            while (true) {
                /**
                 * check if there are still vms in the compute node's workload
                 */
                if (computeNode.getWorkload().size() == 0) {
                    break;
                }

                /**
                 * get next vm
                 */
                VirtualMachineInstance vm = computeNode.getWorkload().getAt(0);

                /**
                 * calculate the maximum number of vms that can be migrated
                 */
                double Emax = (neighbor.getMaxPowerConsumptionThreshold() - neighbor.getCurrentPowerConsumption());
                double Evm = vm.getPowerConsumption();

//				System.out.format("Emax - Evm = %.2f\r\n", Emax - Evm);
                boolean migrationAllowed = (Evm < Emax);

                /**
                 * break loop when it is not possible to migrate more vms to
                 * this neighbor or if the compute node is not overutilized
                 * anymore
                 */
                if (!computeNode.isOverUtilized() || !migrationAllowed) {
                    break;
                }
                /**
                 * perform migration
                 */
//				System.out.format("Migrating one VM from node %s to node %s\r\n", computeNode.getId(), neighbor.getId());
                if (neighbor.isSwitchedOff()) {
                    neighbor.switchOn();
                    lbResult.incrementSwitchOns();
                }
                computeNode.remove(vm);
                if (computeNode.isSwitchedOff()) {
                    lbResult.incrementSwitchOffs();
                }
                neighbor.add(vm);
                lbResult.incrementVmMigrations();

                /**
                 * if after migration the neighbor is overutilized, break the
                 * migration loop
                 */
                if (neighbor.isOverUtilized()) {
                    break;
                }
            }
            /**
             * break loop if the node is no more overutilized
             */
            if (!computeNode.isOverUtilized()) {
                break;
            }
        }
//		System.out.println("Node " + nodeId + " migrated " + lbResult.getVmMigrations());
        return lbResult;
    }

    /**
     * Attempts a full migration of the current workload of the specified
     * compute node.
     *
     * @param thisNodeID
     * @return
     */
    public final LBResult doFullVmMigration(String thisNodeID) {
        LBResult lbResult = new LBResult();

        ComputeNode computeNode = computeNodes.get(thisNodeID);

        if (!computeNode.isUnderUtilized()) {
            return lbResult;
        }

        /**
         * get hypercube dimension
         */
        //ToGO hypercube
        /**
         * loop for each neighbor
         */
        ArrayList<String> peers = new ArrayList<>();
        peers.addAll(Datacenter.$().getActiveComputeNodes(false));
        peers.remove(thisNodeID);
        for (String id : peers) {

            ComputeNode neighbor = computeNodes.get(id);

            /**
             * ignore neighbor if it is overutilized or switched off
             */
            if (!neighbor.isOk() && !neighbor.isUnderUtilized()) {
                continue;
            }

            /**
             * start migrating as many vms as possible to this neighbor
             */
            while (true) {
                /**
                 * check if there are still vms in the compute node's workload
                 */
                if (computeNode.getWorkload().size() == 0) {
                    break;
                }

                /**
                 * get next vm
                 */
                VirtualMachineInstance vm = computeNode.getWorkload().getAt(0);

                /**
                 * calculate the maximum number of vms that can be migrated
                 */
                double Emax = (neighbor.getMaxPowerConsumptionThreshold()
                        - neighbor.getCurrentPowerConsumption());
                double Evm = vm.getPowerConsumption();

                boolean migrationAllowed = (Evm < Emax);

                /**
                 * break loop when it is not possible to migrate more vms to
                 * this neighbor or if the compute node is not overutilized
                 * anymore
                 */
                if (!migrationAllowed) {
                    break;
                }

                /**
                 * perform migration
                 */
                if (neighbor.isSwitchedOff()) {
                    neighbor.switchOn();
                    lbResult.incrementSwitchOns();
                }
                computeNode.remove(vm);
                if (computeNode.isSwitchedOff()) {// is redundant

                    lbResult.incrementSwitchOffs();
                }
                neighbor.add(vm);
                lbResult.incrementVmMigrations();

                /**
                 * if after migration the neighbor is overutilized, break the
                 * migration loop
                 */
                if (neighbor.isOverUtilized()) {
                    break;
                }
            }
        }

        return lbResult;
    }

    /**
     * VMAN tries to maximize the switched off computer nodes. The less loaded
     * node will migrate vms to the most loaded.
     *
     * @param thisNodeID id of the compute node performing the Balancing check
     * @return
     *
     */
    public final LBResult balanceVMAN(String thisNodeID) {
        ComputeNode thisNode = computeNodes.get(thisNodeID);
        LBResult lbResult = new LBResult();
        if (thisNode == null || thisNode.isSwitchedOff() || thisNode.isIdle()) {
            return lbResult;
        }

        //VMan loop for each peer
        for (String peerId : thisNode.getAllNeighbors()) {
            ComputeNode peer = computeNodes.get(peerId);

            //find a suitable peer
            if (peer == null || peer.isSwitchedOff()) {
                continue;
            }

            double a1 = thisNode.getCurrentPowerConsumption();
            double a2 = peer.getCurrentPowerConsumption();

            if (thisNode.isIdle()) {
                break;
            }
            if (peer.isIdle()) {
                continue;
            }

            double max = thisNode.getMaxPowerConsumptionThreshold();
            double max2 = peer.getMaxPowerConsumptionThreshold();
            double a1_avail = max - a1;
            double a2_avail = max2 - a2;
            double trans = Math.min(Math.min(a1, a2),
                    Math.min(a1_avail, a2_avail));
            ComputeNode to;
            ComputeNode from;
            double fromConsumption = 0;

            if (a1 <= a2) {
                // PUSH      
                from = thisNode;

                to = peer;
                a1 -= trans;
                a2 += trans;
                fromConsumption = a1;
            } else {
                // PULL 
                from = peer;
                to = thisNode;
                a1 += trans;
                a2 -= trans;
                fromConsumption = a2;
            }
            // Vman main loop
            while (from.getWorkload().size() > 0
                    && from.getCurrentPowerConsumption() > fromConsumption) {
                //check for a suitable vm can be done
                boolean found = false;
                int i = 0;
                //find a vm which can be transfered
                //note that this is a greedy algorithm, 
                //the optimal can be different
                //this is not needed when all vms have the
                //same power consumption
                while (found == false
                        && i < from.getWorkload().size()) {
                    VirtualMachineInstance vm = from.getWorkload().getAt(i++);
                    if ((vm.getPowerConsumption()
                            + to.getCurrentPowerConsumption())
                            < to.getMaxPowerConsumptionThreshold()) {
                        lbResult.incrementVmMigrations();
                        to.add(vm);
                        from.remove(vm);
                        if (from.isSwitchedOff()) {
                            System.out.println("is useless?");
                            lbResult.incrementSwitchOffs();
                        }
                        break;
                    }
                }
                if (found == false) {
                    break;
                }
            } //end of loop for a specific peer
        }//end of Vman main loop
        return lbResult;
    }

    /**
     * Switches off all idle compute nodes.
     *
     * @return the number of performed switch offs
     */
    public final int switchOffIdleNodes() {
        List<String> idle = new ArrayList<>();
        idle.addAll(getIdleComputeNodes());
        for (String nodeId : idle) {
            computeNodes.get(nodeId).switchOff();
        }
        return idle.size();
    }

//    private int roundRobinTicket = 0;
//
//    public void addVmUsingRoundRobin()
//    {
//        int activeN = getActiveComputeNodes(false).size();
//
//        if (activeN == 0)
//        {
//            return;
//        }
//
//        if (roundRobinTicket >= activeN)
//        {
//            roundRobinTicket = 0;
//        }
//
//        String nodeId = getActiveComputeNodes(false).get(roundRobinTicket);
//
//        VirtualMachineInstance vm = new VirtualMachineInstance(
//                Environment.$().getUseRandomIds()?IdentityGenerator.newRandomUUID():IdentityGenerator.newVmInstanceId());
//        computeNodes.get(nodeId).add(vm);
//
//        roundRobinTicket++;
//    }
//    public final void resetAndDistributeWorkloadUsingRoundRobin(int numberOfVms)
//    {
//        reset();
//
//        List<String> nodeIds = getActiveComputeNodes(false);
////        System.out.println("Active nodes = " + nodeIds.size());
//        if (nodeIds.isEmpty())
//        {
//            System.err.println("Could not add RR workload. No active nodes left.");
//            return;
//        }
//
//        int count = 0;
//        int idx = 0;
//        while (count < numberOfVms)
//        {
//            if (nodeIds.isEmpty())
//            {
//                System.err.println("Could not add RR workload. No active nodes left.");
//                return;
//            }
//
//            if (idx == nodeIds.size())
//            {
//                idx = 0;
//            }
//            String nodeId = nodeIds.get(idx);
//            ComputeNode computeNode = computeNodes.get(nodeId);
//            if (computeNode.isOverUtilized())
//            {
////                System.out.println("[***] " + computeNode);
//                nodeIds.remove(nodeId);
//                continue;
//            }
//            VirtualMachineInstance vm = new VirtualMachineInstance(
//                    Environment.$().getUseRandomIds()?IdentityGenerator.newRandomUUID():IdentityGenerator.newVmInstanceId());
//            computeNode.add(vm);
//            count++;
//            idx++;
//        }
//    }
    public boolean isExhausted() {
        return getNumberOfOverUtilizedComputeNodes() == computeNodes.size();
    }

    private void reset() {
//        computeNodes.clear();
        switchedOffNodes.clear();
        idleNodes.clear();
        underutilizedNodes.clear();
        okNodes.clear();
        overutilizedNodes.clear();

        int initialClusterSize = Environment.$().getInitialClusterSize();
        int counter = 0;
        for (ComputeNode computeNode : computeNodes.values()) {
            if (counter == initialClusterSize) {
                computeNode.switchOff();
            } else {
                computeNode.switchOn();
                counter++;
            }
        }
    }

    /**
     * Re-indexes the specified node.
     *
     * @param node
     * @param oldState
     */
    public final void reindex(ComputeNode node, ComputeNode.State oldState) {
        // first remove node from indices
        if (ComputeNode.State.SwitchedOff.equals(oldState)) {
            switchedOffNodes.remove(node.getId());
        } else if (ComputeNode.State.Idle.equals(oldState)) {
            idleNodes.remove(node.getId());
        } else if (ComputeNode.State.Underutilized.equals(oldState)) {
            underutilizedNodes.remove(node.getId());
        } else if (ComputeNode.State.Ok.equals(oldState)) {
            okNodes.remove(node.getId());
        } else if (ComputeNode.State.Overutilized.equals(oldState)) {
            overutilizedNodes.remove(node.getId());
        }

        // now reindex node based on its current state
        if (node.isSwitchedOff()) {
            switchedOffNodes.add(node.getId());
        } else if (node.isIdle()) {
            idleNodes.add(node.getId());
        } else if (node.isUnderUtilized()) {
            underutilizedNodes.add(node.getId());
        } else if (node.isOk()) {
            okNodes.add(node.getId());
        } else if (node.isOverUtilized()) {
            overutilizedNodes.add(node.getId());
        }
    }

    public final boolean addOneVmToComputeNode(int idx) {
        // return false if the datacenter is exhausted
        if (isExhausted()) {
            return false;
        }

        // identify the compute node that corresponds to the given idx
        Iterator<ComputeNode> iterator = computeNodes.values().iterator();
        int count = 0;
        String nodeId = null;
        while (count <= idx) {
            nodeId = iterator.next().getId();
            count++;
        }

        // check if the node is switched off or overutilized
        if (computeNodes.get(nodeId).isSwitchedOff() || computeNodes.get(nodeId).isOverUtilized()) {
            // augment idx
            idx++;
            if (idx == computeNodes.size()) {
                idx = 0;
            }

            // attempt to add the vm to the next compute node
            return addOneVmToComputeNode(idx);
        } else {
            // add the vm to the compute node
            this.addVirtualMachineInstance(Util.newVm(), nodeId);
//            System.out.println("VM added to node with id: " + nodeId + " and state: " + computeNodes.get(nodeId).getState());
//            System.out.println("Overutilized = " + getOverUtilizedComputeNodes().size());
//            System.out.println("Ok = " + getOkComputeNodes().size());
            return true;
        }
    }

//    public final boolean isUnderUtilized(int idx)
//    {
//        Iterator<ComputeNode> iterator = computeNodes.values().iterator();
//        int count = 0;
//        String nodeId = null;
//        while (count != idx)
//        {
//            nodeId = iterator.next().getId();
//            iterator.next();
//        }
//        return computeNodes.get(nodeId).isUnderUtilized();
//    }
    /**
     * Gets the maximum size of this datacenter, which amounts to the maximum
     * number of compute nodes.
     *
     * @return
     */
    public final int getMaximumSize() {
        return computeNodes.size();
    }

    /**
     * Gets the current size of this datacenter, which amounts to the current
     * number of active (idle, underutilized, ok, overutilized) compute nodes.
     *
     * @return
     */
    public final int getCurrentSize() {
        return getActiveComputeNodes(true).size();
    }

    LBResult doOkPackingMigrations(String nodeId) {
        LBResult results = new LBResult();
        List<String> ok = new ArrayList<>();
        ComputeNode thisNode = computeNodes.get(nodeId);
        if (thisNode == null || !thisNode.isOk()) {
            return results;
        }
//        ok.addAll(Datacenter.$().okNodes);
        ok.addAll(Datacenter.$().okNodes);
        ok.remove(nodeId);

        int i = 0;
        int numberOfVms = thisNode.getWorkload().size();
        VirtualMachineInstance vm = thisNode.getWorkload().getAt(i++);
        ArrayList<ComputeNode> log = new ArrayList<>();
        double sumk;

        bigloop:
        for (String peerID : ok) {
            ComputeNode peer = computeNodes.get(peerID);
            if (peer.isOk()) {
                //if the migration of the current vm is possible log it
                sumk = vm.getPowerConsumption();
                while ((peer.getCurrentPowerConsumption() + sumk)
                        < peer.getMaxPowerConsumptionThreshold()) {

                    log.add(peer);
                    if (i == numberOfVms) {
                        break bigloop;
                    }

                    vm = thisNode.getWorkload().getAt(i++);
                    sumk += vm.getPowerConsumption();
                }
            }

        }

        if (log.size() == numberOfVms) {
            results.setVmMigrations(numberOfVms);
            for (int j = 0; j < numberOfVms; ++j) {
                ComputeNode peer = log.get(j);
                vm = thisNode.getWorkload().getAt(0);
                thisNode.remove(vm);
                peer.add(vm);
            }
        }

        return results;

    }

    public void newsCastDebug() {
//        for (ComputeNode node : computeNodes.values()) {
//            if (node.isUnderUtilized()) {
//                System.out.print(" {" + node.getId()+"}");
//                for (String id : node.getAllNeighbors()) {
//                    System.out.print(" " + id);
//                }
//                System.out.println("\n-----------------------------------------");
//            }
//
//        }
    }

}
