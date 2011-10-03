


import javax.swing.JFrame;

import javax.swing.SwingUtilities;






public class CityRun {

	
	public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                makeGUI(); 
            }
        });
    }
	
	private static void makeGUI() {
        MyFrame f = new MyFrame("metro");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
    } 
	

}
