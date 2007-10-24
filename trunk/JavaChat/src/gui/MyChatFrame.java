/*
 * Created on Jan 6, 2007
 *
 */
package gui;

import java.awt.AWTEvent;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.beans.PropertyVetoException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.plaf.InternalFrameUI;

import core.Channel;

import sun.awt.AWTAutoShutdown;

/**
 * @author   noname
 */
public class MyChatFrame extends JInternalFrame implements InternalFrameListener , ChatApplicationNotifier{

    private JTextField inputText = new JTextField();
    private JTextArea outputText = new JTextArea();
    private JList userList = new JList();
    private JSplitPane splitPane = new JSplitPane();
    private JScrollPane scrollPane = new JScrollPane(outputText,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    private String channel;
    private String userName;
    private Object lock;
    Channel chat;

    public MyChatFrame(String channel, String userName) {
        /*
         * Creazione dell'interfaccia
         */
        super(channel, 
                true,  // resizable
                true,  // closable
                true,  // maximizable
                true); // iconifiable
        this.channel = channel;
        this.userName = userName;
        this.setDefaultCloseOperation(JInternalFrame.DO_NOTHING_ON_CLOSE);
        this.setLocation(10, 10);
        this.setSize(500,300);
        this.addInternalFrameListener(this);
        this.add(splitPane, BorderLayout.CENTER);
        splitPane.setLeftComponent(scrollPane);
        splitPane.setRightComponent(userList);
        splitPane.setResizeWeight(1.0);
        splitPane.setDividerLocation(350);
        this.add(inputText, BorderLayout.SOUTH);
        //this.add(outputText, BorderLayout.CENTER);
        //outputText.setFocusable(false);
        //this.add(userList, BorderLayout.EAST);

        inputText.setFocusCycleRoot(true);
        


        /*
         * Attivazione della connessione
         */
        try {
            chat = Channel.connect(channel, userName);
            chat.setTextReceiver(this);
//            inputText.setText("Type text here and press <enter>");
//            inputText.selectAll();
            inputText.addKeyListener(new KeyAdapter(){
                public void keyPressed(KeyEvent e){
                    int key = e.getKeyCode();
                    if (key == KeyEvent.VK_ENTER) {
                        try {
                            chat.sendText(inputText.getText());
                        }
                        catch (JMSException jme){
                            outputText.setText("failed to send: "+inputText.getText());

                        }
                        inputText.setText("");
                    }

                }


            });
        } catch(Exception E){
            print("Error while connecting to "+channel);
        }

    }
    private void print (String text){
        outputText.append(text+"\n");
    }

    public synchronized void textReceived(String username, String text){
	
	    outputText.setForeground(Color.BLUE);
	    outputText.repaint();
	    outputText.append(username+": ");
	    outputText.setForeground(Color.BLACK);
	    outputText.repaint();
	    outputText.append(text+"\n");   
	    
    }
    
    public void textReceived(String text){
        outputText.append(text+"\n");

    }

    public void internalFrameActivated(InternalFrameEvent e) {
        // TODO Implementare internalFrameActivated

    }

    public void internalFrameClosed(InternalFrameEvent e) {
        // TODO Implementare internalFrameClosed

    }

    public void internalFrameClosing(InternalFrameEvent e) {
//      System.out.println("Trying to close internal frame...");
        int result = JOptionPane.showInternalConfirmDialog(this, "Do you want to close?", "Closing", JOptionPane.YES_NO_OPTION);
        if (result == JOptionPane.YES_OPTION){ 
            if (chat != null){
                try {
                    chat.disconnect();
                }
                catch (JMSException jme){

                }
                chat = null;
            }
            this.dispose();
        }
        // TODO Implementare internalFrameClosing

    }

    public void internalFrameDeactivated(InternalFrameEvent e) {
        // TODO Implementare internalFrameDeactivated

    }

    public void internalFrameDeiconified(InternalFrameEvent e) {
        // TODO Implementare internalFrameDeiconified

    }

    public void internalFrameIconified(InternalFrameEvent e) {
        // TODO Implementare internalFrameIconified

    }

    public void internalFrameOpened(InternalFrameEvent e) {
        // TODO Implementare internalFrameOpened

    }

    public void actionPerformed(ActionEvent e) {
        // TODO Implementare actionPerformed

    }
    public void userJoin(String userName) {
        userList.setListData(chat.getUsersList());
        
    }
    public void userPart(String userName) {
        userList.setListData(chat.getUsersList());
        
    }

}
