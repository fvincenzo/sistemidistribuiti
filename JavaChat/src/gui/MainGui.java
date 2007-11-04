/*
 * Created on Jan 5, 2007
 *
 */
package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import core.JavaChatAdminService;




/**
 * @author   noname
 */
public class MainGui extends JFrame implements ActionListener {

    private int defaultWidth = 800;
    private int defaultHeight = 600;
    private JDesktopPane desktopPane = new JDesktopPane();
    private JToolBar toolBar = new JToolBar("JavaChat toolbar");
    private String address = "localhost";
    private JButton connect = new JButton("Connect to...");
    private JButton settings = new JButton("Settings");
    public static MainGui m;
    private String userName = "Default";
//    private String address = "";
    
    public MainGui(){

        this.setTitle("JavaChat v0.1(R)");
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        this.setBounds((screenSize.width-defaultWidth)/2, (screenSize.height-defaultHeight)/2, defaultWidth,defaultHeight);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);


        try {
            UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
            //TODO cambiare in getSystemLookAndFeelClassName() ma dopo aver sistemato le cose sulla gentoo ;-)
        } catch (ClassNotFoundException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        } catch (InstantiationException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        } catch (UnsupportedLookAndFeelException e) {
            //  Auto-generated catch block
            e.printStackTrace();
        }

        getContentPane().setLayout(new BorderLayout());
        this.add(toolBar, BorderLayout.PAGE_START);
        toolBar.setFloatable(false);
        toolBar.add(settings);
        toolBar.add(connect);
        settings.addActionListener(this);
        connect.addActionListener(this);
//        connect.setActionCommand("connect");
        this.add(desktopPane, BorderLayout.CENTER);
        MyChannelListFrame.setServerAddress(address);
        MyChannelListFrame.setMainApplication(this);
        //TODO leggere le impostazioni da un file
        try {
            BufferedReader read = new BufferedReader(new FileReader("foeifwoifj"));
            
        }
        catch (FileNotFoundException e){
            
        }
//        openChatWindow("topicChat");
//      MyChatFrame cf1 = new MyChatFrame("Prova1");
//      MyChatFrame cf2 = new MyChatFrame("Prova2");
//      MyChatFrame cf3 = new MyChatFrame("Prova3");
//      desktopPane.add(cf1);
//      desktopPane.add(cf2);
//      desktopPane.add(cf3);
//      cf1.setVisible(true);

        /**
         * Non so perchè ma senza il System.exit non chiudeva tutta l'applicazione e 
         * rimanevano dei thread attivi sulla JVM...
         */
        this.addWindowListener(new WindowListener(){
            public void windowClosed(WindowEvent e) {
                System.exit(0);

            }

            public void windowActivated(WindowEvent e) {
                // TODO Implementare windowActivated

            }

            public void windowClosing(WindowEvent e) {
                // TODO Implementare windowClosing

            }

            public void windowDeactivated(WindowEvent e) {
                // TODO Implementare windowDeactivated

            }

            public void windowDeiconified(WindowEvent e) {
                // TODO Implementare windowDeiconified

            }

            public void windowIconified(WindowEvent e) {
                // TODO Implementare windowIconified

            }

            public void windowOpened(WindowEvent e) {
                // TODO Implementare windowOpened

            }
        });



    }
    public String getUsername(){
	return this.userName;
    }
    
    public String getAddress(){
	return this.address;
    }

    public void setUsername(String username){
	this.userName = username;
    }
    
    public void setAddress (String address){
	this.address = address;
	MyChannelListFrame.setServerAddress(address);
    }
    
    public void openChatWindow(String channel){
        
        MyChatFrame newChat = new MyChatFrame(channel, userName);
        
        desktopPane.add(newChat);
        try{
            newChat.setSelected(true);
        }catch (PropertyVetoException e) {}
        newChat.setVisible(true);
        newChat.moveToFront();
    }

    public boolean showListWindow(){
        if (!MyChannelListFrame.getChannelListFrame().isVisible()){
            desktopPane.add(MyChannelListFrame.getChannelListFrame());

            MyChannelListFrame.getChannelListFrame().updateList();
            MyChannelListFrame.getChannelListFrame().setVisible(true);
                try{
                    MyChannelListFrame.getChannelListFrame().setSelected(true);
                }catch (PropertyVetoException e) {}
                MyChannelListFrame.getChannelListFrame().moveToFront();
            
            return true;
        }
        return false;
    }
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == (JButton)connect){
            showListWindow();
        }
        if (e.getSource() == (JButton)settings){
            new MainSettingPanel(this).setVisible(true);
        }

    }
    //Main

    public static void main(String[] args) {
        try{ UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
        catch( Exception e ) {}

        m = new MainGui();
        m.setVisible(true);
    }

}


class MainSettingPanel extends JFrame implements ActionListener {

//    private JavaChatAdminService adminService;
    private JTextField indirizzo = new JTextField();
//    private JTextField porta = new JTextField();
    private JTextField utente = new JTextField();
//    private JPasswordField password = new JPasswordField();
//  private JLabel nome = new JLabel("Inserisci il nome della chat:");
    private JLabel indirizzoL = new JLabel("Indirizzo del server delle chat: ");
//    private JLabel portaL = new JLabel("Porta: ");
    private JLabel utenteL = new JLabel("Nome utente: ");
//    private JLabel passwordL = new JLabel("Password: ");
//  private JCheckBox privato = new JCheckBox("Privato");
    private JButton ok = new JButton("OK");
    private JButton cancel = new JButton("Cancel");
    private JPanel sotto = new JPanel();
    private JPanel centro = new JPanel();
    private MainGui mg;

    public MainSettingPanel(MainGui mg){
	this.mg = mg;
//	this.adminService = adminService;
	sotto.setLayout(new FlowLayout());
	ok.addActionListener(this);
	cancel.addActionListener(this);
	sotto.add(ok);
	sotto.add(cancel);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	getContentPane().add(sotto, BorderLayout.SOUTH);
	getContentPane().add(centro, BorderLayout.CENTER);
	centro.setLayout(null);
	centro.add(indirizzoL);
	indirizzoL.setBounds(30, 10, 300, 25);
	centro.add(indirizzo);
	indirizzo.setBounds(30, 35, 300, 25);
	indirizzo.setText(mg.getAddress());
//	centro.add(portaL);
//	portaL.setBounds(30, 60, 300, 25);
//	centro.add(porta);
//	porta.setBounds(30, 85, 300, 25);
	centro.add(utenteL);
	utenteL.setBounds(30, 60, 300, 25);
	centro.add(utente);
	utente.setBounds(30, 85, 300, 25);
	utente.setText(mg.getUsername());
//	centro.add(passwordL);
//	passwordL.setBounds(30, 160, 300, 25);
//	centro.add(password);
//	password.setBounds(30, 185, 300, 25);
	setResizable(false);
//	indirizzo.setText(adminService.getAddress());
//	porta.setText(""+adminService.getJoramPort());
//	utente.setText(adminService.getUserName());
	java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setBounds((screenSize.width-360)/2, (screenSize.height-200)/2, 360, 200);


    }

    public void actionPerformed(ActionEvent arg0) {
	// TODO Implementare il metodo actionPerformed
	if (arg0.getSource() == (JButton)cancel) {
	    dispose();
	}
	if (arg0.getSource() == (JButton)ok) {
	    if (utente.getText().trim() != "" && indirizzo.getText().trim() != ""){
		mg.setAddress(indirizzo.getText().trim());
		mg.setUsername(utente.getText().trim());
		dispose();
	    }
	}

    }
}
