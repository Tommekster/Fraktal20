/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import javax.swing.*;

/**
 *
 * @author zikmuto2
 */
public class DialogNastaveni extends JDialog {
    public DialogNastaveni(JFrame rodic){
        super(rodic, "Nastaveni", true /*co znamena*/);
        inicializace(); 
        nactiNastaveni(); 
    }
    
    public final void nactiNastaveni(){
        Nastaveni n = Nastaveni.getNastaveni();
        textXod.setText(n.getXod()+""); 
        textXod.setCaretPosition(0);
        textXdo.setText(n.getXdo()+""); 
        textXdo.setCaretPosition(0);
        textYod.setText(n.getYod()+""); 
        textYod.setCaretPosition(0);
        textYdo.setText(n.getYdo()+""); 
        textYdo.setCaretPosition(0);
        
        textPocetIteraci.setText(n.getPocetIteraci()+"");
        textMezDivergence.setText(n.getMezDivergence()+"");
        
        vzorekBarvyFraktalu.setBackground(n.getBarvaFraktalu());
        vzorekBarvyPozadi.setBackground(n.getBarvaPozadi());
        
        String[] jmenaFraktalu = n.getJmenaFraktalu();
        kjpFraktal.setJmenaFraktalu(jmenaFraktalu);
        // vybereme vychozi vybrany
        int i = 0;
        for(String s: jmenaFraktalu){
            if(n.getJmenoTridyFraktalu(s).trim().equals(n.getJmenoTridyFraktalu().trim())){
                kjpFraktal.setSelectedFraktal(i);
                break;
            }
            i++;
        }
    }
    
    private void inicializace(){
        inicializaceDialogu();
        inicializacePaneluTlacitek();
        inicializacePaneluZalozek();
        inicializacePaneluBarvyAVypocet();
        inicializacePaneluRozmery();
        inicializacePaneluFraktal();
    }
    
    private void inicializaceDialogu(){
        // dialogNastaveni
        setBounds(100,100,400,300);
        setResizable(false);
        setLayout(new BorderLayout());
        add(panelZalozek, BorderLayout.CENTER);
        add(panelTlacitek, BorderLayout.SOUTH);
    }
    
    private void inicializacePaneluTlacitek(){
        // tlacitko OK
        tlacitkoOK.addActionListener((ActionEvent e)-> {
            if(nactenoUzivatelskeNastaveni()){
                DialogNastaveni.this.setVisible(false);
            }
        });
        
        // tlacitko Storno
        tlacitkoStorno.addActionListener((ActionEvent e)->{
            DialogNastaveni.this.setVisible(false);
            DialogNastaveni.this.nactiNastaveni();
        });
        
        // vlozeni na panel
        panelTlacitek.setLayout(new FlowLayout());
        panelTlacitek.add(tlacitkoOK);
        panelTlacitek.add(tlacitkoStorno);
    }
    
    private void inicializacePaneluZalozek(){
        panelZalozek.addTab("Rozmery obrázku", rozmery);
        panelZalozek.addTab("Barvy a výpočet", barvyAVypocet);
        panelZalozek.addTab("Fraktál", fraktal);
    }
    
    private void inicializacePaneluBarvyAVypocet(){
        barvyAVypocet.setLayout(new BorderLayout());
        barvyAVypocet.add(napisVypocet, BorderLayout.NORTH);
        barvyAVypocet.add(jizniPanelVypocet);
        inicializaceJiznihoPaneluBarvyAVypocet();
    }
    
    private void inicializaceJiznihoPaneluBarvyAVypocet(){
        // Jizni panel na panelu Barvy a vypocet
        jizniPanelVypocet.setPreferredSize(new Dimension(400, 180));
        
        // komponenty na jiznim panelu barev a vypoctu
        napisPocetIteraci.setBounds(10, 30, 100, 20);
        textPocetIteraci.setBounds(130, 30, 40, 20);
        napisMezDivergence.setBounds(10, 60, 100, 20);
        textMezDivergence.setBounds(130, 60, 40, 20);
        napisBarvaFraktalu.setBounds(10, 90, 100, 20);
        vzorekBarvyFraktalu.setBounds(130, 90, 20, 20);
        napisBarvaPozadi.setBounds(10, 120, 100, 20);
        vzorekBarvyPozadi.setBounds(130, 120, 20, 20);
        
        tlacitkoZmenBarvuFraktalu.setBounds(200, 90, 120, 20);
        tlacitkoZmenBarvuPozadi.setBounds(200, 120, 120, 20);
        
        // Jizni panel na panelu Barvy a vypocet
        jizniPanelVypocet.setLayout(null);
        jizniPanelVypocet.add(napisPocetIteraci);
        jizniPanelVypocet.add(textPocetIteraci);
        jizniPanelVypocet.add(napisMezDivergence);
        jizniPanelVypocet.add(textMezDivergence);
        jizniPanelVypocet.add(napisBarvaFraktalu);
        jizniPanelVypocet.add(vzorekBarvyFraktalu);
        jizniPanelVypocet.add(napisBarvaPozadi);
        jizniPanelVypocet.add(vzorekBarvyPozadi);
        jizniPanelVypocet.add(tlacitkoZmenBarvuFraktalu);
        jizniPanelVypocet.add(tlacitkoZmenBarvuPozadi);        
        
        // Tlacitko Zmen barvu fraktalu
        tlacitkoZmenBarvuFraktalu.addActionListener((ae) -> {vyberBarvu("Zvol barvu fraktalu", this.vzorekBarvyFraktalu);});
        // Tlacitko Zmen barvu pozadi
        tlacitkoZmenBarvuPozadi.addActionListener((ae) -> {vyberBarvu("Zvol barvu pozadi", this.vzorekBarvyPozadi);});
    }
    
