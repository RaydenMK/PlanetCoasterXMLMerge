//IMPORTS
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.Color;
import java.awt.Container;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;


public class Window extends JFrame {

    private JLabel labelOldFile;
    private JLabel labelNewFile;
    private JButton elaborate;

    private String path_old_file, path_new_file;
    private boolean done1=false,done2=false;
    private JLabel result;

    private Window(){
        Container background;
        JButton selectOldFile;
        JButton selectNewFile;

        print_log("Program started!");
        print_log("Creating the Window...!");
        setSize(500,500);
        setTitle("PlanetCoaster Translation");
        background=this.getContentPane();
        ImageIcon img = new ImageIcon("icon.bmp");
        setIconImage(img.getImage());
        //setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("icon.bmp"))); //setting the icon
        //----------------------------------------------------------------
        JPanel total_panel=new JPanel();
        total_panel.setLayout(new GridLayout(3,2));
        //----------------------------------------------------------------
        labelOldFile=new JLabel("OLD File name");
        selectOldFile=new JButton("Select Old File");
        selectOldFile.addActionListener(new OldFilePath());
        JPanel oldL=new JPanel();
        oldL.add(labelOldFile);
        JPanel oldS=new JPanel();
        oldS.add(selectOldFile);
        oldL.setBorder(new TitledBorder("Old XML File"));
        oldS.setBorder(new TitledBorder("Old XML File"));
        total_panel.add(oldL);
        total_panel.add(oldS);
        //----------------------------------------------------------------
        labelNewFile=new JLabel("NEW File name");
        selectNewFile=new JButton("Select New File");
        selectNewFile.addActionListener(new NewFilePath());
        JPanel newL=new JPanel();
        newL.add(labelNewFile);
        JPanel newS=new JPanel();
        newS.add(selectNewFile);
        newL.setBorder(new TitledBorder("New XML File"));
        newS.setBorder(new TitledBorder("New XML File"));
        total_panel.add(newL);
        total_panel.add(newS);
        //----------------------------------------------------------------
        elaborate=new JButton("Process");
        elaborate.addActionListener(new Elaborate_Files());

        JPanel elaborate_panel = new JPanel();
        elaborate_panel.add(elaborate);
        JPanel wrapperPanel1 = new JPanel(new GridBagLayout());
        wrapperPanel1.add(elaborate_panel);
        wrapperPanel1.setBorder(BorderFactory.createLineBorder(Color.black));
        total_panel.add(wrapperPanel1);

        result=new JLabel("Ready");
        JPanel state = new JPanel();
        state.add(result);
        state.setBorder(new TitledBorder("Current State"));
        total_panel.add(state);
        //----------------------------------------------------------------
        background.add(total_panel);
        print_log("Window Fully Created!");
        setResizable(false);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setVisible(true);
    }

    //---------------------------------------------------------------------------------------
    /**Listener for old file button*/
    class OldFilePath implements ActionListener {
        public void actionPerformed(ActionEvent e){
            try{
                JFileChooser fileChooser = new JFileChooser();
                javax.swing.filechooser.FileFilter f1 = new FileNameExtensionFilter("OLD Xml File", "xml");
                fileChooser.addChoosableFileFilter(f1);
                fileChooser.setFileFilter(f1);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                print_log("C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Frontier Developments\\Planet Coaster\\Translations");
                fileChooser.setCurrentDirectory(new File("C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Frontier Developments\\Planet Coaster\\Translations"));
                int result = fileChooser.showOpenDialog(labelOldFile);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    print_log("Selected file: " + selectedFile.getAbsolutePath());
                    labelOldFile.setText(selectedFile.getName());
                    path_old_file =selectedFile.getAbsolutePath();
                    done1=true;
                }
            }catch(Exception a){
                JOptionPane.showMessageDialog(null, "Errore apertura - Causa sconosciuta", "Errore!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //---------------------------------------------------------------------------------------
    /**Listener for new file button*/
    class NewFilePath implements ActionListener {
        public void actionPerformed(ActionEvent e){
            try{
                JFileChooser fileChooser = new JFileChooser();
                javax.swing.filechooser.FileFilter f1 = new FileNameExtensionFilter("NEW Xml File", "xml"); //Imposto i filtri
                fileChooser.addChoosableFileFilter(f1);
                fileChooser.setFileFilter(f1);
                fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
                fileChooser.setMultiSelectionEnabled(false);
                print_log("C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Frontier Developments\\Planet Coaster\\Translations");
                fileChooser.setCurrentDirectory(new File("C:\\Users\\"+System.getProperty("user.name")+"\\AppData\\Local\\Frontier Developments\\Planet Coaster\\Translations"));
                //System.getProperty("user.name")
                int result = fileChooser.showOpenDialog(labelNewFile);
                if (result == JFileChooser.APPROVE_OPTION) {
                    File selectedFile = fileChooser.getSelectedFile();
                    print_log("Selected file: " + selectedFile.getAbsolutePath());
                    labelNewFile.setText(selectedFile.getName());
                    path_new_file =selectedFile.getAbsolutePath();
                    done2=true;
                }
            }catch(Exception a){
                JOptionPane.showMessageDialog(null, "Errore apertura - Causa sconosciuta", "Errore!", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
    //---------------------------------------------------------------------------------------
    /**Listener that merge the 2 files with a thread*/

    class Elaborate_Files implements ActionListener, Runnable{
        public void actionPerformed(ActionEvent e) {
            if(done1&&done2) {
                Thread t = new Thread(new Elaborate_Files());
                t.start();
            }else{
                result.setText("Select Files First!");
                elaborate.setEnabled(true);
            }
        }

        public void run() {
            elaborate.setEnabled(false);
            result.setText("Working...");
            try{
                PlanetCoasterWriter w = new PlanetCoasterWriter(path_old_file, path_new_file);
                while(!w.has_finished){
                    Thread.sleep(30);
                }
                result.setText("Done!");
                elaborate.setEnabled(true);
            }catch (Exception err){
                print_log("Error:"+err);
                result.setText("ERROR!<br/>"+err);
                elaborate.setEnabled(true);
            }
        }
    }//Class
    //---------------------------------------------------------------------------------------
    /**Function to pretty print the log with time
     @param s String to print*/
    private void print_log(String s){
        Date date = new Date();
        SimpleDateFormat ft = new SimpleDateFormat ("hh:mm:ss");
        System.out.println(ft.format(date)+"-->"+s);
    }//Stampa
    //---------------------------------------------------------------------------------------

    public static void main(String[] args){
        new Window();
    }

    //---------------------------------------------------------------------------------------
}