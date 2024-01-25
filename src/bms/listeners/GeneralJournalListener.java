/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package bms.listeners;

import bms.classes.Account;

/**
 *
 * @author Vienji
 */
public interface GeneralJournalListener {
    void addEntryAccount(Account account);
    void updateEntryAccount(Account account, int accountIndex);
}