    private void vyberBarvu(String title, JPanel vzorek){
        Color zvoleno = JColorChooser.showDialog(this, title, vzorek.getBackground());
        if(zvoleno != null){
            vzorek.setBackground(zvoleno);
        }
    }
    
    private void inicializacePaneluRozmery(){
        // Panel Rozmery
        rozmery.setLayout(new BorderLayout());
        rozmery.add(napisVysvetleni, BorderLayout.NORTH);
        rozmery.add(jizniPanelRozmery, BorderLayout.SOUTH); // Tady to funguje diky PreferredSize
        // jinak se SOUTH musi vynechat
        inicializaceJiznihoPaneluRozmery();
    }
    
    private void inicializaceJiznihoPaneluRozmery(){
        // Napisy na panelu Rozmery
        napisX.setBounds(10,10,100,20);
        napisXdo.setBounds(190, 10, 20, 20);
        napisY.setBounds(10, 60, 100, 20);
        napisYdo.setBounds(190, 60, 20, 20);
        
        // Vstupni pole na panelu Rozmery
        textXod.setBounds(120, 10, 40, 20);
        textXdo.setBounds(230, 10, 40, 20);
        textYod.setBounds(120, 60, 40, 20);
        textYdo.setBounds(230, 60, 40, 20);
        
        // Jizni panel na panelu rozmery
        jizniPanelRozmery.setLayout(null);
        jizniPanelRozmery.setPreferredSize(new Dimension(400,180));
        jizniPanelRozmery.setLayout(null);
        jizniPanelRozmery.add(napisX);
        jizniPanelRozmery.add(textXod);
        jizniPanelRozmery.add(napisXdo);
        jizniPanelRozmery.add(textXdo);
        jizniPanelRozmery.add(napisY);
        jizniPanelRozmery.add(textYod);
        jizniPanelRozmery.add(napisYdo);
        jizniPanelRozmery.add(textYdo);
    }
    
    private void inicializacePaneluFraktal(){
        // Panel Fraktal
        fraktal.setLayout(new BorderLayout());
        fraktal.add(napisFraktal, BorderLayout.NORTH);
        fraktal.add(jizniPanelFraktal);
        
        // Jizni panel na panelu fraktal
        jizniPanelFraktal.setLayout(null);
        //jizniPanelFraktal.setPreferredSize(new Dimension(400, 180));
        jizniPanelFraktal.add(napisTest);
        kjpFraktal.inicializace();
    }
    
