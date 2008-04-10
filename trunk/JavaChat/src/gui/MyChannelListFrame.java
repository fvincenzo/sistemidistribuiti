/*
 * Created on Jan 6, 2007
 *
 */
package gui;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;

import javax.swing.JButton;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import serverUtils.ChannelList;

/**
 * @author   noname
 */
@SuppressWarnings("serial")
public class MyChannelListFrame extends JInternalFrame implements KeyListener, ActionListener {

    private JList lista = new JList();
    private JTextField address = new JTextField();
    private JButton connect = new JButton("Connect");
    private JPanel south = new JPanel();
    private JPanel main = new JPanel();

    private JScrollPane scrollPane = new JScrollPane(lista,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    /**
     * @uml.property  name="serverAddress"
     */
    private static String serverAddress = ""; 
    private static ChannelList list = null;
    private static MyChannelListFrame frame = null;
    private static MainGui mainGui= null;


    private MyChannelListFrame() {
        super("Select a Channel to join", true, true, true, true);
        this.setLocation(10, 10);
        this.setDefaultCloseOperation(JInternalFrame.DISPOSE_ON_CLOSE);
        this.setSize(500, 300);
        this.setLayout(new BorderLayout());
        this.add(main, BorderLayout.CENTER);
        main.setLayout(new BorderLayout());
        main.add(scrollPane, BorderLayout.CENTER);
        // FlowLayout f = new FlowLayout();
//      f.setAlignment(FlowLayout.LEFT);
        south.setLayout(new BorderLayout());
        south.add(address, BorderLayout.CENTER);
        address.addKeyListener(this);
        //  address.setMinimumSize(new Dimension(300, 25));
        connect.addActionListener(this);
        south.add(connect, BorderLayout.EAST);

        main.add(south, BorderLayout.SOUTH);
        try {
            String[] l = list.getAllChannels();
            lista.setListData(l);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            String[] s = {"Error occurred on server "+serverAddress,"Try to contact the administrator"};
            lista.setListData(s);
        }
        lista.addMouseListener(new MouseListener(){

            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2){
                    mainGui.openChatWindow((String)lista.getSelectedValue());
                }

            }

            public void mouseEntered(MouseEvent e) {
                // TODO Implementare mouseEntered

            }

            public void mouseExited(MouseEvent e) {
                // TODO Implementare mouseExited

            }

            public void mousePressed(MouseEvent e) {
                // TODO Implementare mousePressed

            }

            public void mouseReleased(MouseEvent e) {
                // TODO Implementare mouseReleased

            }

        });


    }

    public static MyChannelListFrame getChannelListFrame(){
        if (frame == null)
            frame = new MyChannelListFrame();
        return frame;
    }

    public static void setMainApplication(MainGui main){
        mainGui  = main;
    }

    /**
     * @param address
     * @return
     * @uml.property  name="serverAddress"
     */
    public static boolean setServerAddress(String address){
        try {
            serverAddress =address;
            list = (ChannelList)Naming.lookup("rmi://"+address+"/ChannelList");
            return true;
        } catch (MalformedURLException e) {
//          e.printStackTrace();
            return false;
        } catch (RemoteException e) {
//          e.printStackTrace();
            return false;
        } catch (NotBoundException e) {
//          e.printStackTrace();
            return false;
        }
    }
    

    public void updateList(){
	try {
          String[] l = list.getAllChannels();
          lista.setListData(l);
      } catch (Exception e) {
          // TODO Auto-generated catch block
          String[] s = {"Error occurred on server "+serverAddress,"Try to contact the administrator"};
          lista.setListData(s);
      }
    }
    
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_ENTER){
            String text = address.getText();
            if (!text.trim().equals("") ){
                mainGui.openChatWindow(address.getText().trim());
                address.setText("");
            }
        }
    }

    public void keyReleased(KeyEvent arg0) {
        // TODO Implementare il metodo keyReleased
    }

    public void keyTyped(KeyEvent arg0) {
        // TODO Implementare il metodo keyTyped
    }

    public void actionPerformed(ActionEvent e) {
	if (e.getSource() == (JButton)connect){
	    String text = address.getText();
            if (!text.trim().equals("") ){
                mainGui.openChatWindow(address.getText().trim());
                address.setText("");
            }
	}
	
    }

}
