package gr.uoa.di.madgik.greencloudsim;

import static gr.uoa.di.madgik.greencloudsim.HyperCube.constructHypercubeTopology;
import java.util.Arrays;
import java.util.List;

/**
 * Implements the basis of the cloud simulator. All experiments are expected to
 * override this class and implement its abstract methods.
 *
 * Created by michael on 21/11/14.
 */
public abstract class CloudSimulator {

    /**
     * the cloud's energy consumption (in KWh) for each simulation round (hour)
     */
    private double[] energyConsumption;

    /**
     * Constructor.
     */
    protected CloudSimulator(List<ComputeNode> compute_nodes ) {
        
        Datacenter.$().init(compute_nodes);
        energyConsumption = new double[Environment.$().getNumberOfSimulationRounds() / 3600];
        Arrays.fill(energyConsumption, 0d);
    }

    /**
     * Sets the cloud's initial workload (i.e., distributes a number of VM
     * instances to the cloud's compute nodes).
     *
     * @throws Exception
     */
    protected abstract void setInitialWorkload() throws Exception;

    /**
     * Updates the cloud's workload by either adding or removing VM instances
     * to/from the cloud's compute nodes.
     *
     * @param round
     * @throws Exception
     */
    protected abstract void updateWorkload(int round) throws Exception;

    /**
     * Takes and returns the measurements for the specified round.
     *
     * @param round
     * @param vmMigrations
     * @return
     * @throws Exception
     */
    protected final Measurements takeMeasurements(int round, int vmMigrations) throws Exception {
        Measurements measurements = new Measurements();
        measurements.round = round;
        measurements.switchedOffComputeNodes = Datacenter.$().getSwitchedOffComputeNodes().size();
        measurements.idleComputeNodes = Datacenter.$().getIdleComputeNodes().size();
        measurements.underutilizedComputeNodes = Datacenter.$().getUnderUtilizedComputeNodes().size();
        measurements.okComputeNodes = Datacenter.$().getOkComputeNodes().size();
        measurements.overutilizedComputeNodes = Datacenter.$().getOverUtilizedComputeNodes().size();
        measurements.vmInstances = Datacenter.$().getNumberOfVmInstances();
        measurements.vmMigrations = vmMigrations;
        measurements.powerConsumption = Datacenter.$().getCurrentPowerConsumption();

        return measurements;
    }

    /**
     * Performs load balancing.
     *
     * @return the number of VM migrations
     * @throws Exception
     */
    protected abstract LBResult performLoadBalancing() throws Exception;

    /**
     * Runs the simulation.
     *
     * @throws Exception
     */
    public final void run() throws Exception {
        int hours = 0;
        int period = Environment.$().getPeriod();

        setInitialWorkload();

        System.out.println(Measurements.headers());
        System.out.println(takeMeasurements(0, 0));

        // initialize power consumption accumulators
        double powerAccumulator = 0d;
        double vmMigrationPowerOverhead = 0d;
        double switchOffsPowerOverhead = 0d;
        double switchOnsPowerOverhead = 0d;

        // initialize vm migrations, switch offs/ons accumulators
        int vmMigrations = 0;
        int switchOffs = 0;
        int switchOns = 0;

        // loop for each simulation round (second)
        int rounds = Environment.$().getNumberOfSimulationRounds();
        for (int round = 1; round <= rounds; round++) {
            // accumulate power consumption on a per-second basis
            powerAccumulator += Datacenter.$().getCurrentPowerConsumption();

            // periodicly perform load balancing
            if (round % period == 0) {
                LBResult lbResult = performLoadBalancing();
                vmMigrations += lbResult.getVmMigrations();
                switchOffs += lbResult.getSwitchOffs();
                switchOns += lbResult.getSwitchOns();
//				System.out.format("round %d, vmMigrations = %d\r\n", round, vmMigrations);
                powerAccumulator += lbResult.getVmMigrations() * Environment.$().getVmMigrationPowerOverhead() * Environment.$().getVmMigrationDuration();
                powerAccumulator += lbResult.getSwitchOffs() * Environment.$().getSwitchOffPowerConsumption() * Environment.$().getSwitchOffDuration();
                powerAccumulator += lbResult.getSwitchOns() * Environment.$().getSwitchOnPowerConsumption() * Environment.$().getSwitchOnDuration();

                vmMigrationPowerOverhead += lbResult.getVmMigrations() * Environment.$().getVmMigrationPowerOverhead() * Environment.$().getVmMigrationDuration();
                switchOffsPowerOverhead += lbResult.getSwitchOffs() * Environment.$().getSwitchOffPowerConsumption() * Environment.$().getSwitchOffDuration();
                switchOnsPowerOverhead += lbResult.getSwitchOns() * Environment.$().getSwitchOnPowerConsumption() * Environment.$().getSwitchOnDuration();
            }

            // take hourly measurements and reset
            if (round % 3600 == 0) {
                // print out measurements of this hour
                Measurements measurements = takeMeasurements(hours, vmMigrations);
                measurements.round = hours + 1;
                measurements.powerConsumption = powerAccumulator / 3600; // avg power consumption
                measurements.switchOffs = switchOffs;
                measurements.switchOns = switchOns;
                measurements.vmMigrationPowerOverhead = vmMigrationPowerOverhead / 3600;
                measurements.switchOffsPowerOverhead = switchOffsPowerOverhead / 3600;
                measurements.switchOnsPowerOverhead = switchOnsPowerOverhead / 3600;
                System.out.println(measurements);

                // calculate hourly energy consumption
                double kwh = powerAccumulator / 1000 / 3600;
                energyConsumption[hours++] = kwh;

                // reset power accumulators for the next hour
                powerAccumulator = 0d;
                vmMigrationPowerOverhead = 0d;
                switchOffsPowerOverhead = 0d;
                switchOnsPowerOverhead = 0d;

                // reset vm migrations, switch on/off accumulators for the next hour
                vmMigrations = 0;
                switchOffs = 0;
                switchOns = 0;
            }

            // update the workload on a per-second basis
            updateWorkload(round);
            Datacenter.$().topologyUpdates();
        } // end loop for each simulation round (second)
    }

    /**
     * Gets the simulated cloud's hourly energy consumption (in KWh).
     *
     * @return
     */
    public final double[] getEnergyConsumption() {
        return energyConsumption;
    }

    public void printOutEnergyConsumption() {
        System.out.println("Hour\tEnergyConsumption");
        for (int i = 0; i < energyConsumption.length; i++) {
            System.out.format("%4d\t%15.2f\r\n", i, Double.valueOf(energyConsumption[i]));
        }
    }
}
