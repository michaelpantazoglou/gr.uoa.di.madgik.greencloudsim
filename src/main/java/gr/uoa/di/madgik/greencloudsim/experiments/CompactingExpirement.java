/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim.experiments;

import gr.uoa.di.madgik.greencloudsim.Datacenter;
import gr.uoa.di.madgik.greencloudsim.Environment;
import gr.uoa.di.madgik.greencloudsim.Util;

/**
 *
 * @author Skulos
 */
public class CompactingExpirement extends CloudExperiment {

    /**
     * number of VM instances to add/remove in each simulation round
     */
    private static final int WORKLOAD_UPDATE_SIZE = 2;

    /**
     * initial number of vm instances
     */
    private static final int WORKLOAD_INITIAL_SIZE = 1024;
    private int mod;
    private int[] randomSequence;

    /**
     * Constructor
     *
     * @param compute_nodes
     * @throws Exception
     */
    public CompactingExpirement() throws Exception {
        name = "Compacting";
        mod = (int) (1/((double)WORKLOAD_INITIAL_SIZE * 9 / Environment.$().getNumberOfSimulationRounds()));
        System.out.println("mod is "+mod);
        
        randomSequence = Util.loadRandomSequenceFromFile();
    }

    @Override
    public void setInitialWorkload() throws Exception {
        for (int i = 0; i < WORKLOAD_INITIAL_SIZE * 9; i++) {
            Datacenter.$().addOneVm(false);
        }
    }

    @Override
    public void updateWorkload(int round) throws Exception {
        //172800
 
        
        if (round % mod == 0) {
            Datacenter.$().removeOneVm();
        }

    }

}
