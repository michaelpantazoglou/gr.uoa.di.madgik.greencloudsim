/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim.experiments;

/**
 *
 * @author gavriil
 */
public abstract class CloudExperiment {

    protected boolean performLoadBalancing = true;
    protected String name = "";

    public String getName() {
        return name;
    }

    public abstract void setInitialWorkload() throws Exception;

    public boolean isPerformLoadBalancing() {
        return performLoadBalancing;
    }

    /**
     * Updates the cloud's workload by either adding or removing VM instances
     * to/from the cloud's compute nodes.
     *
     * @param round
     * @throws Exception
     */
    public abstract void updateWorkload(int round) throws Exception;
}
