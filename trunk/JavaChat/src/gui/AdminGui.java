/*
 * Created on Jan 21, 2007
 *
 */
package gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import org.w3c.dom.events.MouseEvent;

import core.JavaChatAdminService;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.util.Vector;

/**
 * @author   noname
 */
public class AdminGui extends JFrame implements ActionListener, MouseListener, AdminGuiInterface {

    private JButton add = new JButton("Add");
    private JButton delete = new JButton("Delete");
    private JButton edit = new JButton("Edit");
    private JButton exit = new JButton("Exit");
    private JButton init = new JButton("Init");
    private JButton settings = new JButton("Settings");
    private JButton disconnect = new JButton("Disconnect");
    private JPanel jPanel1 = new JPanel();
    private JTable tabella = new JTable();
    private JScrollPane jScrollPane1 = new JScrollPane(tabella, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED); 
    private Vector<String> columns = new Vector<String>();
    private Vector rows= new Vector();
    private DefaultTableModel tabModel;
    private int lines = 0;

    private JavaChatAdminService adminService;
    private SettingPanel setPanel;


    public AdminGui() {
	String[] titles = {"Channels", "Private"};
	for (int i=0; i<titles.length; i++){
	    columns.addElement(titles[i]);
	}
	tabModel = new DefaultTableModel();
	tabModel.setDataVector(rows, columns);
	tabella.setModel(tabModel);
	tabella.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	getContentPane().setLayout(new BorderLayout());
	setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
//	setResizable(false);
	setTitle("Java chat administration panel");
//	getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
	getContentPane().add(jScrollPane1, BorderLayout.CENTER);
	jPanel1.setLayout(new FlowLayout());
	init.addActionListener(this);
	add.addActionListener(this);
	edit.addActionListener(this);
	delete.addActionListener(this);
	settings.addActionListener(this);
	exit.addActionListener(this);
	disconnect.addActionListener(this);
	add.setEnabled(false);
	edit.setEnabled(false);
	delete.setEnabled(false);
	disconnect.setEnabled(false);

	jPanel1.add(init);
	jPanel1.add(add);
	jPanel1.add(edit);
	jPanel1.add(delete);
	jPanel1.add(settings);
	jPanel1.add(disconnect);
	jPanel1.add(exit);
	getContentPane().add(jPanel1, BorderLayout.SOUTH);
	java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setBounds((screenSize.width-600)/2, (screenSize.height-413)/2, 600, 413);

	adminService = new JavaChatAdminService();
	adminService.setInterfaccia(this);
	setPanel = new SettingPanel(adminService);

	tabella.addMouseListener(this);


    }

    private void quit(){
	adminService.disconnect();

	this.dispose();
	System.exit(0);


    }


    public void actionPerformed(ActionEvent e) {
	// TODO Implementare actionPerformed
	if (e.getSource() == (JButton)exit ){
//	    adminService.disconnect();
	    quit();
	}
	if (e.getSource() == (JButton)init ){
	    if (adminService.initServer()){
		add.setEnabled(true);
		disconnect.setEnabled(true);
		init.setEnabled(false);
		settings.setEnabled(false);
	    }
	    else {
		JOptionPane.showMessageDialog(this, "Impossibile avviare il server.\n\nControlla di aver lanciato:\n- Il registry di rmi\n- Il server joram\n- Di aver impostato la corretta codebase\n- Di aver attivato il Channel Name Server\n\nPer ulteriori informazioni consulta\nil file README.txt alla sezione inizializzazione", "Errore", JOptionPane.ERROR_MESSAGE);
	    }
	}
	if (e.getSource() == (JButton)add ){
	    new NewChat(adminService).setVisible(true);

	}
	if (e.getSource() == (JButton)delete ){
	    if (tabella.getSelectedRow() != -1){
		Vector<String> v = (Vector<String>)rows.get(tabella.getSelectedRow());
		String tmp = v.firstElement();
		adminService.deleteChat(tmp);
	    }
	    else {

	    }
	}
	if (e.getSource() == (JButton)settings ){
	    setPanel.setVisible(true);

	}
	if (e.getSource() == (JButton)edit ){
	    new EditChat(adminService, ((Vector<String>)rows.get(tabella.getSelectedRow())).firstElement(), Boolean.parseBoolean(((Vector<String>)rows.get(tabella.getSelectedRow())).elementAt(1)) ).setVisible(true);
	}
	if (e.getSource() == (JButton)disconnect ){
	    if (adminService.disconnect()){
		rows.clear();
		disconnect.setEnabled(false);
		settings.setEnabled(true);
		add.setEnabled(false);
		edit.setEnabled(false);
		delete.setEnabled(false);
		init.setEnabled(true);
		init.setText("Connect");
		tabella.addNotify();
	    }
	}

    }

