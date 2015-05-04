package gr.uoa.di.madgik.greencloudsim;

import java.util.Properties;

/**
 * Implements the Simulator's environment.
 *
 * Created by michael on 17/11/14.
 */
public class Environment {

    /**
     * name of the environemnt properties file
     */
    private static final String ENVIRONMENT_PROPERTIES_FILE_NAME = "environment.properties";

    /**
     * singleton
     */
    private static Environment sharedInstance;

    /**
     * the hypercube dimension
     */
    private Integer hypercubeDimension;
    private Integer newsCastDimension;
    /**
     * compute node power profile properties
     */
    private Double computeNodeIdlePowerConsumption;
    private Double computeNodeMinPowerConsumption;
    private Double computeNodeMaxPowerConsumption;

    public Integer getNewsCastDimension() {
        return newsCastDimension;
    }

    /**
     * whether to use random power consumption per vm instance or not
     */
    private Boolean useRandomVmPowerConsumption;

    /**
     * default (constant) power consumption per vm instance
     */
    private Double defaultVmPowerConsumption;

    /**
     * minimum power consumption per vm instance
     */
    private Double minVmPowerConsumption;

    /**
     * maximum power consumption per vm instance
     */
    private Double maxVmPowerConsumption;

    /**
     * indicates whether to package VM instances (simulate VM dependencies) or
     * not
     */
    private Boolean packageVmInstances;

    /**
     * initial number of idle compute nodes upon startup
     */
    private Integer initialClusterSize;

    /**
     * Indicates whether to use random UUIDs or sequential numeric IDs for
     * compute nodes and vm instances
     */
    private Boolean useRandomIds;

    /**
     * number of simulation rounds
     */
    private Integer numberOfSimulationRounds;

    /**
     * period (in seconds) of load balancing
     */
    private Integer period;

    /**
     * power overhead induced by the migration of one VM
     */
    private Double vmMigrationPowerOverhead;

    /**
     * power overhead induced by switching off one compute node
     */
    private Double switchOffPowerConsumption;

    /**
     * power overhead induced by switching on one compute node
     */
    private Double switchOnPowerConsumption;

    /**
     * duration in seconds of a vm migration
     */
    private Integer vmMigrationDuration;

    /**
     * duration in seconds of a switch on
     */
    private Integer switchOnDuration;

    /**
     * duration in seconds of a switch off
     */
    private Integer switchOffDuration;

    /**
     * Constructor.
     *
     * @throws Exception
     */
    private Environment() throws Exception {
        Properties properties = new Properties();
        properties.load(getClass().getClassLoader().getResourceAsStream(ENVIRONMENT_PROPERTIES_FILE_NAME));

        newsCastDimension = Integer.valueOf(properties.getProperty("newscast.dimension"));
        hypercubeDimension = Integer.valueOf(properties.getProperty("hypercube.dimension"));
        computeNodeIdlePowerConsumption = Double.valueOf(properties.getProperty("compute.node.idle.power.consumption"));
        computeNodeMinPowerConsumption = Double.valueOf(properties.getProperty("compute.node.min.power.consumption"));
        computeNodeMaxPowerConsumption = Double.valueOf(properties.getProperty("compute.node.max.power.consumption"));
        useRandomVmPowerConsumption = Boolean.valueOf(properties.getProperty("vm.use.random.power.consumption"));
        defaultVmPowerConsumption = Double.valueOf(properties.getProperty("vm.default.power.consumption"));
        minVmPowerConsumption = Double.valueOf(properties.getProperty("vm.min.power.consumption"));
        maxVmPowerConsumption = Double.valueOf(properties.getProperty("vm.max.power.consumption"));
        packageVmInstances = Boolean.valueOf(properties.getProperty("package.vms"));
        initialClusterSize = Integer.valueOf(properties.getProperty("initial.cluster.size"));
        useRandomIds = Boolean.valueOf(properties.getProperty("use.random.ids"));
        numberOfSimulationRounds = Integer.valueOf(properties.getProperty("simulation.rounds"));
        period = Integer.valueOf(properties.getProperty("period"));
        vmMigrationPowerOverhead = Double.valueOf(properties.getProperty("vm.migration.power.overhead"));
        switchOnPowerConsumption = Double.valueOf(properties.getProperty("switch.on.power.consumption"));
        switchOffPowerConsumption = Double.valueOf(properties.getProperty("switch.off.power.consumption"));
        vmMigrationDuration = Integer.valueOf(properties.getProperty("vm.migration.duration"));
        switchOnDuration = Integer.valueOf(properties.getProperty("switch.on.duration"));
        switchOffDuration = Integer.valueOf(properties.getProperty("switch.off.duration"));
    }

    /**
     * Returns the singleton.
     *
     * @return
     */
    public static Environment $() {
        if (null == sharedInstance) {
            try {
                sharedInstance = new Environment();
            } catch (Exception e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        return sharedInstance;
    }

    public final Integer getHypercubeDimension() {
        return hypercubeDimension;
    }

    public final Double getComputeNodeIdlePowerConsumption() {
        return computeNodeIdlePowerConsumption;
    }

    public final Double getComputeNodeMinPowerConsumption() {
        return computeNodeMinPowerConsumption;
    }

    public final Double getComputeNodeMaxPowerConsumption() {
        return computeNodeMaxPowerConsumption;
    }

    public final Double getDefaultVmPowerConsumption() {
        return defaultVmPowerConsumption;
    }

    public final Boolean getUseRandomVmPowerConsumption() {
        return useRandomVmPowerConsumption;
    }

    public final Double getMinVmPowerConsumption() {
        return minVmPowerConsumption;
    }

    public final Double getMaxVmPowerConsumption() {
        return maxVmPowerConsumption;
    }

    public final Boolean getPackageVmInstances() {
        return packageVmInstances;
    }

    public final Integer getInitialClusterSize() {
        return initialClusterSize;
    }

    public final Boolean getUseRandomIds() {
        return useRandomIds;
    }

    public final Integer getNumberOfSimulationRounds() {
        return numberOfSimulationRounds;
    }

    public final Integer getPeriod() {
        return period;
    }

    public final Double getVmMigrationPowerOverhead() {
        return vmMigrationPowerOverhead;
    }

    public final Double getSwitchOffPowerConsumption() {
        return switchOffPowerConsumption;
    }

    public final Double getSwitchOnPowerConsumption() {
        return switchOnPowerConsumption;
    }

    public final Integer getVmMigrationDuration() {
        return vmMigrationDuration;
    }

    public final Integer getSwitchOnDuration() {
        return switchOnDuration;
    }

    public final Integer getSwitchOffDuration() {
        return switchOffDuration;
    }
}
