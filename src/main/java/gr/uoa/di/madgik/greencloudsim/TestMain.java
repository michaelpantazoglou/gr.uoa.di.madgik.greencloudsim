/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim;

import gr.uoa.di.madgik.greencloudsim.experiments.ConvergenceExperiment;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author gavriil
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
        List<ComputeNode> compute_nodes
                //= 
                //                constructHypercubeTopology(
                //                Environment.$().getHypercubeDimension() - 1,
                //                Environment.$().getUseRandomIds());
                = Newscast.constructNewsCastTopology(
                        Environment.$().getHypercubeDimension() - 1,
                        Environment.$().getUseRandomIds());
        for (ComputeNode node : compute_nodes) {
            node.switchOn();
        }
        int[] counts = new int[compute_nodes.size()];
        for (int i = 0; i < 10; ++i) {
            Arrays.fill(counts, 0);
            SimulationTime.timePlus();
            for (ComputeNode node : compute_nodes) {
                node.topologyUpdate();
            }
            for (ComputeNode node : compute_nodes) {

                for (String s : node.getAllNeighbors()) {
//                    System.out.print(" " + s);
                    counts[Integer.parseInt(s)]++;
                }
            }
            for (int j = 0; j < counts.length; ++j) {
                System.out.println("[" + j + "] :" + counts[j]);
            }
        }
        for (ComputeNode node : compute_nodes) {
            node.switchOff();
        }
//        CloudSimulator simulator
//                = new ConvergenceExperiment(compute_nodes);
////                =new GCLoadBalancingExperiment(compute_nodes);
////        =new RRLoadBalancingExperiment(compute_nodes);
////=                new ElasticityExperiment(compute_nodes);
//        simulator.run();
//
//        System.out.println();
//
//        simulator.printOutEnergyConsumption();
    }
}
