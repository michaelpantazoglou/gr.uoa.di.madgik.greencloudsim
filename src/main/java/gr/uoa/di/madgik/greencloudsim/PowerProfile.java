package gr.uoa.di.madgik.greencloudsim;

/**
 * Defines the power profile of a node.
 *
 * Created by michael on 25/10/14.
 */
public class PowerProfile {

    /**
     * Power consumption when the node is idle
     */
    private Double idlePowerConsumption;

    /**
     * Power consumption threshold below which the node should try to switch off
     */
    private Double minThreshold;

    /**
     * Power consumption threshold above which the node should try to reduce its
     * energy consumption
     */
    private Double maxThreshold;

    /**
     * Constructor.
     *
     * @param idlePowerConsumption
     * @param min
     * @param max
     */
    public PowerProfile(Double idlePowerConsumption, Double min, Double max) {
        this.idlePowerConsumption = idlePowerConsumption;
        this.minThreshold = min;
        this.maxThreshold = max;
    }

    public Double getIdlePowerConsumption() {
        return idlePowerConsumption;
    }

    public Double getMinThreshold() {
        return minThreshold;
    }

    public Double getMaxThreshold() {
        return maxThreshold;
    }
}
