/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim;

/**
 *
 * @author Skulos
 */
public class SimulationTime {

    private static int time = 0;
// --------------------------------------------------------------------

    public static void timePlus() {
        time++;
    }
// --------------------------------------------------------------------

    public static int getTime() {
        return time;
    }
}
