/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim;

import static gr.uoa.di.madgik.greencloudsim.IdentityGenerator.newComputeNodeId;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gavriil
 */
public class TestMain {

    public static void main(String[] args) throws Exception {
//        
////        }
//        CloudExperiment exp = new ConvergenceExperiment();
//        CloudSimulator simulator
//                = new VManCloudSimulator(exp);
//        simulator.run();
//
//        System.out.println();
//
//        simulator.printOutEnergyConsumption();
        List<ComputeNode> nodes = new ArrayList<>();

        Newscast an = new Newscast(2);
        Newscast bn = new Newscast(2);

        ComputeNode a = new ComputeNode(newComputeNodeId(), an);
        ComputeNode b = new ComputeNode(newComputeNodeId(), bn);
        an.init(a);
        bn.init(b);
        a.switchOn();
        b.switchOn();
        nodes.add(a);
        nodes.add(b);
        an.addNeighbor(b);
        an.refreshNeighborsNames();
        bn.refreshNeighborsNames();
//        an.nextCycle(a);
//        bn.nextCycle(b);

        Datacenter.$().init(nodes);

        a.add(Util.newVm());
        a.add(Util.newVm());
        a.add(Util.newVm());
     
       
        a.add(Util.newVm());
        b.add(Util.newVm());
     
        System.out.println(" a : " + a.getCurrentPowerConsumption() + " / " + a.getMaxPowerConsumptionThreshold());
        System.out.println(" b : " + b.getCurrentPowerConsumption() + " / " + b.getMaxPowerConsumptionThreshold());

        Datacenter.$().balanceVMAN(a.getId());

        System.out.println(!a.isSwitchedOff() + " a : " + a.getCurrentPowerConsumption() + " / " + a.getMaxPowerConsumptionThreshold() + " " + a.getWorkload().size());
        System.out.println(!b.isSwitchedOff() + " b : " + b.getCurrentPowerConsumption() + " / " + b.getMaxPowerConsumptionThreshold() + " " + b.getWorkload().size());
        an.printNeighbors();
        bn.printNeighbors();
        a.getWorkload().size();

    }
}
