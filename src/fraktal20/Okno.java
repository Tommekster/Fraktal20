/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import nastroje.Fraktal;
import nastroje.VypocetFraktalu;

/* Domaci ukol
Pridat rozhrani se dvemi metody 
 > jak se pocita prvni clen
 > jak se pocitaji dalsi cleny
>> toto v-implementovat mezi Mandelbrodta a vypocet fraktalu

Zjistit proc se cosi kdesi obrazek uklada kamsi. 

Stahnout si Julii od Viria
*/

/**
 *
 * @author zikmuto2
 */
public class Okno extends JFrame{
    
    public Okno(){
        super();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializace();
    }
    
    public static Class<?> nactiTridu() throws ClassNotFoundException{
        return Class.forName(Nastaveni.getNastaveni().getJmenoTridyFraktalu());
    }
    
    public static VypocetFraktalu vytvorInstanci(Class<?> trida) 
            throws InstantiationException, IllegalAccessException
    {
        return (VypocetFraktalu) trida.newInstance();
    }
    
    private void inicializace(){
        // Okno
        Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((obrazovka.width - 400)/2, (obrazovka.height - 300)/2, 400, 300);
        setTitle("Fraktalnik 2016");
        setLayout(new BorderLayout()); 
        add(panelSObrazkem); 
        setJMenuBar(nabidka);
        
        // Nabidka
        nabidka.add(obrazekMenu);
        nabidka.add(nastaveniMenu);
        nabidka.add(napovedaMenu);
        
        // Obrazek menu
        obrazekMenu.add(nakresliItem); 
        obrazekMenu.add(novyItem);
        obrazekMenu.add(ulozItem);
        obrazekMenu.addSeparator();
        obrazekMenu.add(konecItem);
        
        // Nastaveni menu
        nastaveniMenu.add(upravitItem);
        nastaveniMenu.add(obnovitItem);
        
        // Napoveda menu
        napovedaMenu.add(aboutItem);
        
        // Obsluha nabídky
        konecItem.addActionListener((e)->{System.exit(0);});
        aboutItem.addActionListener(this::oProgamu);
        nakresliItem.addActionListener(this::nakresli); 
        obnovitItem.addActionListener((e)->{Nastaveni.getNastaveni().obnovVychoziNastaveni();});
        upravitItem.addActionListener(this::otevriDialogNastaveni); 
        ulozItem.addActionListener(this::ulozObrazek); 
        
    }
    
    private void nakresli(ActionEvent e){
        //try{
        Dimension rozmeryOkna = this.getContentPane().getSize();
        Fraktal fraktal = new UnikovyFraktal();
        //nastroje.VypocetFraktalu fraktal = new Mandelbrot();
        obrazek = new BufferedImage(rozmeryOkna.width, rozmeryOkna.height, BufferedImage.TYPE_3BYTE_BGR);
        int poctyIteraci[][] = fraktal.vypoctiFraktal(rozmeryOkna);
        for(int i = 0; i < rozmeryOkna.width; i++)
            for(int j = 0; j < rozmeryOkna.height; j++){
                if(poctyIteraci[i][j] < Nastaveni.getNastaveni().getPocetIteraci()){
                    obrazek.setRGB(i,j,Nastaveni.getNastaveni().getBarvaPozadi().getRGB());
                }else{
                    obrazek.setRGB(i,j,Nastaveni.getNastaveni().getBarvaFraktalu().getRGB());
                }
            }
        /* Proc volam Okno.this? */
        Okno.this.repaint();
    }
    
    private void ulozObrazek(ActionEvent ae){
        final FileNameExtensionFilter filtrBMP = new FileNameExtensionFilter("Bitové mapy", "bmp");
        final FileNameExtensionFilter filtrPNG = new FileNameExtensionFilter("Obrázek PNG", "png");
        try{
            final JFileChooser jfc = new JFileChooser();
            jfc.setCurrentDirectory(new File("."));
            jfc.setDialogTitle("Ulož obrázek jako");
            jfc.setDialogType(JFileChooser.SAVE_DIALOG);
            jfc.setSelectedFile(new File("Obrazek.png"));
            jfc.setFileFilter(filtrPNG);
            jfc.addChoosableFileFilter(filtrBMP);
            jfc.addPropertyChangeListener((pce)->{zmenaPripony(pce,jfc,filtrPNG,filtrBMP);});
            if(jfc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION){
                String format = "PNG";
                if(jfc.getFileFilter().equals(filtrBMP)) format = "BMP";
                
                File vybranySoubor = new File(jfc.getCurrentDirectory().getAbsolutePath(), jfc.getSelectedFile().getName());
                ImageIO.write(obrazek, format, vybranySoubor);
            }
        }catch(IOException ex){
            System.out.println("Nepodařilo se zapsat soubor. ");
        }
    }
    
