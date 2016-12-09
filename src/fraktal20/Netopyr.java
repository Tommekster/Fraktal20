/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import nastroje.Komplex;
import nastroje.VypocetFraktalu;

/**
 *
 * @author zikmuto2
 */
public class Netopyr implements VypocetFraktalu {
    @Override
    public Komplex dalsi(Komplex z) {
        return (z.krat(z)).plus(z.sqrt().krat(0.566)).plus(z);
    }
}
