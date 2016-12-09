/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import java.awt.Dimension;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import nastroje.Komplex;
import nastroje.VypocetFraktalu;

/**
 *
 * @author zikmuto2
 */
public class UnikovyFraktal implements nastroje.Fraktal{
    
    public UnikovyFraktal(JProgressBar bar){
        try{
            setProgressBar(bar);
            tridaFraktalu = Okno.nactiTridu();
        }
        catch(ClassNotFoundException cnf){
            JOptionPane.showMessageDialog(null, "Instalovanou třídu nelze použít", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public UnikovyFraktal(){
        this(null);
    }

    @Override
    public int[][] vypoctiFraktal(Dimension rozmery) {
        spocteno = new AtomicInteger(0); // rekl bych, ze atomic je kvuli paralelizaci
        sirka = rozmery.width;
        vyska = rozmery.height;
        vysledek = new int[sirka][vyska];
        Nastaveni.getNastaveni().aktualizujKonstanty(rozmery);
        IntStream proud = IntStream.iterate(0, /*lambda*/ n->n+1).limit(sirka);
        if(Nastaveni.getNastaveni().isParalelne()) proud.parallel();
        proud.forEach(/*lambda funkce*/n->vypoctiSloupec(n));
        return vysledek;
    }
    
    private void vypoctiSloupec(int i) {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        try{
            VypocetFraktalu vypocet = Okno.vytvorInstanci(tridaFraktalu);
            Nastaveni n = Nastaveni.getNastaveni();
            
            for(int j = 0; j < vyska; j++){
                Komplex z0 = new Komplex(n.x(i), n.y(j));
                Komplex z1 = vypocet.prvni(z0);
                vysledek[i][j] = n.getPocetIteraci();
                ven:{
                    for(int k = 0; k < n.getPocetIteraci(); k++){
                        z1 = vypocet.dalsi(z1);
                        if(n.diverguje(z1)){
                            vysledek[i][j] = k;
                            break ven;
                        }
                    }
                }
            }
            int hotovo = spocteno.incrementAndGet();
            progressBar.setValue(100 * (hotovo + 1) / sirka); // tedy progras bar ukazuje hotove sloupce
        }
        catch(ReflectiveOperationException roe){
            JOptionPane.showMessageDialog(null, "Instalovanou třídu nelze použít", "Chyba", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public final void setProgressBar(JProgressBar bar){progressBar = bar;}
    
    /** Odkaz na ukazatel postupu */
    private JProgressBar progressBar;
    
    int vyska,sirka;
    int [][] vysledek;
    AtomicInteger spocteno;
    Class<?> tridaFraktalu;
}
