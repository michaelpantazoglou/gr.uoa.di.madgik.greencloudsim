package gr.uoa.di.madgik.greencloudsim;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides a number of utility methods.
 *
 * Created by michael on 17/11/14.
 */
public class Util {

    /**
     * Creates and returns a new VirtualMachineInstance. Depending on whether
     * the environment has been set to use random IDs or not, the new
     * VirtualMachineInstance is given either a random UUID or a sequential new
     * VM instance ID.
     *
     * @return
     */
    public static VirtualMachineInstance newVm() {
        VirtualMachineInstance vm = new VirtualMachineInstance(
                Environment.$().getUseRandomIds() ? IdentityGenerator.newRandomUUID() : IdentityGenerator.newVmInstanceId());
        return vm;
    }

    /**
     * Loads the contents of file "random.txt" into an int[] array. This array
     * is expected to be used to simulate random increases or decreases of
     * workload ( 1 = increase, 0 = decrease ).
     *
     * @return
     * @throws Exception
     */
    public static int[] loadRandomSequenceFromFile() throws Exception {
        int[] result = new int[Environment.$().getNumberOfSimulationRounds()];
        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader("random.txt"));
            String line;
            int i = 0;
            while ((line = br.readLine()) != null) {
                result[i++] = Integer.valueOf(line);
            }
            return result;
        } finally {
            if (null != br) {
                br.close();
            }
        }
    }
}