    public void addLine(String nome, boolean privato){
	Vector<String> tmp = new Vector<String>();
	tmp.addElement(nome);
	tmp.addElement(String.valueOf(privato));
	rows.addElement(tmp);
	lines++;
	tabella.addNotify();

    }
    public void editLine(String nome, boolean privato){
	Vector<String> tmp = new Vector<String>();
	tmp.addElement(nome);
	tmp.addElement(String.valueOf(privato));
	for (int i = 0; i<rows.size(); i++){
//	    tmp = (Vector<String>)rows.elementAt(i);
	    if (nome == ((Vector)rows.elementAt(i)).firstElement() ){
		rows.set(i, tmp);
	    }
	}
	tabella.addNotify();
	tabella.repaint();
    }
    public void deleteLine(String chat){
	Vector<String> tmp;
	for (int i =0; i< rows.size(); i++){
	    tmp = (Vector<String>)rows.elementAt(i);
	    if (chat == tmp.elementAt(0)){
		rows.removeElementAt(i);
		lines--;
	    }
	}
	tabella.addNotify();
	edit.setEnabled(false);
	delete.setEnabled(false);
    }



    public static void main(String args[]) {
	java.awt.EventQueue.invokeLater(new Runnable() {
	    public void run() {
		new AdminGui().setVisible(true);
	    }
	});
    }



    public void mouseClicked(java.awt.event.MouseEvent e) {
	delete.setEnabled(true);
	edit.setEnabled(true);
    }

    public void mouseEntered(java.awt.event.MouseEvent e) {
	// TODO Implementare il metodo mouseEntered
    }

    public void mouseExited(java.awt.event.MouseEvent e) {
	// TODO Implementare il metodo mouseExited
    }

    public void mousePressed(java.awt.event.MouseEvent e) {
	// TODO Implementare il metodo mousePressed
    }

    public void mouseReleased(java.awt.event.MouseEvent e) {
	// TODO Implementare il metodo mouseReleased
    }

}


/**
 * @author   noname
 */
class NewChat extends JFrame implements ActionListener {

    /**
     * 
     */
    private JTextField nomeChat = new JTextField();
    private JLabel nome = new JLabel("Inserisci il nome della chat:");
    private JCheckBox privato = new JCheckBox("Privato");
    private JButton ok = new JButton("OK");
    private JButton cancel = new JButton("Cancel");
    private JPanel sotto = new JPanel();
    private JPanel centro = new JPanel();

    private JavaChatAdminService adminService;

    public NewChat(JavaChatAdminService adminService){
	this.adminService = adminService;
	sotto.setLayout(new FlowLayout());
	ok.addActionListener(this);
	cancel.addActionListener(this);
	sotto.add(ok);
	sotto.add(cancel);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	getContentPane().add(sotto, BorderLayout.SOUTH);
	getContentPane().add(centro, BorderLayout.CENTER);
	centro.setLayout(null);
	centro.add(nome);
	nome.setBounds(30, 10, 300, 25);
//	nome.setBounds(10, 10, 30, 20);
	centro.add(nomeChat);
	nomeChat.setBounds(30, 35, 300, 25);
	centro.add(privato);
	privato.setBounds(30, 60, 300, 25);
	setResizable(false);
	java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setBounds((screenSize.width-360)/2, (screenSize.height-150)/2, 360, 150);


    }

    public void actionPerformed(ActionEvent arg0) {
	// TODO Implementare il metodo actionPerformed
	if (arg0.getSource() == (JButton)cancel) {
	    dispose();
	}
	if (arg0.getSource() == (JButton)ok) {
	    if (privato.isSelected() && !nomeChat.getText().equals("")){
		if (!adminService.createChat(nomeChat.getText(), true)){
		    JOptionPane.showMessageDialog(this, "Impossibile creare la chat.\nControlla che non ne esista giˆ una con lo\nstesso nome", "Errore", JOptionPane.ERROR_MESSAGE);
		}
		else 
		    dispose();
	    }
	    if (!privato.isSelected() && !nomeChat.getText().equals("")){
		if (!adminService.createChat(nomeChat.getText(), false)){
		    JOptionPane.showMessageDialog(this, "Impossibile creare la chat.\nControlla che non ne esista giˆ una con lo\nstesso nome", "Errore", JOptionPane.ERROR_MESSAGE);
		}
		else
		    dispose();
	    }

	}

    }

}
class EditChat extends JFrame implements ActionListener {

    /**
     * 
     */
//  private JTextField nomeChat = new JTextField();
    private JLabel titolo = new JLabel();
    private String[] options = {"Visible", "Private"};
    private JComboBox visibility = new JComboBox(options);
//  private JCheckBox privato = new JCheckBox("Privato");
    private JButton ok = new JButton("OK");
    private JButton cancel = new JButton("Cancel");
    private JPanel sotto = new JPanel();
    private JPanel centro = new JPanel();
    
    private String current;
    private JavaChatAdminService adminService;

