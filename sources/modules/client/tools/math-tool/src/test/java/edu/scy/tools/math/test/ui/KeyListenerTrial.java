package edu.scy.tools.math.test.ui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.jdesktop.swingx.JXTextField;

import net.miginfocom.swing.MigLayout;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.util.KeyStrokeUtil;

public class KeyListenerTrial {

	public static void main(String[] args) {
		JFrame frame = new JFrame("mathTool"); //$NON-NLS-1$
		frame.setLayout(new MigLayout("fill,insets 0 0 0 0")); //$NON-NLS-1$
		// JScrollPane scrollPane = new JScrollPane(doInit());
		//				
		// scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		// scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		
		JXTextField tf = new JXTextField();
		tf.addKeyListener(new KeyAdapter() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				
				 if (e.isShiftDown()) {
					System.out.println("shift down"); 
					System.out.println("shift down keyText " + e.getKeyChar() + e.getKeyCode());
				 } else {
					 String keyText = e.getKeyText(e.getKeyCode());
					 System.out.println("a keyText " + e.getKeyChar() + e.getKeyCode());
				 }
				 
				 System.out.println("opens keyText " + e.getKeyChar() + e.getKeyCode());
				
			}
		});
		tf.setColumns(5);
		frame.add(tf);
		
		// when you close the frame, the app exits
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// center the frame and show it
		frame.setLocationRelativeTo(null);
		frame.pack();
		frame.setVisible(true);
	}
	
}
