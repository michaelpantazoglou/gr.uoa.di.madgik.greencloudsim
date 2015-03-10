package gr.uoa.di.madgik.greencloudsim;

/**
 * Implements the concrete measurements taken by the simulator.
 *
 * Created by michael on 21/11/14.
 */
public class Measurements
{
    public int round;

    public int switchedOffComputeNodes;
    public int idleComputeNodes;
    public int underutilizedComputeNodes;
    public int okComputeNodes;
    public int overutilizedComputeNodes;

    public int vmInstances;
    public int vmMigrations;
	public int switchOffs;
	public int switchOns;

    public double powerConsumption;

	public double vmMigrationPowerOverhead;
	public double switchOnsPowerOverhead;
	public double switchOffsPowerOverhead;

    public final void clear()
    {
        round = 0;
        switchedOffComputeNodes = 0;
        idleComputeNodes = 0;
        underutilizedComputeNodes = 0;
        okComputeNodes = 0;
        overutilizedComputeNodes = 0;
        vmInstances = 0;
        vmMigrations = 0;
		switchOffs = 0;
		switchOns = 0;
		vmMigrationPowerOverhead = 0;
		switchOnsPowerOverhead = 0;
		switchOffsPowerOverhead = 0;
    }

    /**
     * Returns a string representing the measurements headers.
     * @return
     */
    public static String headers()
    {
        return "Hour\tSwitchedOff\tIdle\tUnder\t  Ok\tOver\tActive\tVmInstances\tVmMigrations\tPower\tSwitchOns\tSwitchOffs\tVmMigrationOverhead\tSwitchOnsOverhead\tSwitchOffsOverhead";
    }

    @Override
    public String toString()
    {
        return String.format("%4d\t%11d\t%4d\t%5d\t%4d\t%4d\t%6d\t%11d\t%12d\t%.0f\t%9d\t%10d\t%17.2f\t%15.2f\t%16.2f",
                round, switchedOffComputeNodes, idleComputeNodes, underutilizedComputeNodes, okComputeNodes,
                overutilizedComputeNodes,(idleComputeNodes+underutilizedComputeNodes+okComputeNodes+overutilizedComputeNodes),
				vmInstances, vmMigrations, powerConsumption, switchOns, switchOffs,
				vmMigrationPowerOverhead, switchOnsPowerOverhead, switchOffsPowerOverhead);
    }
}
