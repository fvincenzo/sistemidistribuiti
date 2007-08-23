/*
 * Created on Jan 5, 2007
 *
 */
package gui;

import java.awt.BorderLayout;
import java.awt.event.*;
import java.beans.PropertyVetoException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStreamReader;

import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;




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
    public static MainGui m;
    private String userName = "";

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
        toolBar.add(connect);
        connect.addActionListener(this);
        connect.setActionCommand("connect");
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

    public void openChatWindow(String channel){
        if (userName.equals("")) this.userName = "Default";
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
        if ("connect".equals(e.getActionCommand())){
            showListWindow();
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
