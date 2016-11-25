/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import javax.swing.JOptionPane;

/**
 *
 * @author zikmuto2
 */
public class Neimplementovano implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        JOptionPane.showMessageDialog(null, 
                "Tato možnost ještě není implementována.\n"+e.getMessage(), 
                "Upozornění", JOptionPane.INFORMATION_MESSAGE);
        JOptionPane.showMessageDialog(null, 
                e.getStackTrace(), 
                "Stack", JOptionPane.INFORMATION_MESSAGE);
    }
    
}
