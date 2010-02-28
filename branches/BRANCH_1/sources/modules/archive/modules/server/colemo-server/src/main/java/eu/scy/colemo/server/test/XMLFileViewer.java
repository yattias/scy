/*
 * Created on 11.jul.2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.test;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;



/**
 * @author Roger
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class XMLFileViewer extends JFrame implements ActionListener{
	private String directory, file;
	private JTextArea txtarea;
	private final JFileChooser fc=new JFileChooser();
	private JScrollPane scrollPane;
	
	public XMLFileViewer(){ //bruker denne nå, men kan bruke "hovedkonstruktøren" om vi vil ha bare det xml
		this(null, null);	//som genereres for den spesifikk session
	}
	public XMLFileViewer(String filename){
		this(null, filename);
	}
	public XMLFileViewer(String directory, String filename){
		super();
				
		addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				dispose();
			}
		});
		
		//Creating TextArea with JScrollPane
		txtarea = new JTextArea("", 24, 80);
		txtarea.setBackground(new Color(238,238,238));
		txtarea.setFont(new Font("MonoSpaced", Font.PLAIN, 12));
		txtarea.setEditable(false);
		this.scrollPane=new JScrollPane(txtarea);
		this.scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.scrollPane.setBounds(10, 10, 300, 200);
		this.getContentPane().add("Center", this.scrollPane);
		
		//Panel for buttons
		JPanel panel = new JPanel();
		panel.setLayout(new FlowLayout(FlowLayout.RIGHT, 10, 5));
		this.getContentPane().add(panel, "South");
		
		//Buttons
		JButton openfile = new JButton("Open file");
		JButton close = new JButton("Close");
		JButton refresh = new JButton("Refresh");
		openfile.addActionListener(this);
		openfile.setActionCommand("open");
		close.addActionListener(this);
		close.setActionCommand("close");
		refresh.addActionListener(this);
		refresh.setActionCommand("refresh");
		panel.add(openfile);
		panel.add(close);
		panel.add(refresh);
		
		this.pack();
		
		
		if(directory == null){
			File f;
			if((filename != null) && (f = new File(filename)).isAbsolute()){
				directory = f.getParent();
				filename = f.getName();
			}
			else directory = System.getProperty("user.dir");
		}
		this.directory = directory;
		this.setTitle("XML Viewer");
		this.setVisible(true);
		setFile(directory, filename);
	}
	
	//Load and display selected file
	public void setFile(String directory, String filename){
		if((filename == null) || (filename.length() == 0)) return;
		File f;
		FileReader in = null;
		
		try{
			f = new File(directory, filename);
			in = new FileReader(f);
			char[] buffer = new char[4096];
			int len;
			txtarea.setText("");
			while((len = in.read(buffer)) != -1){
				String s = new String(buffer, 0, len);
				txtarea.append(s);
			}
			this.setTitle("XML Viewer - " + filename);
			txtarea.setCaretPosition(0); //start of file
		}
		catch(IOException e){
			txtarea.setText(e.getClass().getName() + ": " + e.getMessage());
			this.setTitle("Fileviewer: " + filename + ": I/O Exception");
            e.printStackTrace();
		}
		finally{
			try{
				if(in != null) in.close();
			}
			catch(IOException e){
			    e.printStackTrace();
			}
		}
	}			
	
	public void actionPerformed(ActionEvent e) {
		String cmd = e.getActionCommand();
		if(cmd.equals("open")){
			int returnVal = fc.showOpenDialog(this);
			if(returnVal == JFileChooser.APPROVE_OPTION) {
		    file = fc.getSelectedFile().getName();
		    }			
			directory = fc.getCurrentDirectory().getPath();
			setFile(directory, file);
			
		}
		else if (cmd.equals("close"))this.dispose();
		else if (cmd.equals("refresh")){
			setFile(directory, file);
			}
	}
	static public void main(String[] args) throws IOException{
		JFrame f = new XMLFileViewer((args.length == 1)?args[0]:null);
		f.addWindowListener(new WindowAdapter(){
			public void windowClosed(WindowEvent e){
				System.exit(0);
			}
		});
		f.setVisible(true);
	}

}