    private boolean nactenoUzivatelskeNastaveni(){
        // Pokusi se prevest datove typy z formulare do nastaveni, 
        // pri neuspechu vrati false
        Color barvaFraktalu, barvaPozadi;
        int pocetIteraci;
        double mezDivergence, xOd, xDo, yOd, yDo;
        try{ // ve try blocku, jestli se vsechno převede tak, jak umí
            barvaFraktalu = vzorekBarvyFraktalu.getBackground();
            barvaPozadi = vzorekBarvyPozadi.getBackground();
            
            // Ziskame hodnoty .. askenujeme cisla ze stringu .. muze hodit vyjimku
            xOd = Double.parseDouble(textXod.getText());
            xDo = Double.parseDouble(textXdo.getText());
            yOd = Double.parseDouble(textYod.getText());
            yDo = Double.parseDouble(textYdo.getText());
            pocetIteraci = Integer.parseInt(textPocetIteraci.getText());
            mezDivergence = Double.parseDouble(textMezDivergence.getText());
            
            // Ted uz bychom meli mit po vyjimkach a ulozime to do nastaveni
            Nastaveni n = Nastaveni.getNastaveni();
            n.setBarvaFraktalu(barvaFraktalu);
            n.setBarvaPozadi(barvaPozadi);
            n.setXod(xOd);
            n.setXdo(xDo);
            n.setYod(yOd);
            n.setYdo(yDo);
            n.setPocetIteraci(pocetIteraci);
            n.setMezDivergence(mezDivergence);
            
            return true;
        }
        catch(NumberFormatException e){
            JOptionPane.showMessageDialog(this, e.getMessage(), "Chyba v zadaných hodnotách", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }
    
    // Panel nesouci jednotlive zalozky pro skupinu voleb
    private final JTabbedPane panelZalozek = new JTabbedPane();
    private final JPanel panelTlacitek = new JPanel();
    
    // Panely na zalozkach
    private final JPanel rozmery = new JPanel();
    private final JPanel barvyAVypocet = new JPanel();
    private final JPanel fraktal = new JPanel();
    
    // Komponenty na panelu rozmery
    private final JPanel jizniPanelRozmery = new JPanel();
    
    private final JLabel napisVysvetleni = new JLabel(" Rozsah souřadnic zobrazované oblasti");
    private final JLabel napisX = new JLabel("Souradnice x od:");
    private final JLabel napisY = new JLabel("Souradnice y od:");
    private final JLabel napisXdo = new JLabel("do:");
    private final JLabel napisYdo = new JLabel("do:");
    
    private final JTextField textXod = new JTextField();
    private final JTextField textXdo = new JTextField();
    private final JTextField textYod = new JTextField();
    private final JTextField textYdo = new JTextField();
    
    // Komponenty na panelu barvy a vypocet
    private final JPanel jizniPanelVypocet = new JPanel();
    
    private final JLabel napisVypocet = new JLabel(" Parametry výpočtu");
    private final JLabel napisPocetIteraci = new JLabel("Počet iteraí: ");
    private final JLabel napisMezDivergence = new JLabel("Mez divergence: ");
    private final JLabel napisBarvaFraktalu = new JLabel("Barva fraktálu: ");
    private final JLabel napisBarvaPozadi = new JLabel("Barva pozadí: ");
    
    private final JTextField textPocetIteraci = new JTextField();
    private final JTextField textMezDivergence = new JTextField();
    private final JPanel vzorekBarvyFraktalu = new JPanel();
    private final JPanel vzorekBarvyPozadi = new JPanel();
    
    private final JButton tlacitkoZmenBarvuFraktalu = new JButton("Změň...");
    private final JButton tlacitkoZmenBarvuPozadi = new JButton("Změň...");
    
    // Komponenty na panelu fraktal
    private final JPanel jizniPanelFraktal = new JPanel();
    
    private final JLabel napisFraktal = new JLabel("  Fraktal k vykreslení");
    /* Kdyz je to bez "navestich" tak co je to defaultne? */
    private final JLabel napisTest = new JLabel("TEST");
    KomponentyJiznihoPaneluFraktal kjpFraktal = new KomponentyJiznihoPaneluFraktal(jizniPanelFraktal, this);
    
    
    // Tlacitka pro cely dialog
    private final JButton tlacitkoOK = new JButton("   OK   ");
    private final JButton tlacitkoStorno = new JButton("Storno");
}

class KomponentyJiznihoPaneluFraktal{
    /** 
     * Konstruktor ulozi predane odkazy na panel a dialog
     * @param panel Panel, s nímž instance pracuje
     * @param dialog Dialog, v němž je panel
     */
    public KomponentyJiznihoPaneluFraktal(JPanel panel, DialogNastaveni dialog){
        this.panel = panel;
        this.dialog = dialog;
    }
    
    /**
     * Vrati retezec se jmenem fraktalu, ktery uzivatel vybral
     * @return jmeno fraktalu
     */
    public String vybranáHodnotaVSeznamu()
    {
        return this.seznamNabidkaFraktalu.getSelectedValue();
    }
    
    public void inicializace(){
        nastavVlastnostiKomponent();
        //nastavObsluhuUdalosti();
        vlozKomponentyNaPanel();
    }
    
    private void nastavVlastnostiKomponent(){
        napisVyberFraktal.setBounds(10, 0, 300, 20);
        nabidkaFraktalu.setBounds(10, 20, 200, 60);
    }
    
    private void nastavObsluhuUdalosti(){
        // rezerva
    }
    
    private void vlozKomponentyNaPanel() {
        panel.add(napisVyberFraktal);
        seznamNabidkaFraktalu.setVisibleRowCount(5);
        panel.add(nabidkaFraktalu);        
    }
    
    public void setJmenaFraktalu(String[] jmena){
        seznamNabidkaFraktalu.setListData(jmena);
    }
    
    public void setSelectedFraktal(int i){
        seznamNabidkaFraktalu.setSelectedIndex(i);
    }
    
    /** Java docs pro panel */
    private final JPanel panel;
    /** Java docs pro dialog */
    private final DialogNastaveni dialog;
    
    private final JLabel napisVyberFraktal = new JLabel("Vyber fraktal k nakresleni");
    private final JList<String> seznamNabidkaFraktalu = new JList<>();
    private final JScrollPane nabidkaFraktalu = new JScrollPane(seznamNabidkaFraktalu);
   
}