    private void zmenaPripony(PropertyChangeEvent pce, JFileChooser jfc, FileNameExtensionFilter fpng, FileNameExtensionFilter fbmp){
        if(pce.getPropertyName().equals(JFileChooser.SELECTED_FILE_CHANGED_PROPERTY)){
            String pripona;
            if(jfc.getFileFilter().equals(fpng)) pripona = ".png";
            else if(jfc.getFileFilter().equals(fbmp)) pripona = ".bmp";
            else return;
            
            File selectedFile = jfc.getSelectedFile();
            if(selectedFile == null) jfc.setSelectedFile(new File("Obrazek" + pripona));
            else{
                String jmeno = jfc.getSelectedFile().getName();
                int i = jmeno.lastIndexOf(".");
                jmeno = jmeno.substring(0,i) + pripona;
                jfc.setSelectedFile(new File(jmeno));
            }
                
        }
    }
    
    private void oProgamu(ActionEvent e){
        JOptionPane.showMessageDialog(null, 
                "Program pro vypocet a kresleni fraktalu\n(c) Piskvorky sw 2016", 
                "Fraktalnik 0.9", JOptionPane.INFORMATION_MESSAGE, 
                new ImageIcon(ikony.Pristup.class.getResource("strom.png")));
    }
    
    public boolean isNakresleno(){return obrazek != null;}
    public BufferedImage getObrazekFraktalu(){return obrazek;}
    public void otevriDialogNastaveni(ActionEvent ae){
        dialogNastaveni.setVisible(true);
    }
    
    private final JMenuBar nabidka = new JMenuBar();
    
    private final JMenu obrazekMenu = new JMenu("Obrázek");
    private final JMenuItem nakresliItem = new JMenuItem("Nakresli");
    private final JMenuItem ulozItem = new JMenuItem("Uložit ...");
    private final JMenuItem novyItem = new JMenuItem("Nový");
    private final JMenuItem konecItem = new JMenuItem("Šmitec");
    
    private final JMenu nastaveniMenu = new JMenu("Nastaveni");
    private final JMenuItem upravitItem = new JMenuItem("Upravit ...");
    private final JMenuItem obnovitItem = new JMenuItem("Obnovit výchozí");
    
    private final JMenu napovedaMenu = new JMenu("?");
    private final JMenuItem aboutItem = new JMenuItem("O programu");
    
    private Kresleni kresleni;
    private BufferedImage obrazek = null;
    private final KresliciPanel panelSObrazkem = new KresliciPanel(this);
    
    private final DialogNastaveni dialogNastaveni = new DialogNastaveni(this); 
    
    private int poctyIteraci[][]; // pouzije se v jednotlivych thredech, proto je jako field
    
    /** Okno s progrssbarem */
    private final Postup postup = new Postup();

    // Nestaticka vnitrni trida
    class Vypocet extends SwingWorker<Void, Void>{ // alternativou je Executor service

        public Vypocet(Dimension rozmerObrazku, Fraktal fraktal) {
            this.rozmer = rozmerObrazku;
            this.fraktal = fraktal;
        }

        @Override
        protected Void doInBackground() throws Exception {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        }

        @Override
        public void done(){ // zavola se v tomto threadu; ne v pozadi
            obrazek = Okno.this.kresleni.vybarvi(rozmer); // musi byt nestaticka vnitrni trida
            repaint(); // protoze Okno je potomek JFrame
            
            // obsluha nabidky a uprava GUI - za/odblokovani nabidky
            Okno.this.postup.setVisible(false);
            Okno.this.postup.getProgressBar().setValue(0);
            // TODO
        }

        private final Dimension rozmer;
        private final Fraktal fraktal; 
    }
}

class KresliciPanel extends JPanel{
    public KresliciPanel(Okno okno){
        // asi konstruktor bez parametru
        this.okno = okno;
    }
    
    @Override
    public void paintComponent(Graphics g){
        super.paintComponent(g);
        setBackground(Nastaveni.getNastaveni().getBarvaPozadi());
        if(okno.isNakresleno()){
            g.drawImage(okno.getObrazekFraktalu(), 0, 0, this);
        }
    }
    
    private final Okno okno;
}