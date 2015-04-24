package gr.uoa.di.madgik.greencloudsim.experiments;

import gr.uoa.di.madgik.greencloudsim.ComputeNode;
import gr.uoa.di.madgik.greencloudsim.Datacenter;
import gr.uoa.di.madgik.greencloudsim.GreenCloudSimulator;
import gr.uoa.di.madgik.greencloudsim.Util;
import java.util.List;

/**
 * Implements the elasticity experiment. The goal of this experiment is to
 * assess the ability of the datacenter to scale up and down (i.e. to switch on
 * and off compute nodes) in response to changes in the workload (i.e.
 * increase/decrease of number of vm instances).
 *
 * Created by michael on 21/11/14.
 */
public class ElasticityExperiment extends CloudExperiment {

    /**
     * number of VM instances to add/remove in each simulation round
     */
    private static final int WORKLOAD_UPDATE_SIZE = 4;

    /**
     * initial number of vm instances
     */
    private static final int WORKLOAD_INITIAL_SIZE = 16;

    private int[] randomSequence;

    /**
     * Constructor
     *
     * @param compute_nodes
     * @throws Exception
     */
    public ElasticityExperiment() throws Exception {
            name = "Elasticity";
      

        randomSequence = Util.loadRandomSequenceFromFile();
    }

    @Override
    public void setInitialWorkload() throws Exception {
        for (int i = 0; i < WORKLOAD_INITIAL_SIZE; i++) {
            Datacenter.$().addOneVm(false);
        }
    }

    @Override
    public void updateWorkload(int round) throws Exception {
        if (randomSequence[round - 1] == 0) {
            for (int i = 0; i < WORKLOAD_UPDATE_SIZE; i++) {
                Datacenter.$().removeOneVm();
            }
        } else {
            for (int i = 0; i < WORKLOAD_UPDATE_SIZE; i++) {
                Datacenter.$().addOneVm(false);
            }
        }
    }
}
