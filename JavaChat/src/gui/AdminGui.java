/*
 * Created on Jan 21, 2007
 *
 */
package gui;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import core.JavaChatAdminService;


import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Label;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;

/**
 * @author   noname
 */
public class AdminGui extends JFrame implements ActionListener, AdminGuiInterface {

    
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
//            setResizable(false);
            setTitle("Java chat administration panel");
//          getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
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
            
        }
        
        private void exit(){
            this.dispose();
        }



        public void actionPerformed(ActionEvent e) {
            // TODO Implementare actionPerformed
            if (e.getSource() == (JButton)exit ){
                exit();
            }
            if (e.getSource() == (JButton)init ){
                if (adminService.initServer()){
                    add.setEnabled(true);
                    disconnect.setEnabled(true);
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
                
            }
            if (e.getSource() == (JButton)edit ){
                
            }
            if (e.getSource() == (JButton)disconnect ){
                adminService.disconnect();
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
        }
        
        
        public static void main(String args[]) {
            java.awt.EventQueue.invokeLater(new Runnable() {
                public void run() {
                    new AdminGui().setVisible(true);
                }
            });
        }
}

/**
 * @author   noname
 */
class NewChat extends JFrame implements ActionListener {
    
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
//        nome.setBounds(10, 10, 30, 20);
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
                adminService.createChat(nomeChat.getText(), true);
            }
            if (!privato.isSelected() && !nomeChat.getText().equals("")){
                adminService.createChat(nomeChat.getText(), false);
            }
            dispose();
    }
    
}

}