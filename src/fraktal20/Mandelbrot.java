/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import java.awt.Dimension;
import nastroje.Fraktal;
import nastroje.VypocetFraktalu;
import nastroje.Komplex;

/**
 *
 * @author zikmuto2
 */
public class Mandelbrot implements VypocetFraktalu{
    
    @Override
    public Komplex prvni(Komplex z){
        z0 = z;
        return (z.krat(z)).plus(z);
    }

    @Override
    public Komplex dalsi(Komplex z) {
        return (z.krat(z)).plus(z0);
    }

    /** Pocatecni konstanta, pricita se ke kazdemu clenu */
    private Komplex z0; // rekl bych, ze zde vznika potiz pri paralelizaci
    
    /*@Override
    public int[][] vypoctiFraktal(Dimension rozmeryObrazku) {
        int [][] vysledek = new int[rozmeryObrazku.width][rozmeryObrazku.height];
        Nastaveni.getNastaveni().aktualizujKonstanty(rozmeryObrazku);
        for(int i = 0; i < rozmeryObrazku.width; i++)
            ven:for(int j = 0; j < rozmeryObrazku.height; j++){
                vysledek[i][j] = Nastaveni.getNastaveni().getPocetIteraci();
                Komplex z0 = new Komplex(Nastaveni.getNastaveni().x(i),Nastaveni.getNastaveni().y(j));
                Komplex z = (z0.krat(z0)).plus(z0);
                for(int k = 0; k < Nastaveni.getNastaveni().getPocetIteraci(); k++){
                    z = (z.krat(z)).plus(z0);
                    if(Nastaveni.getNastaveni().diverguje(z)){
                        vysledek[i][j] = k;
                        continue ven;
                    }
                }
            }
        return vysledek;
    }*/
    
}
