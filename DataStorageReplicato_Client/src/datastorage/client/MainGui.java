package datastorage.client;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CosNaming.NamingContextExt;
import org.omg.CosNaming.NamingContextExtHelper;
import org.omg.CosNaming.NamingContextPackage.CannotProceed;
import org.omg.CosNaming.NamingContextPackage.InvalidName;
import org.omg.CosNaming.NamingContextPackage.NotFound;
import datastorage.corba.CantUpdate;
import datastorage.corba.NoValue;
import datastorage.corba.Server;
import datastorage.corba.ServerHelper;

/**
 * Classe che implementa un semplice client grafico per accedere alle funzionalita' deò datastorage replicato
 * 
 * 
 * @author Vincenzo Frascino
 * @author Nicolas Tagliani
 *
 */
@SuppressWarnings("serial")
public class MainGui extends JFrame implements ActionListener , KeyListener{
	private static MainGui m;
	private Server s;
	private int defaultWidth = 400;
	private int defaultHeight = 230;
	private JPanel mainPanel = new JPanel();
	private JPanel buttonPanel = new JPanel();
	private JLabel idRead = new JLabel("Id da leggere:");
	private JLabel idWrite = new JLabel("Id da scrivere:");
	private JLabel idWrite2 = new JLabel("Valore da scrivere");
	private JTextField readBox = new JTextField();
	private JTextField writeBox = new JTextField();
	private JTextField writeBox2 = new JTextField();
	private JLabel results = new JLabel("Risultato:");
	private JTextField result = new JTextField();
	private JButton exit = new JButton("Exit");
	private JButton read = new JButton("Read");
	private JButton write = new JButton("Write");



	/**
	 * @param args
	 */
	public MainGui(String[] args){

		try {
			ORB orb = ORB.init(args,null);
			Object objRef = orb.resolve_initial_references("NameService");

			NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
			s = ServerHelper.narrow(ncRef.resolve_str("ReplicationServer"));
		} catch (NotFound e) {
			e.printStackTrace();
		} catch (CannotProceed e) {
			e.printStackTrace();
		} catch (InvalidName e) {
			e.printStackTrace();
		} catch (org.omg.CORBA.ORBPackage.InvalidName e) {
			e.printStackTrace();
		}

		this.setTitle("Data Storage Replicato Client");
		java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
		this.setBounds((screenSize.width-defaultWidth)/2, (screenSize.height-defaultHeight)/2, defaultWidth,defaultHeight);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);



		getContentPane().setLayout(new BorderLayout());
		mainPanel.setLayout(null);

		mainPanel.add(idRead);
		idRead.setBounds(20, 10, 300, 25);
		mainPanel.add(readBox);
		readBox.setBounds(20, 35, 270, 25);
		readBox.addKeyListener(this);
		mainPanel.add(idWrite);
		idWrite.setBounds(20, 60, 130, 25);
		mainPanel.add(idWrite2);
		idWrite2.setBounds(160, 60, 130, 25);

		mainPanel.add(writeBox);
		mainPanel.add(writeBox2);
		writeBox.setBounds(20, 85, 130, 25);
		writeBox2.setBounds(160, 85, 130, 25);
		mainPanel.add(results);
		results.setBounds(20, 110, 80, 25);
		mainPanel.add(result);
		result.setBounds(20, 135, 360, 25);
		writeBox2.addKeyListener(this);
		writeBox.addKeyListener(this);
		mainPanel.add(read);
		read.setBounds(305, 35, 80, 25);

		mainPanel.add(write);
		write.setBounds(305, 85, 80, 25);

		buttonPanel.setLayout(new FlowLayout());

		read.addActionListener(this);
		write.addActionListener(this);
		exit.addActionListener(this);


		buttonPanel.add(exit);
		getContentPane().add(mainPanel, BorderLayout.CENTER);
		getContentPane().add(buttonPanel, BorderLayout.SOUTH);


	}

	public static void main(String[] args) {

		try{ UIManager.setLookAndFeel( UIManager.getSystemLookAndFeelClassName() ); }
		catch( Exception e ) {}

		m = new MainGui(args);
		m.setVisible(true);
	}

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == read){
			if (!readBox.getText().equals("")){
				result.setText(read(readBox.getText()));
			} else {
				result.setText("Inserire un id da leggere");
			}
		}
		if (e.getSource() == write){
			if (!writeBox.equals("") && !writeBox2.equals("") ){
				result.setText(write(writeBox.getText(), writeBox2.getText()));
			} else {
				result.setText("Inserire id e valore da scrivere");
			}
		}
		else if (e.getSource() == exit){
			this.dispose();
		}
	}

	public void keyPressed(KeyEvent e) {


	}

	public void keyReleased(KeyEvent e) {


	}

	public void keyTyped(KeyEvent e) {
		if (e.getKeyChar() == KeyEvent.VK_ENTER){
			if (e.getSource() == readBox){
				if (!readBox.getText().equals("")){
					result.setText(read(readBox.getText()));
				} else {
					result.setText("Inserire un id da leggere");
				}
			} else if (e.getSource() == writeBox || e.getSource() == writeBox2){
				if (!writeBox.equals("") && !writeBox2.equals("") ){
					result.setText(write(writeBox.getText(), writeBox2.getText()));
				} else {
					result.setText("Inserire id e valore da scrivere");
				}
			}

		}

	}

	private String read(String value){
		try{
			int dataId = Integer.parseInt(value.trim());
			return ""+s.read(dataId);
		}catch (NumberFormatException e ){
			return "Errore nel formato dei dati";
		}catch (NoValue e){
			return "Valore NON presente";
		}
	}
	private String write (String id, String value){
		try{
			int dataId = Integer.parseInt(id.trim());
			int dataValue = Integer.parseInt(value.trim());
			s.write(dataId, dataValue);
			return "Fatto";
		}catch (NumberFormatException e ){
			return "Errore nel formato dei dati";
		}catch (CantUpdate e){
			return "Impossibilie aggiornare il valore";
		}
	}
}


