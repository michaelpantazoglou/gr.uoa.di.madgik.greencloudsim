/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gr.uoa.di.madgik.greencloudsim;

import java.util.List;

/**
 *
 * @author Skulos
 */
public abstract class NetworkInterface {

    /**
     * neighbors' ids in a list
     */
    protected List<String> listNeighbors;

    /**
     *
     * @return all neighbors' ids in a list
     */
    public List<String> getListOfNeighbors() {
        return listNeighbors;
    }
    public abstract int getNeighborsMaxSize();
    public boolean addNeighbor(ComputeNode node) {
        return false;
    }

    public boolean needReinitiation() {
        return false;
    }

    /**
     * update the topology neighbors if needed
     */
    public abstract void update();

    public void reinitiate(List<String> activeComputeNodes) {

    }

}