    public EditChat(JavaChatAdminService adminService, String current, boolean priv){
	this.adminService = adminService;
	this.current = current;
	sotto.setLayout(new FlowLayout());
	ok.addActionListener(this);
	cancel.addActionListener(this);
	sotto.add(ok);
	sotto.add(cancel);
	setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	getContentPane().add(sotto, BorderLayout.SOUTH);
	getContentPane().add(centro, BorderLayout.CENTER);
	centro.setLayout(null);
	titolo.setText("Visibilitˆ per la stanza \""+current+"\"");
	centro.add(titolo);
	titolo.setBounds(30, 10, 300, 25);
//	nome.setBounds(10, 10, 30, 20);
	if (priv){
	    visibility.setSelectedIndex(1);
	}
	else {
	    visibility.setSelectedIndex(0);
	}
//	visibility.addActionListener(this);
	centro.add(visibility);
	visibility.setBounds(30, 35, 300, 25);
//	centro.add(privato);
//	privato.setBounds(30, 60, 300, 25);
	setResizable(false);
	java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setBounds((screenSize.width-360)/2, (screenSize.height-150)/2, 360, 150);
	

    }
 

    public void actionPerformed(ActionEvent arg0) {
	// TODO Implementare il metodo actionPerformed
	if (arg0.getSource() == (JButton)cancel) {
	    dispose();
	}
	if (arg0.getSource() == (JButton)ok) {
	    String select = (String)visibility.getSelectedItem();
	    if (select.equals("Private") ){
		if (adminService.editChat(current, true))
		    dispose();
		else
		    JOptionPane.showMessageDialog(this, "Errore remoto. Controllare che il list server stia andando", "Errore", JOptionPane.ERROR_MESSAGE);
	    }
	    else {
		if (adminService.editChat(current, false))
		    dispose();
		else
		    JOptionPane.showMessageDialog(this, "Errore remoto. Controllare che il list server stia andando", "Errore", JOptionPane.ERROR_MESSAGE);
	    }
	}


    }

}



class SettingPanel extends JFrame implements ActionListener {

    private JavaChatAdminService adminService;
    private JTextField indirizzo = new JTextField();
    private JTextField porta = new JTextField();
    private JTextField utente = new JTextField();
    private JPasswordField password = new JPasswordField();
//  private JLabel nome = new JLabel("Inserisci il nome della chat:");
    private JLabel indirizzoL = new JLabel("Indirizzo: ");
    private JLabel portaL = new JLabel("Porta: ");
    private JLabel utenteL = new JLabel("Nome utente: ");
    private JLabel passwordL = new JLabel("Password: ");
//  private JCheckBox privato = new JCheckBox("Privato");
    private JButton ok = new JButton("OK");
    private JButton cancel = new JButton("Cancel");
    private JPanel sotto = new JPanel();
    private JPanel centro = new JPanel();


    public SettingPanel(JavaChatAdminService adminService){
	this.adminService = adminService;
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
	centro.add(portaL);
	portaL.setBounds(30, 60, 300, 25);
	centro.add(porta);
	porta.setBounds(30, 85, 300, 25);
	centro.add(utenteL);
	utenteL.setBounds(30, 110, 300, 25);
	centro.add(utente);
	utente.setBounds(30, 135, 300, 25);
	centro.add(passwordL);
	passwordL.setBounds(30, 160, 300, 25);
	centro.add(password);
	password.setBounds(30, 185, 300, 25);
	setResizable(false);
	indirizzo.setText(adminService.getAddress());
	porta.setText(""+adminService.getJoramPort());
	utente.setText(adminService.getUserName());
	java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
	setBounds((screenSize.width-360)/2, (screenSize.height-300)/2, 360, 300);


    }

    public void actionPerformed(ActionEvent arg0) {
	// TODO Implementare il metodo actionPerformed
	if (arg0.getSource() == (JButton)cancel) {
	    dispose();
	}
	if (arg0.getSource() == (JButton)ok) {
	    if (!indirizzo.getText().equals("") && !porta.getText().equals("") && !utente.getText().equals("") && (password.getPassword()).length != 0){
		try {
		    adminService.setAddress(indirizzo.getText());
		    adminService.setJoramPort(Integer.parseInt(porta.getText()));
		    adminService.setUserName(utente.getText());
		    adminService.setPassword(new String(password.getPassword()));

		    dispose();
		}
		catch(NumberFormatException e){
		    JOptionPane.showMessageDialog(this, "Il campo \"porta\" non contiene un numero", "Errore", JOptionPane.ERROR_MESSAGE);

		}
	    }else {
		JOptionPane.showMessageDialog(this, "Impossibile impostare questi valori.\n Per favore controlla di aver inserto tutti i dati\ne di aver inserito un numero nel campo \"porta\"", "Errore", JOptionPane.ERROR_MESSAGE);

	    }
	}

    }
}
