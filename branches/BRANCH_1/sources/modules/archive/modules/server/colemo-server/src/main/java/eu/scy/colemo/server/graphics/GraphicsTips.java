/*
 * Created on 20.okt.2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package eu.scy.colemo.server.graphics;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JToolBar;

/**
 * @author Øystein
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GraphicsTips extends JFrame implements ActionListener{

	private JButton next;
	private JButton previous;
	private JButton ok;
	private JLabel abs;
	private JLabel inter;
	private JLabel association1;
	private JLabel association2;
	private JToolBar toolbar;
	private String type;
	
	private Vector a = new Vector();
	private Vector b = new Vector();
	private Vector c = new Vector();
	private Vector d = new Vector();
	Vector images = new Vector();
	
	private int currentPicture=0;
	
	public GraphicsTips(String type) {
		super("TIP: UML relations");
		this.setSize(370,390);
		this.type=type;
		URL url=null;		
		Class GraphicsTips=this.getClass();
		
		url=GraphicsTips.getResource("bulb.png");
		
		ImageIcon image = new ImageIcon(url);
	    this.setIconImage(image.getImage());
		
			
		next = new JButton("Learn more >>");
		previous = new JButton("<< Back");
		ok = new JButton("Ok");
		next.addActionListener(this);
		previous.addActionListener(this);
		ok.addActionListener(this);
		
		url=GraphicsTips.getResource("abstract.jpg");
		abs = new JLabel(new ImageIcon(url));
		
		url=GraphicsTips.getResource("interface.jpg");
		inter = new JLabel(new ImageIcon(url));
		
		url=GraphicsTips.getResource("ass.jpg");
		association1 = new JLabel(new ImageIcon(url));
		
		url=GraphicsTips.getResource("ass2.jpg");
		association2 = new JLabel(new ImageIcon(url));
		
		a.add(abs); a.add(inter); a.add(association1); a.add(association2);
		b.add(inter); b.add(abs); b.add(association1); b.add(association2);
		c.add(association1); c.add(association2); c.add(abs); c.add(inter);
		d.add(association2); d.add(association1); d.add(abs); d.add(inter);
		toolbar = new JToolBar();
		
		setUpFrame(type);
		System.out.println("BAKGRUNN: "+getBackground());
	}
	public void setUpFrame(String type){
		if(type.equals("abstract")){
			images=a;
		}
		else if(type.equals("interface")){
			images=b;
		}
		else if(type.equals("association1")){
			images=c;
		}
		else if(type.equals("association2")){
			images=d;
		}
		
		this.getContentPane().add((JLabel)images.elementAt(currentPicture),BorderLayout.CENTER);
	
		this.getContentPane().add(toolbar,BorderLayout.SOUTH);
		toolbar.add(previous);
		toolbar.add(ok);
		toolbar.add(next);
		this.setVisible(true);
	}
	
	public void nextPage() {
		if(currentPicture+1==images.size()){
			currentPicture=0;
		}
		else{
			currentPicture++;
		}
		this.getContentPane().removeAll();
		this.getContentPane().add((JLabel)images.elementAt(currentPicture),BorderLayout.CENTER);
		
		this.getContentPane().add(toolbar,BorderLayout.SOUTH);
		toolbar.add(previous);
		toolbar.add(ok);
		toolbar.add(next);
		this.setVisible(true);
		this.repaint();
	}
	public void previousPage() {
		if(currentPicture==0){
			currentPicture=images.size()-1;
		}
		else{
			currentPicture--;
		}
		this.getContentPane().removeAll();
		this.getContentPane().add((JLabel)images.elementAt(currentPicture),BorderLayout.CENTER);
		
		this.getContentPane().add(toolbar,BorderLayout.SOUTH);
		toolbar.add(previous);
		toolbar.add(ok);
		toolbar.add(next);
		this.setVisible(true);
		this.repaint();
		
	}
	
	public void actionPerformed(ActionEvent ae) {
		if(ae.getSource()==next){
			nextPage();
		}
		if(ae.getSource()==previous) {
			previousPage();
		}
		if(ae.getSource()==ok) {
			this.dispose();
		}
	}
}
