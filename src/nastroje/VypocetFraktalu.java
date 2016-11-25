/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package nastroje;

/**
 *
 * @author zikmuto2
 */
public interface VypocetFraktalu {
    /**
     * Vypocte prvni clen posloupnosti definujici unikovy fraktal.
     * @param z Vychozi bod poslopnosti, bod v komplexni rovine
     * @return prvni clen posloupnosti unkoveho fraktalu. Implicitni implementace 
     * vraci totez co metoda <tt>dalsi()</tt>
     */
    default Komplex prvni(Komplex z){
        return dalsi(z);
    }
    
    /**
     * Vypocte dalsi clen posloupnosti
     * @param z predchozi clen posloupnosti
     * @return hodnota dalsiho clenu posloupnosti
     */
    Komplex dalsi(Komplex z);
}

