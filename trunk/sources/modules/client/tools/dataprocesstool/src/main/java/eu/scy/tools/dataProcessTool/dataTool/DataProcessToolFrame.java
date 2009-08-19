/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package eu.scy.tools.dataProcessTool.dataTool;

import eu.scy.tools.dataProcessTool.utilities.CopexReturn;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Dimension;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;
import roolo.elo.JDomStringConversion;

/**
 *
 * @author Marjolaine
 */
public class DataProcessToolFrame extends JFrame implements OpenDataAction{
    private DataProcessToolPanel dataProcessPanel;
    private File lastUsedFile = null;
    private XMLOutputter xmlOutputter = new XMLOutputter(Format.getPrettyFormat());
    private transient SAXBuilder builder = new SAXBuilder(false);

    // menu
    private JMenuBar menuBar;
    private JMenu menuData;
    private JMenuItem menuItemNew;
    private JMenuItem menuItemOpen;
    private JMenuItem menuItemSave;
    private JMenuItem menuItemQuit;

    public DataProcessToolFrame() {
        super();
        initGUI();
        initDataProcessTool();
    }

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        // Initialisation du look and feel
        try{
            String myLookAndFeel=UIManager.getSystemLookAndFeelClassName();
            UIManager.setLookAndFeel(myLookAndFeel);
        }catch(Exception e){
            System.out.println("ERREUR dans l'initialisation du lookAndFeel : "+e) ;
            //JOptionPane.showMessageDialog(this , "ERREUR ans l'initialisation du lookAndFeel : "+e, "ERROR",JOptionPane.ERROR_MESSAGE);
        }
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DataProcessToolFrame dataFrame =new DataProcessToolFrame();
                dataFrame.setVisible(true);
                dataFrame.load();
            }
        });

    }


    public void load(){
        dataProcessPanel.loadData();
        setSize(310, 335);
    }

    private void initDataProcessTool(){
        dataProcessPanel = new DataProcessToolPanel();
        add(dataProcessPanel, BorderLayout.CENTER);
        setSize(DataProcessToolPanel.PANEL_WIDTH, DataProcessToolPanel.PANEL_HEIGHT);
    }

    private void initGUI(){
        setMinimumSize(new Dimension(310, 335));
        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Data Processing Tool");
        setLayout(new BorderLayout());
        
        menuBar = new JMenuBar();
        menuData = new JMenu();
        menuData.setText("Data");
        menuItemNew = new JMenuItem("New");
        menuItemNew.setHorizontalTextPosition(SwingConstants.LEFT);
        menuItemNew.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataProcessNew();
            }
        });
        menuData.add(menuItemNew);
        menuItemOpen = new JMenuItem("Open...");
        menuItemOpen.setHorizontalTextPosition(SwingConstants.LEFT);
        menuItemOpen.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataProcessOpen();
            }
        });
        menuData.add(menuItemOpen);
        menuItemSave = new JMenuItem("Save...");
        menuItemSave.setHorizontalTextPosition(SwingConstants.LEFT);
        menuItemSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataProcessSave();
            }
        });
        menuData.add(menuItemSave);
        menuItemQuit = new JMenuItem("Quit");
        menuItemQuit.setHorizontalTextPosition(SwingConstants.LEFT);
        menuItemQuit.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dataProcessQuit();
            }
        });
        menuData.add(menuItemQuit);
        menuBar.add(menuData);
        setJMenuBar(menuBar);
    }

    private void dataProcessQuit(){
        this.dispose();
    }

    private void dataProcessSave(){
        Element pds = dataProcessPanel.getPDS() ;

        JFileChooser aFileChooser = new JFileChooser();
        if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
        int r = aFileChooser.showSaveDialog(this);
        if (r == JFileChooser.APPROVE_OPTION){
            File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
			FileWriter fileWriter = null;
			try
			{
				fileWriter = new FileWriter(file);
				xmlOutputter.output(pds, fileWriter);
			}
			catch (IOException e)
			{
				dataProcessPanel.displayError(new CopexReturn("Erreur lors de la sauvegarde", false), "Erreur");
			}
			finally
			{
				if (fileWriter != null)
					try
					{
						fileWriter.close();
					}
					catch (IOException e)
					{
						dataProcessPanel.displayError(new CopexReturn("Erreur lors de la sauvegarde, fermeture du fichier", false), "Erreur");
					}
			}
        }
    }

   

    public void loadELO(String xmlContent){
        this.dataProcessPanel.loadELO(xmlContent);
        setSize(this.dataProcessPanel.getWidth(), this.dataProcessPanel.getHeight());
    }


    private void dataProcessOpen(){
        OpenDataDialog openDataDialog = new OpenDataDialog(dataProcessPanel);
        openDataDialog.addOpenDataAction(this);
        openDataDialog.setVisible(true);
    }

    private void dataProcessNew(){
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                DataProcessToolFrame dataFrame =new DataProcessToolFrame();
                dataFrame.setVisible(true);
                dataFrame.load();
            }
        });

    }

    @Override
    public void openELO() {
        JFileChooser aFileChooser = new JFileChooser();
		if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = aFileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION){
			File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
			FileReader fileReader = null;
			try
			{
				fileReader = new FileReader(file);
				Document doc = builder.build(fileReader, file.getAbsolutePath());
                DataProcessToolFrame dataFrame =new DataProcessToolFrame();
                dataFrame.setVisible(true);
                dataFrame.load();
                dataFrame.loadELO(new JDomStringConversion().xmlToString(doc.getRootElement()));
			}
			catch (Exception e)
			{
				e.printStackTrace();
                dataProcessPanel.displayError(new CopexReturn("Erreur durant le chargement "+e, false), "Erreur");
			}
			finally
			{
				if (fileReader != null)
					try
					{
						fileReader.close();
					}
					catch (IOException e)
					{
						dataProcessPanel.displayError(new CopexReturn("Erreur durant le chargement, fermeture fichier "+e, false), "Erreur");
					}
			}
		}
    }

    @Override
    public void importCSVFile() {
        JFileChooser aFileChooser = new JFileChooser();
		if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = aFileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION){
			File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
            eu.scy.elo.contenttype.dataset.DataSet dsElo = dataProcessPanel.importCSVFile(file);
            if(dsElo != null){
                DataProcessToolFrame dataFrame =new DataProcessToolFrame();
                dataFrame.setVisible(true);
                dataFrame.load();
                dataFrame.loadELO(new JDomStringConversion().xmlToString(dsElo.toXML()));
            }
		}
    }

    @Override
    public void mergeDataset() {
        JFileChooser aFileChooser = new JFileChooser();
		if (lastUsedFile != null)
			aFileChooser.setCurrentDirectory(lastUsedFile.getParentFile());
		int userResponse = aFileChooser.showOpenDialog(this);
		if (userResponse == JFileChooser.APPROVE_OPTION){
			File file = aFileChooser.getSelectedFile();
			lastUsedFile = file;
			FileReader fileReader = null;
			try
			{
				fileReader = new FileReader(file);
				Document doc = builder.build(fileReader, file.getAbsolutePath());
                dataProcessPanel.mergeELO(doc.getRootElement());
			}
			catch (Exception e)
			{
				e.printStackTrace();
                dataProcessPanel.displayError(new CopexReturn("Erreur durant le chargement "+e, false), "Erreur");
			}
			finally
			{
				if (fileReader != null)
					try
					{
						fileReader.close();
					}
					catch (IOException e)
					{
						dataProcessPanel.displayError(new CopexReturn("Erreur durant le chargement, fermeture fichier "+e, false), "Erreur");
					}
			}
		}
    }
}
