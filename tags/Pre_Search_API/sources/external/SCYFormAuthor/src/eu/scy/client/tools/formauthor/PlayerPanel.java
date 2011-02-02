package eu.scy.client.tools.formauthor;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import com.sun.media.jai.opimage.AddCollectionCRIF;

public class PlayerPanel extends JPanel {

	private Component visualComponent;

	public PlayerPanel(final byte[] audioArray) {
		JButton jbtnSave3gp = new JButton("Save 3gp file");
		jbtnSave3gp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String wd = System.getProperty("user.dir");
				JFileChooser fc = new JFileChooser(wd);
				int rc = fc.showDialog(null, "3GP speichern");
				if (rc == JFileChooser.APPROVE_OPTION) {
					File file = fc.getSelectedFile();
					String filename = file.getAbsolutePath();
					// call your function heredf
					FileOutputStream fos;
					try {
						fos = new FileOutputStream(file);
						fos.write(audioArray);
						fos.close();
					} catch (Exception e2) {
						// TODO Auto-generated catch block
						e2.printStackTrace();
					}

				} else
					System.out.println("File chooser cancel button clicked");
				return;
			}
	
		});
		add(jbtnSave3gp);

	}

}
