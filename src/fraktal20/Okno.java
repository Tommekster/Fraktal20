/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fraktal20;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.EnumMap;
import java.util.logging.Level;
import java.util.logging.Logger;
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
        //this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
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
        nactiNastaveni();
        
        inicializaceOkna();
        inicializaceNabidky();
        inicializaceKresleni();
        
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e){
                ulozNastaveni();
                System.exit(0);
            }
        });
    }

    private void inicializaceOkna() throws HeadlessException {
        // Okno
        Dimension obrazovka = Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((obrazovka.width - 400)/2, (obrazovka.height - 300)/2, 400, 300);
        setTitle("Fraktalnik 2016");
        setLayout(new BorderLayout());
        add(panelSObrazkem); 
        setJMenuBar(nabidka);
    }

    private void inicializaceNabidky() {
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
        
        inicializaceListeneruNabidky();
    }

    private void inicializaceListeneruNabidky() {
        // Obsluha nabídky
        konecItem.addActionListener((e)->{System.exit(0);});
        aboutItem.addActionListener(this::oProgamu);
        nakresliItem.addActionListener(this::nakresli); 
        obnovitItem.addActionListener((e)->{Nastaveni.getNastaveni().obnovVychoziNastaveni();});
        upravitItem.addActionListener(this::otevriDialogNastaveni);
        ulozItem.addActionListener(this::ulozObrazek);
    }
    
    private void inicializaceKresleni() {
        metodyKresleni.put(ZpusobKresleni.OSTRE_OKRAJE, this::nakresliObratekOstre);
        metodyKresleni.put(ZpusobKresleni.PLYNULY_PRECHOD, this::nakresliObrazekPlynule);
        kresleni = metodyKresleni.get(Nastaveni.getNastaveni().getZpusobKresleni());
    }
    
    private void nakresli(ActionEvent e){
        kresleni = metodyKresleni.get(Nastaveni.getNastaveni().getZpusobKresleni());
        try{
            Dimension rozmeryOkna = panelSObrazkem.getSize();
            Fraktal fraktal = new UnikovyFraktal(postup.getProgressBar());
            // TODO zakaz kresleni v nabidce
            Vypocet /*vnorena trida*/ vypocet = new Vypocet(rozmeryOkna, fraktal);
            vypocet.execute(); // to je podedena metoda z SwingWorker .. bezi na pozadi
        }catch(ClassCastException ec){
            JOptionPane.showMessageDialog(this, "Jakysi problem se tridou: " + 
                    Nastaveni.getNastaveni().getJmenoTridyFraktalu() + "\n"
                    , "Upozorneni", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private BufferedImage nakresliObrazekPlynule(Dimension rozmery){
        Nastaveni n = Nastaveni.getNastaveni();
        int sirka = rozmery.width;
        int vyska = rozmery.height;
        Color startC = n.getBarvaFraktalu();
        int start[] = {startC.getRed(),startC.getGreen(),startC.getBlue()};
        Color cilC = n.getBarvaPozadi();
        int cil[] = {cilC.getRed(),cilC.getGreen(),cilC.getBlue()};
        
        
        BufferedImage obrazek = new BufferedImage(sirka, vyska, BufferedImage.TYPE_3BYTE_BGR);
        
        out: for(int i = 0; i < sirka; i++)
            for(int j = 0; j < vyska; j++){
                if(poctyIteraci[i][j] >= n.getPocetIteraci())
                    obrazek.setRGB(i, j, n.getBarvaFraktalu().getRGB());
                else{
                    double x = ((double)(n.getPocetIteraci() - poctyIteraci[i][j]))/(double)n.getPocetIteraci();
                    
                    int rgb[] = new int[3];
                    for(int k = 0; k < 3; k++){
                        rgb[k] = (int)(cil[k]*x + start[k]*(1-x));
                    }
                    
                    obrazek.setRGB(i, j, new Color(rgb[0],rgb[1],rgb[2]).getRGB());
                }
            }
        
        return obrazek;
    }
    
    private BufferedImage nakresliObratekOstre(Dimension rozmery){
        Nastaveni n = Nastaveni.getNastaveni();
        int sirka = rozmery.width;
        int vyska = rozmery.height;
        
        BufferedImage obrazek = new BufferedImage(sirka, vyska, BufferedImage.TYPE_3BYTE_BGR);
        for(int i = 0; i < sirka; i++)
            for(int j = 0; j < vyska; j++) 
                obrazek.setRGB(i,j, (poctyIteraci[i][j] < n.getPocetIteraci()) ? n.getBarvaPozadi().getRGB() : n.getBarvaFraktalu().getRGB());
        
        return obrazek;
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
                
                File vybranySoubor = new File(jfc.getCurrentDirectory()
                        .getAbsolutePath(), jfc.getSelectedFile().getName());
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
    
    private void ulozNastaveni() {
        try(
                FileOutputStream fileOut = new FileOutputStream("settings.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
        ){
            out.writeObject(Nastaveni.getNastaveni());
            //out.close();
            //fileOut.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    private void nactiNastaveni() {
        try(
                FileInputStream fileIn = new FileInputStream("settings.ser");
                ObjectInputStream in = new ObjectInputStream(fileIn);
        ){
            Nastaveni saved = (Nastaveni) in.readObject();
            Nastaveni.loadNastaveni(saved);
            //in.close();
            //fileIn.close();
        } catch (FileNotFoundException ex) {
            Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Okno.class.getName()).log(Level.SEVERE, null, ex);
        }
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
    private final EnumMap<ZpusobKresleni, Kresleni> metodyKresleni = new EnumMap<>(ZpusobKresleni.class);
    private BufferedImage obrazek = null;
    private final KresliciPanel panelSObrazkem = new KresliciPanel(this);
    
    private final DialogNastaveni dialogNastaveni = new DialogNastaveni(this); 
    
    private int poctyIteraci[][]; // pouzije se v jednotlivych thredech, proto je jako field
    
    /** Okno s progressbarem */
    private final Postup postup = new Postup();

    // Nestaticka vnitrni trida
    class Vypocet extends SwingWorker<Void, Void>{ // alternativou je Executor service

        public Vypocet(Dimension rozmerObrazku, Fraktal fraktal) {
            this.rozmer = rozmerObrazku;
            this.fraktal = fraktal;
        }

        @Override
        protected Void doInBackground() throws Exception {
            // setVisible je blocking? prijde mi divne, ze patri do background
            Okno.this.postup.setVisible(true); 
            poctyIteraci = fraktal.vypoctiFraktal(rozmer);
            return null;
        }

        @Override
        public void done(){ // zavola se v tomto threadu; ne v pozadi
            /* Okno.this. */obrazek = Okno.this.kresleni.vybarvi(rozmer); // musi byt nestaticka vnitrni trida
            repaint(); // protoze Okno je potomek JFrame
            
            // obsluha nabidky a uprava GUI - za/odblokovani nabidky
            Okno.this.postup.setVisible(false);
            Okno.this.postup.getProgressBar().setValue(0);
            // TODO v budoucnu, at bude asi panel
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