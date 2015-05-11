package gr.uoa.di.madgik.greencloudsim;

import java.util.ArrayList;
import java.util.List;

/**
 * Implements the compute node.
 *
 * Created by michael on 17/11/14.
 */
public class ComputeNode extends Node {

    public void topologyUpdate() {
        networkInteface.update();
    }

    /**
     * Enumerates the possible states of a compute node.
     */
    public static enum State {

        Overutilized, Underutilized, Ok, SwitchedOff, Idle
    }
    private final NetworkInterface networkInteface;
    /**
     * power profile of this node
     */
    private final PowerProfile powerProfile;

    /**
     * workload of this node
     */
    private final Workload workload;

    /**
     * state of this node
     */
    private State state;

    /**
     * Constructor.
     *
     * @param id
     * @param network
     */
    public ComputeNode(String id, NetworkInterface network) {
        super(id);
        networkInteface = network;
        /**
         * initialize power profile
         */
        powerProfile = new PowerProfile(
                Environment.$().getComputeNodeIdlePowerConsumption(),
                Environment.$().getComputeNodeMinPowerConsumption(),
                Environment.$().getComputeNodeMaxPowerConsumption()
        );

        workload = new Workload();

        /**
         * all nodes start as switched off by default
         */
        switchOff();
    }

//    public String getNeighbor(int i) {
//        return networkInteface.getNeighbor(i);
//    }
//    public void setNeighbor(Integer dimension, String id) {
//        networkInteface.setNeighbor(dimension, id);
//    }
    public NetworkInterface getInterface() {
        return networkInteface;
    }

    /**
     * Gets the workload of this node.
     *
     * @return
     */
    public final Workload getWorkload() {
        return workload;
    }
 
    /**
     * Adds a VM to this node.
     *
     * @param vm the VM to be added
     */
    public final void add(VirtualMachineInstance vm) {
        workload.add(vm);
        refreshState();
    }

    /**
     * Removes a VM from this node.
     *
     * @param vm the VM to be removed
     */
    public final void remove(VirtualMachineInstance vm) {
        workload.remove(vm);
        refreshState();
    }

    /**
     * Removes a VM from this node.
     *
     * @param vmId the id of the VM to be removed
     */
    public final void remove(String vmId) {
        workload.remove(vmId);
        refreshState();
    }

    /**
     * Removes a random VM from this node.
     */
    public final void removeRandomVm() {
        int random = Double.valueOf(Math.random() * workload.size()).intValue();
        workload.removeAt(random);
        refreshState();
    }

    /**
     * Switches on this node bringing it into Idle state.
     */
    public final void switchOn() {
        State oldState = state;
        workload.clear();
        state = State.Idle;

        Datacenter.$().reindex(this, oldState);
    }

    /**
     * Switches off this node.
     */
    public final void switchOff() {
        State oldState = state;
        workload.clear();
        state = State.SwitchedOff;

        Datacenter.$().reindex(this, oldState);
    }

    /**
     * Returns true if this node is switched off.
     *
     * @return
     */
    public final boolean isIdle() {
        return state.equals(State.Idle);
    }

    /**
     * Returns true if this node is ok.
     *
     * @return
     */
    public final boolean isOk() {
        return state.equals(State.Ok);
    }

    /**
     * Returns true if this node is overutilized.
     *
     * @return
     */
    public final boolean isOverUtilized() {
        return state.equals(State.Overutilized);
    }

    /**
     * Returns true if this node is underutilized.
     *
     * @return
     */
    public final boolean isUnderUtilized() {
        return state.equals(State.Underutilized);
    }

    /**
     * Returns true if this node is switched off.
     *
     * @return
     */
    public final boolean isSwitchedOff() {
        return state.equals(State.SwitchedOff);
    }

    /**
     * Calculates and returns the current power consumption of this node.
     *
     * @return
     */
    public final Double getCurrentPowerConsumption() {
        if (isSwitchedOff()) {
            return 0d;
        }
        return powerProfile.getIdlePowerConsumption() + workload.getPowerConsumption();
    }

