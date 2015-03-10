package gr.uoa.di.madgik.greencloudsim;

/**
 * Defines the control flow of all experiments.
 *
 * Created by michael on 17/11/14.
 */
@Deprecated
public abstract class Experiment
{
    /** holds the cloud's energy consumption in KWh for each hour of the experiment */
    protected double[] energyConsumption;

    /**
     * Actions taken upon startup of the experiment.
     *
     * @throws Exception
     */
    protected abstract void doUponStartup() throws Exception;

    /**
     * Takes measurements for the specified simulation round.
     *
     * @param round
     * @throws Exception
     */
    protected abstract Measurements takeMeasurements(int round) throws Exception;

    /**
     * Actions taken after the specified simulation round.
     *
     * @param round
     * @throws Exception
     */
    protected abstract void doAfterRound(int round) throws Exception;

    /**
     * Actions taken upon completion of the experiment.
     *
     * @throws Exception
     */
    protected abstract void doUponCompletion() throws Exception;

    /**
     * Executes this experiment.
     *
     * @throws Exception
     */
    public final void run() throws Exception
    {
        Datacenter.$();

        doUponStartup();

        /** run the specified number of rounds */
        for (int round=1; round<= Environment.$().getNumberOfSimulationRounds(); round++)
        {
            takeMeasurements(round);
            doAfterRound(round);
        }

        doUponCompletion();
    }
}
