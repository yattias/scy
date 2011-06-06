package eu.scy.tools.math.ui.actions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JDialog;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXEditorPane;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTitledPanel;

import eu.scy.tools.math.controller.MathToolController;
import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.images.Images;

public class ExportToGoogleSketchUpAction extends AbstractAction {

	
	
	private MathToolController mathToolController;
	public ExportToGoogleSketchUpAction(MathToolController mathToolController) {
		putValue(Action.NAME, "Export for Google Sketchup");
		putValue(Action.SMALL_ICON, Images.SketchUp.getIcon());
		putValue(Action.SHORT_DESCRIPTION, "Create a Floor Plan for Google Sketchup");
		this.mathToolController = mathToolController;
	}
	@Override
	public void actionPerformed(ActionEvent e) {

		JComponent source = (JComponent) e.getSource();
		String path = mathToolController.export2DCanvasPNG();
//		System.out.println("ExportToGoogleSketchUpAction.actionPerformed()");
		final JDialog jd = new JDialog();
		jd.setTitle("Google Sketchup Import");
		jd.setResizable(false);
		jd.setModal(true);
		URL resource = ExportToGoogleSketchUpAction.class.getResource(UIUtils.SKETCHUP_FILE);
		
		
		
		try {
			JXEditorPane editor = new JXEditorPane(resource);
			editor.setEditable(false);
			JXPanel outerPanel = new JXPanel(new BorderLayout(5,5));
			outerPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
			outerPanel.setBorder(BorderFactory.createLineBorder(Color.white, 5));
			
			
			JXTitledPanel p = new JXTitledPanel("Import Your floor plan into Google Sketchup.");
			p.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
			UIUtils.setModTitlePanel(p);
			JXPanel ep = new JXPanel(new BorderLayout(5,5));
			ep.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
			ep.setBorder(BorderFactory.createLineBorder(Color.white, 5));
			
			
			ep.add(new JXLabel("<html><body>&nbsp;Your floor plan file has been saved to: " + path +"</body></html>"), BorderLayout.NORTH);
			ep.add(editor,BorderLayout.CENTER);
			p.add(ep);
			
			
			JXPanel buttonPanel = new JXPanel(new FlowLayout(FlowLayout.RIGHT));
			buttonPanel.setOpaque(false);
			buttonPanel.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
			JXButton ok = new JXButton("OK");
			ok.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					jd.setVisible(false);
				}
			});
			buttonPanel.add(ok);
			outerPanel.add(p,BorderLayout.CENTER);
			outerPanel.add(buttonPanel, BorderLayout.SOUTH);
		     jd.getContentPane().add(outerPanel);
		     jd.setSize(new Dimension(800, 400));
		     jd.setVisible(true);
		} catch (IOException e1) {
			e1.printStackTrace();
		} 
		
		
	}
	
   
    

}
