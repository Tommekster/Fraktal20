/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import java.awt.Color;
import java.awt.Dimension;
import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import nastroje.Komplex;

/**
 *
 * @author zikmuto2
 */
public class Nastaveni implements Serializable{
    // Singleton class
    private static Nastaveni nastaveni;
    private double x_od;
    private double x_do;
    private double y_od;
    private double y_do;
    private int pocetIteraci;
    private double mezDivergence;
    private Color barvaFraktalu;
    private Color barvaPozadi;
    private final String[][] jmenaFraktalu;
    private String jmenoTridyFraktalu;
    private boolean paralelne;
    private ZpusobKresleni zpusobKresleni;
    
    private final Map<String,String> seznamFraktalu;
    
    private double ax, bx, ay, by;
    
    private Nastaveni(){
        x_od = -2;
        x_do = 2;
        y_od = -2;
        y_do = 2;
        pocetIteraci = 500;
        mezDivergence = 2;
        barvaFraktalu = Color.BLUE;
        barvaPozadi = Color.white;
        paralelne = false;
        zpusobKresleni = ZpusobKresleni.OSTRE_OKRAJE;
        
        String[][] jf = {
            {"Mandelbrot", "fraktal20.Mandelbrot"},
            {"Julia", "fraktal20.Julia"},
            {"Netopyr", "fraktal20.Netopyr"}
        };
        jmenaFraktalu = jf;
        jmenoTridyFraktalu = jmenaFraktalu[0][1];
        seznamFraktalu = Collections.synchronizedMap(new HashMap<>());
        for (String[] fraktal : jmenaFraktalu) {
            addFractal2List(fraktal[0], fraktal[1]);
        }
    }
    
    public static Nastaveni getNastaveni(){
        if(nastaveni == null){
            nastaveni = new Nastaveni();
        }
        return nastaveni;
    }
    
    public static void loadNastaveni(Nastaveni n){
        nastaveni = n;
    }
    
    public void aktualizujKonstanty(Dimension rozmerObrazku){
        ax = (x_do - x_od)/(double)rozmerObrazku.width;
        bx = x_od;
        ay = (y_od - y_do)/(double)rozmerObrazku.height;
        by = y_do;
    }
    
    public double getXod(){return x_od;}
    public double getXdo(){return x_do;}
    public double getYod(){return y_od;}
    public double getYdo(){return y_do;}
    public int getPocetIteraci(){return pocetIteraci;}
    public double getMezDivergence(){return mezDivergence;}
    public Color getBarvaPozadi(){return barvaPozadi;}
    public Color getBarvaFraktalu(){return barvaFraktalu;}
    public boolean isParalelne(){return paralelne;}
    public ZpusobKresleni getZpusobKresleni(){return zpusobKresleni;} 
    public String[] getJmenaFraktalu(){return seznamFraktalu.keySet().toArray(new String[0]);}
    public String getJmenoTridyFraktalu(String jmenoFraktalu){return seznamFraktalu.get(jmenoFraktalu);}
    public String getJmenoTridyFraktalu(){return jmenoTridyFraktalu;}
    
    public void setBarvaFraktalu(Color b){barvaFraktalu = b;}
    public void setBarvaPozadi(Color b){barvaPozadi = b;}
    public void setXod(double d){x_od = d;}
    public void setXdo(double d){x_do = d;}
    public void setYod(double d){y_od = d;}
    public void setYdo(double d){y_do = d;}
    public void setMezDivergence(double d){mezDivergence = d;}
    public void setPocetIteraci(int i){pocetIteraci = i;}
    public void setZpusobKresleni(ZpusobKresleni kresleni){zpusobKresleni=kresleni;}
    public void setParalelne(boolean b){paralelne = b;}
    public final void addFractal2List(String nameFractal, String nameClass){seznamFraktalu.put(nameFractal,nameClass);}
    public void setJmenoTridyFraktalu(String tridaFraktalu){jmenoTridyFraktalu = tridaFraktalu;}
    
    public double x(int _x){ // Re
        return ax*_x + bx;
    }
    public double y(int _y){ // Im
        return ay*_y + by;
    }
    
    public boolean diverguje(Komplex z){
        if(z.re() > mezDivergence) return true;
        return z.im() > mezDivergence;
    }
    
    public void obnovVychoziNastaveni(){
        nastaveni = new Nastaveni();
    }
}


/* ? Transakcni zpracovani

*/
