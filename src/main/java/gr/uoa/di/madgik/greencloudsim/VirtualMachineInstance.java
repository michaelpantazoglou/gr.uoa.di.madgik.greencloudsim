package gr.uoa.di.madgik.greencloudsim;

/**
 * Implements the VirtualMachineInstance abstraction. Each VM instance has an ID
 * and a power consumption. For the time being, we don't care about the rest of
 * its characteristics.
 *
 * Created by michael on 17/11/14.
 */
public class VirtualMachineInstance {

    private String id;

    private Double powerConsumption;

    public VirtualMachineInstance(String id) {
        this.id = id;
        if (Environment.$().getUseRandomVmPowerConsumption()) {
            Double p = Environment.$().getMinVmPowerConsumption() + Math.random() * Environment.$().getMaxVmPowerConsumption();
            if (p > Environment.$().getMaxVmPowerConsumption()) {
                p = Environment.$().getMaxVmPowerConsumption();
            }
            powerConsumption = p;
        } else {
            powerConsumption = Environment.$().getDefaultVmPowerConsumption();
        }
    }

    public final String getId() {
        return id;
    }

    public final Double getPowerConsumption() {
        return powerConsumption;
    }
}
