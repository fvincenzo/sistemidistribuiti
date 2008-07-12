package android.pc.client;

import java.awt.AWTException;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class PcClient {

	public PcClient() {
		
		final PcClientAPI pcapi = new PcClientAPI();
		
		final TrayIcon trayIcon;

		if (SystemTray.isSupported()) {

		    SystemTray tray = SystemTray.getSystemTray();
		    final Image image = Toolkit.getDefaultToolkit().getImage("res/inactive.png");
		    final Image updatedImage = Toolkit.getDefaultToolkit().getImage("res/active.png");

		    PopupMenu popup = new PopupMenu();
		    trayIcon = new TrayIcon(image, "Android Contact PC Client", popup);

		    ActionListener connectListener = new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		pcapi.connect();
		    		trayIcon.setImage(updatedImage);
		    		trayIcon.displayMessage("Android Contact PC Client", 
		    		           "Connected!",
		    		           TrayIcon.MessageType.INFO);
		    	}
		    };
		    
		    MenuItem connectItem = new MenuItem("Connect");
		    connectItem.addActionListener(connectListener);
		    popup.add(connectItem);
		    
		    ActionListener mailListener = new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		if(pcapi.mail()) {
			    		trayIcon.displayMessage("Android Contact PC Client", 
			    		           "Mail Setted as Preferred!",
			    		           TrayIcon.MessageType.INFO);
		    		} else {
		    			trayIcon.displayMessage("Android Contact PC Client", 
			    		           "Connect Before!",
			    		           TrayIcon.MessageType.ERROR);
		    		}
		    	}
		    };
		    
		    MenuItem mailItem = new MenuItem("Set Mail as Preferred");
		    mailItem.addActionListener(mailListener);
		    popup.add(mailItem);
		    
		    ActionListener imListener = new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		if(pcapi.im()) {
			    		trayIcon.displayMessage("Android Contact PC Client", 
			    		           "IM Setted as Preferred!",
			    		           TrayIcon.MessageType.INFO);
		    		} else {
		    			trayIcon.displayMessage("Android Contact PC Client", 
			    		           "Connect Before!",
			    		           TrayIcon.MessageType.ERROR);
		    		}
		    	}
		    };
		    
		    MenuItem imItem = new MenuItem("Set IM as Preferred");
		    imItem.addActionListener(imListener);
		    popup.add(imItem);
		    
		    ActionListener disconnectListener = new ActionListener() {
		    	public void actionPerformed(ActionEvent e) {
		    		pcapi.disconnect();
		    		trayIcon.setImage(image);
		    		trayIcon.displayMessage("Android Contact PC Client", 
		    		           "Disconnected!",
		    		           TrayIcon.MessageType.INFO);
		    	}
		    };
		    
		    MenuItem disconnectItem = new MenuItem("Disconnect");
		    disconnectItem.addActionListener(disconnectListener);
		    popup.add(disconnectItem);
		    
		    ActionListener exitListener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            System.out.println("Exiting...");
		            System.exit(0);
		        }
		    };
		            
		    MenuItem defaultItem = new MenuItem("Exit");
		    defaultItem.addActionListener(exitListener);
		    popup.add(defaultItem);
		    
		    ActionListener actionListener = new ActionListener() {
		        public void actionPerformed(ActionEvent e) {
		            trayIcon.displayMessage("Android Contact PC Client", 
		                "This is the PC Version of Android Contact Client!",
		                TrayIcon.MessageType.INFO);
		        }
		    };
		            
		    trayIcon.setImageAutoSize(true);
		    trayIcon.addActionListener(actionListener);

		    try {
		        tray.add(trayIcon);
		    } catch (AWTException e) {
		        System.err.println("TrayIcon could not be added.");
		    }

		} else {

		    System.out.println("PcClient.PcClient():SystemTray not supported!");
		}
		
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new PcClient();

	}

	
}
