package gr.uoa.di.madgik.greencloudsim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Implements the workload of a compute node. The workload consists
 * of zero or more Virtual Machine (VM) instances.
 *
 * Created by michael on 17/11/14.
 */
public class Workload
{
    /** the actual workload content*/
    private HashMap<String, VirtualMachineInstance> content;

    /** the power consumption of this workload */
    private double powerConsumption = 0d;

    /**
     * Constructor.
     */
    public Workload()
    {
        content = new HashMap<>();
    }

    /**
     * Adds the specified VM to this workload.
     *
     * @param vm
     */
    public final void add(VirtualMachineInstance vm)
    {
        content.put(vm.getId(), vm);
        powerConsumption += vm.getPowerConsumption();
    }

    /**
     * Removes the specified VM from this workload.
     *
     * @param vm
     */
    public final void remove(VirtualMachineInstance vm)
    {
        content.remove(vm.getId());
        powerConsumption -= vm.getPowerConsumption();
    }

    /**
     * Removes the VM with the specified id from this workload.
     *
     * @param vmId
     */
    public final void remove(String vmId)
    {
        VirtualMachineInstance vm = content.remove(vmId);
        powerConsumption -= vm.getPowerConsumption();
    }

    /**
     * Removes the VM at the specified position from this workload.
     *
     * @param pos
     */
    public final void removeAt(int pos)
    {
        List<String> l = new ArrayList<>();
        for (String s : content.keySet())
        {
            l.add(s);
        }
        String s = l.get(pos);
        VirtualMachineInstance vm = content.remove(s);
        powerConsumption -= vm.getPowerConsumption();
    }

    /**
     * Gets the VM at the specified position.
     *
     * @param pos
     * @return
     */
    public final VirtualMachineInstance getAt(int pos)
    {
        List<String> l = new ArrayList<>();
        for (String s : content.keySet())
        {
            l.add(s);
        }
        String s = l.get(pos);
        return content.get(s);
    }

    /**
     * Gets the size of this workload, i.e. the number of VMs.
     *
     * @return
     */
    public final int size()
    {
        return content.size();
    }

    /**
     * Calculates and returns the power consumption of this workload.
     *
     * @return
     */
    public final double getPowerConsumption()
    {
//        double powerConsumption = 0d;
//
//        for (VirtualMachineInstance vm : content.values())
//        {
//            powerConsumption += vm.getPowerConsumption();
//        }

        return powerConsumption;
    }

    /**
     * Clears this workload.
     */
    public void clear()
    {
        content.clear();
        powerConsumption = 0; 
    }
}
