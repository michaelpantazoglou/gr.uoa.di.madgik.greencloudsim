/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim;

import gr.uoa.di.madgik.greencloudsim.experiments.CloudExperiment;
import java.util.ArrayList;

public class VManCloudSimulator extends CloudSimulator {

    public VManCloudSimulator(CloudExperiment exp) {
        super(Newscast.constructNewsCastTopology(
                Environment.$().getNewsCastDimension(),
                Environment.$().getUseRandomIds()), exp);
    }

    @Override
    protected LBResult performLoadBalancing() {
        LBResult result = new LBResult();
        for (String nodeId : Datacenter.$().getActiveComputeNodes(true)) {
            LBResult temp = Datacenter.$().balanceVMAN(nodeId);
            result.merge(temp);

        }
        int switchoffs = Datacenter.$().switchOffIdleNodes();
        result.setSwitchOffs(result.getSwitchOffs() + switchoffs);

        return result;
    }

}
