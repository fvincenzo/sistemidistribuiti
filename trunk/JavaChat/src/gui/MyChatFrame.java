/*
 * Created on Jan 6, 2007
 *
 */
package gui;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.jms.JMSException;
import javax.swing.JInternalFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;

import core.Channel;


/**
 * @author   noname
 */
public class MyChatFrame extends JInternalFrame implements InternalFrameListener , ChatApplicationNotifier, KeyListener{

    private JTextField inputText = new JTextField();
    private JTextArea outputText = new JTextArea();
    private JList userList = new JList();
    private JSplitPane splitPane = new JSplitPane();
    private JScrollPane scrollPane = new JScrollPane(outputText,
            JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

    private String channel;
    private String userName;
//    private Object lock;
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

//        inputText.setFocusCycleRoot(true);
//        inputText.requestFocus();

        /*
         * Attivazione della connessione
         */
        inputText.addKeyListener(this);
        
        try {
            chat = initialize(this.userName);
            
//            inputText.setText("Type text here and press <enter>");
//            inputText.selectAll();
        } catch(Exception E){
            print("Error while connecting to "+channel);
        }
        inputText.requestFocusInWindow();

    }
    private void print (String text){
        outputText.append(text+"\n");
    }

    private Channel initialize(String username) throws Exception {
	Channel c = Channel.connect(this.channel, username);
        c.setTextReceiver(this);
        return c;
	
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
    
    public void usernameAlreadyInUse(){
	JOptionPane.showMessageDialog(this, "Questo username è già in uso.", "Errore", JOptionPane.ERROR_MESSAGE);
	try{
	this.chat = initialize("username_secondario");
	}
	catch(Exception e){
	    
	}
    }
    public void keyPressed(KeyEvent e) {
	// TODO Implementare il metodo keyPressed
	int key = e.getKeyCode();
        if (key == KeyEvent.VK_ENTER) {
            try {
                
                chat.sendText(inputText.getText());
            }
            catch (JMSException jme){
                outputText.setText("failed to send: "+inputText.getText());

            }
            inputText.setText("");
            inputText.requestFocusInWindow();
        }
    }
    public void keyReleased(KeyEvent arg0) {
	// TODO Implementare il metodo keyReleased
    }
    public void keyTyped(KeyEvent arg0) {
	// TODO Implementare il metodo keyTyped
    }

}
