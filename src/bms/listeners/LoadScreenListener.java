/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bms.listeners;

/**
 *
 * @author Vienji
 */
public interface LoadScreenListener {
    void updateLoadingBar(int i);
    void updateLoadingModule(String module);
    void updateLoadingPercentage(int i);
}
