

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import javax.swing.SwingUtilities;


import info.collide.sqlspaces.client.TupleSpace;
import info.collide.sqlspaces.commons.Field;
import info.collide.sqlspaces.commons.Tuple;
import info.collide.sqlspaces.commons.TupleSpaceException;




public class UploadDataCSV {

	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                makeGUI(); 
            }
        });
    }
	
	private static void makeGUI() {
        DataFrame f = new DataFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    } 
	

}


 class DataFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5722803427363246535L;
	JButton butt;
	JTextField textField,textField2,textField3;
	/**
	 * @param args
	 */
	DataFrame(){
		super("upload data");
	    JPanel p = new JPanel();
        p.setLayout(new FlowLayout());
		//-Text Field
		p.add(new JLabel("Server:"));
		textField = new JTextField("127.0.0.1",15);
		p.add(textField);
		
		p.add(new JLabel("Port:"));
		textField3 = new JTextField("2525",3);
		p.add(textField3);
		textField2 = new JTextField("c:\\data\\doc\\infomedia.uib.no\\Scy project\\logs\\cyprus_lpv_3.csv",33);
		//textField2 = new JTextField("cyprus_lpv_2.csv",33);
		p.add(new JLabel("CSV file:"));
		p.add(textField2);
		//Top Panel add
		add(p,BorderLayout.PAGE_START);
		//-Button
        butt = new JButton ( "Upload" ) ; 
		p.add(butt);
        pack();
        setVisible(true);
        //*************** END Add all GUI elements
        //*************** Add action listeners
		butt.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				upload(textField.getText(),Integer.parseInt(textField3.getText()),textField2.getText());
				
			}
		});
		
		//*************** END Add action listeners
	}
	
	public void upload(String server, int port, String csv) {
		// TODO Auto-generated method stub
		System.out.println("connecting to "+server+":"+port);
		TupleSpace ts;

		try {
			ts = new TupleSpace(server, port);
			//	ts = new TupleSpace("127.0.0.1", 2525);
		//	ts = new TupleSpace("scy.collide.info", 2525);
			System.out.println("connected");
			Tuple template2 = new Tuple("action",Field.createWildCardField());
			int c = ts.count(template2);
			System.out.println("The server already contains "+c+" action tuples");
			System.out.println("Starting parsing and uploading");
			
			try {
					//String csvUrl="c:\\data\\doc\\infomedia.uib.no\\Scy project\\logs\\cyprus_lpv_3.csv";
				    String csvUrl = csv;
					BufferedReader in = new BufferedReader(new FileReader(csvUrl));
				    String str;
				    while ((str = in.readLine()) != null) {
				        String[] l = str.split(",");
				        for(int i = 0; i<l.length;i++){
				        	if(l[i] == "") l[i] = null;
				        }
				        if(l.length>6){
				        	ts.write(new Tuple("action","bla",Long.parseLong(l[0]), l[4], l[2], l[3], l[1],"kva", l[5], "key=value", "elo_uri="+l[6]));
				        }
				        else {
				        	if(l.length == 6)
				        		ts.write(new Tuple("action","bla",Long.parseLong(l[0]), l[4], l[2], l[3], l[1],"kva", l[5]));
				        	if(l.length < 6)
				        		System.out.println("cvs input error: not enough fields");
				        }
				    }
				    in.close();
				} catch (IOException e) {
				}
				int d = ts.count(template2);
				System.out.println((d-c)+" tuples uploaded");


		
		} catch (TupleSpaceException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	


}
