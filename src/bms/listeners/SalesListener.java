/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bms.listeners;

import bms.classes.Item;

/**
 *
 * @author Vienji
 */
public interface SalesListener {
    void addItem(Item item);
    void updateItem(int index, Item item);
    void updateCustomerTable();
}
