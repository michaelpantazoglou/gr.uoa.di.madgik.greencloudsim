/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim;

import static gr.uoa.di.madgik.greencloudsim.HyperCube.constructHypercubeTopology;
import gr.uoa.di.madgik.greencloudsim.experiments.CloudExperiment;
import gr.uoa.di.madgik.greencloudsim.experiments.ConvergenceExperiment;
import gr.uoa.di.madgik.greencloudsim.experiments.GCLoadBalancingExperiment;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author gavriil
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        
//        }
        CloudExperiment exp = new ConvergenceExperiment();
        CloudSimulator simulator
                = new VManCloudSimulator(exp);
        simulator.run();

        System.out.println();

        simulator.printOutEnergyConsumption();
    }
}
