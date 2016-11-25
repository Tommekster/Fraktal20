/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import javax.swing.JFrame;
import javax.swing.JProgressBar;

/**
 *
 * @author zikmuto2
 */
public class Postup extends JFrame{

    public Postup() {
        super("Postup výpočtu");
        inicializace();
    }
    
    private void inicializace(){
        setBounds(100, 100, 500, 70);
        setResizable(false);
        setLayout(null);
        add(progressBar);
        //setIconImage((new ImageIcon("nevim co sem napsat")).getImage());
        
        progressBar.setBounds(10, 10, 470, 15);
        progressBar.setValue(0);
        progressBar.setStringPainted(true);
        progressBar.setVisible(true);
    }
    
    public JProgressBar getProgressBar(){
        return progressBar;
    }
    
    private final JProgressBar progressBar = new JProgressBar(0, 100);
}