    /**
     * Refreshes the state of this node based on its current state and workload.
     */
    public void refreshState() {
        State oldState = state;

        /**
         * check if there is no workload
         */
        if (workload.size() == 0) {
            /**
             * check if the node is currently idle
             */
            if (isIdle()) {
                switchOff();
            } else {
                /**
                 * only set to idle if the node is not switched off
                 */
                if (!isSwitchedOff()) {
                    state = State.Idle;
                }
            }
        } else {
            /**
             * the node has some workload, get its current power consumption
             */
            double currentPowerConsumption = getCurrentPowerConsumption();

            if (currentPowerConsumption <= powerProfile.getMinThreshold()) {
                state = State.Underutilized;
            } else if (currentPowerConsumption
                    >= powerProfile.getMaxThreshold()) {
                state = State.Overutilized;
            } else {
                state = State.Ok;
            }
        }

        Datacenter.$().reindex(this, oldState);
    }

    public State getState() {
        return state;
    }

    public List<String> getAllNeighbors() {
        return networkInteface.getListOfNeighbors();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("compute node ")
                .append("{")
                .append("id: ").append(id).append(", ")
                .append("state: ").append(state).append(", ")
                .append("workload: ").append(workload.size()).append(", ")
                .append("powerConsumption: ").append(getCurrentPowerConsumption())
                .append("}");

//        sb.append("\r\n");
//
//        for (int i=0; i<Environment.$().getHypercubeDimension(); i++)
//        {
//            sb.append("\t").append("Neighbor at dimension ").append(i).append(": ").append(getNeighbor(i)).append("\r\n");
//        }
        return sb.toString();
    }

    /**
     * Gets the max power consumption threshold of this node.
     *
     * @return
     */
    public double getMaxPowerConsumptionThreshold() {
        return powerProfile.getMaxThreshold();
    }

    /**
     * Returns a list with the ids of all the neighbors of this node, whose
     * state is Ok.
     *
     * @return
     */
    public List<String> getOkNeighbors() {
        List<String> okNodes = Datacenter.$().getOkComputeNodes();
        List<String> filteredNeighbors = new ArrayList<>();
        for (String nodeId : networkInteface.getListOfNeighbors()) {

            if (okNodes.contains(nodeId)) {
                filteredNeighbors.add(nodeId);
            }
        }
        return filteredNeighbors;
    }

    /**
     * Returns a list with the ids of all the neighbors of this node, whose
     * state is Underutilized.
     *
     * @return
     */
    public List<String> getUnderutilizedNeighbors() {
        List<String> underutilizedNodes = Datacenter.$().getUnderUtilizedComputeNodes();
        List<String> filteredNeighbors = new ArrayList<>();
        for (String nodeId : networkInteface.getListOfNeighbors()) {
            if (underutilizedNodes.contains(nodeId)) {
                filteredNeighbors.add(nodeId);
            }
        }
        return filteredNeighbors;
    }

    /**
     * Returns a list with the ids of all the neighbors of this node, whose
     * state is Idle.
     *
     * @return
     */
    public List<String> getIdleNeighbors() {
        List<String> idleNodes = Datacenter.$().getIdleComputeNodes();
        List<String> filteredNeighbors = new ArrayList<>();
        for (String nodeId : networkInteface.getListOfNeighbors()) {
            if (idleNodes.contains(nodeId)) {
                filteredNeighbors.add(nodeId);
            }
        }
        return filteredNeighbors;
    }

    /**
     * Returns a list with the ids of all the neighbors of this node, whose
     * state is SwitchedOff.
     *
     * @return
     */
    public List<String> getSwitchedOffNeighbors() {
        List<String> switchedOffComputeNodes = Datacenter.$().getSwitchedOffComputeNodes();
        List<String> filteredNeighbors = new ArrayList<>();
        for (String nodeId : networkInteface.getListOfNeighbors()) {
            if (switchedOffComputeNodes.contains(nodeId)) {
                filteredNeighbors.add(nodeId);
            }
        }
        return filteredNeighbors;
    }
}
