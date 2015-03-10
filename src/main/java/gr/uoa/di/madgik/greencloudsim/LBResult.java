package gr.uoa.di.madgik.greencloudsim;

/**
 * Holds the result of one round of load balancing.
 *
 * Created by michael on 27/11/14.
 */
public class LBResult
{
	private int vmMigrations;

	private int switchOns;

	private int switchOffs;

	public LBResult()
	{
		vmMigrations = 0;
		switchOffs = 0;
		switchOns = 0;
	}

	public int getVmMigrations()
	{
		return vmMigrations;
	}

	public void setVmMigrations(int vmMigrations)
	{
		this.vmMigrations = vmMigrations;
	}

	public int getSwitchOns()
	{
		return switchOns;
	}

	public void setSwitchOns(int switchOns)
	{
		this.switchOns = switchOns;
	}

	public int getSwitchOffs()
	{
		return switchOffs;
	}

	public void setSwitchOffs(int switchOffs)
	{
		this.switchOffs = switchOffs;
	}

	public void incrementVmMigrations()
	{
		vmMigrations++;
	}

	public void incrementSwitchOffs()
	{
		switchOffs++;
	}

	public void incrementSwitchOns()
	{
		switchOns++;
	}
}
