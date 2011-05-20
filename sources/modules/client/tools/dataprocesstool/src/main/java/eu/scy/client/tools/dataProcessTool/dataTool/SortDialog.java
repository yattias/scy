/*
 * SortDialog.java
 *
 * Created on 19 novembre 2008, 10:18
 */

package eu.scy.client.tools.dataProcessTool.dataTool;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Vector;
import javax.swing.*;
import eu.scy.client.tools.dataProcessTool.utilities.ElementToSort;
import java.awt.Component;
import java.awt.FlowLayout;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * sort dialog : 3 keys  max
 * @author  Marjolaine Bodin
 */
public class SortDialog extends javax.swing.JDialog {
    private final Logger logger = Logger.getLogger(SortDialog.class.getName());
    /* owner */
    private FitexToolPanel owner ;
    /* list keys to sort */
    private Vector listOfColumns ;
    /* texte for "no cirteria" */
    private String noText ;
    /* name of the first criteria to select */
    private String selColumn;

    // panel can be hide and their state
    JPanel thirdPanel = null;
    boolean thirdPanelHidden = false;
    
    /*first key sort
      * if keySort1 == null then no sort to execute.
     */
    private ElementToSort keySort1=null;

    /**
    * second key sort
    * if keySort2== null then there is no 2nd criteria to sort
    */
    private  ElementToSort keySort2=null;

    /**
    * .
    * third key sort
    * if keySort3 == null  then there is no 3rd criteria to sort.
    */
    private ElementToSort keySort3=null;

    int idKey1=0,idKey2=-1,idKey3=-1;
    boolean isKey1Croi=true,isKey2Croi=true,isKey3Croi=true;

    public SortDialog(FitexToolPanel owner, Vector listOfColumns, String selColumn) {
        super();
        this.owner = owner;
        this.listOfColumns = listOfColumns;
        this.selColumn = selColumn;
        noText = owner.getBundleString("LABEL_SORT_NO") ;
        this.listOfColumns.addElement(noText);
        initComponents();
        setLocationRelativeTo(owner);
        setModal(true);
        setResizable(false);
        init();
    }
    
    
    /** Creates new form SortDialog */
    public SortDialog(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }

    /* dialog inisitalization*/
    private void init(){
        panelSort.setLayout(new BoxLayout(panelSort, BoxLayout.Y_AXIS));
	panelSort.setFont(new Font("Dialog",1,12));

        panelSort.add(getKeyJPanel(owner.getBundleString("LABEL_SORT_BY"),isKey1Croi,"comboKey1"), "Panel de la 1er cle");
	panelSort.add(getKeyJPanel(owner.getBundleString("LABEL_SORT_THEN_BY"),isKey2Croi,"comboKey2"), "Panel de la 2e cle");
        thirdPanel = getKeyJPanel(owner.getBundleString("LABEL_SORT_THEN_BY"),isKey3Croi,"comboKey3") ;
	panelSort.add(thirdPanel, "Panel de la 3e cle");
        
        // button bar ok/cancel
	JPanel buttonPanel = new JPanel();
	buttonPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
	buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
	buttonPanel.add(Box.createHorizontalGlue());
	buttonPanel.add(getJButton(24,215,owner.getBundleString("BUTTON_OK")), "Bouton OK");
	buttonPanel.add(Box.createHorizontalGlue());
	buttonPanel.add(getJButton(152, 215,owner.getBundleString("BUTTON_CANCEL")), "Bouton Annuler");
	buttonPanel.add(Box.createHorizontalGlue());

	panelSort.add(Box.createRigidArea(new Dimension(10, 16)));
	panelSort.add(buttonPanel);
        this.setPanelsVisibility();
        if (selColumn != null){
            Component [] lesComposants = panelSort.getComponents();
            JPanel key1Panel = (JPanel) lesComposants[0];
            ((JComboBox) ((JPanel)(key1Panel.getComponents()[0])).getComponents()[0]).setSelectedItem(selColumn);
        }
    }
    
    /**
     * .
     * Instanciation of a sub JPanel containing combo box with columns to sort and 2
     * check box to specify the sort : "Ascending" and "DEscencding".
     * @return JPanel : sub panel
     */
 
    private JPanel getKeyJPanel(String texte, boolean isCroi,String name) {
	JPanel keyPanel = null;
	try{
            keyPanel = new javax.swing.JPanel();
            keyPanel.setFont(new Font("Dialog",0,9));
		
            /*border*/
            javax.swing.border.Border blackline= BorderFactory.createLineBorder(Color.black);
            javax.swing.border.TitledBorder title = BorderFactory.createTitledBorder(blackline,texte);
            title.setTitleFont(new Font("Dialog",1,12));
		
            keyPanel.setBorder(title);
            keyPanel.setLayout(new BoxLayout(keyPanel, BoxLayout.X_AXIS));
			
            //keyPanel.add(getJComboBox(name),name);
            JPanel panelCB = new JPanel();
            panelCB.setLayout(new FlowLayout());
            panelCB.add(getJComboBox(name),name);
            keyPanel.add(panelCB);
        
            keyPanel.add(Box.createRigidArea(new Dimension(10, 0)));
			
            JPanel vPane = new JPanel();
            vPane.setLayout(new BoxLayout(vPane, BoxLayout.Y_AXIS));
					
            JRadioButton croi = getJRadioButton(owner.getBundleString("LABEL_SORT_ASCENDING"),isCroi),
            decroi=getJRadioButton(owner.getBundleString("LABEL_SORT_DESCENDING"),!isCroi);
            /*
            *Button group
            */
            ButtonGroup group = new ButtonGroup();
            group.add(croi);group.add(decroi);

            vPane.add(croi, owner.getBundleString("LABEL_SORT_ASCENDING"));
            vPane.add(decroi, owner.getBundleString("LABEL_SORT_DESCENDING"));

            keyPanel.add(vPane);
	} catch (java.lang.Throwable t) {
            // System.out.println(t);
            logger.log(Level.SEVERE, "SortDialog getKeyJPanel error"+t);
	}
	return keyPanel;
    }
    
    /**
     * .
     * Instanciation of the radiobutton
     * @param texte String : text of the radiobutton
     * @param isSelect boolean : "true" if selected
     * @return JRadioButton : the instanciate radiobutton
     */

    private JRadioButton getJRadioButton(String texte, boolean isSelect){
        JRadioButton croi=null;
        try{
            croi = new JRadioButton(texte,isSelect);
            croi.setName(texte);
            croi.setFont(new Font("Dialog",1,12));
        }
        catch (Throwable t) {
            // System.out.println(t);
            logger.log(Level.SEVERE, "SortDialog getJRadioButton error"+t);
        }
        return croi;
    }

    /**
     * .
     * Instanciation of the combo box :
     * @param name String  : name of the combobox
    * @return JComboBox : the instanciate combobox
     */

    private JComboBox  getJComboBox(String name){
	try {
        Vector listOfColumnsC = new Vector();
        for (int i=0; i<listOfColumns.size(); i++){
            listOfColumnsC.add(new String((String)listOfColumns.get(i)));
        }
		JComboBox comboBox = new JComboBox(listOfColumnsC);
		comboBox.setName(name);
		comboBox.setFont(new Font("Dialog",0,12));
        
		if (name.equals("comboKey1")){
			comboBox.removeItem(noText);
			comboBox.setSelectedIndex(idKey1);
			
		}
		else if (name.equals("comboKey2")){
			if (idKey2 == -1)
                comboBox.setSelectedItem(noText);
			else
                comboBox.setSelectedIndex(idKey2);
			
		}
		else if (comboBox.getName().equals("comboKey3")){
			if (idKey3 == -1)
                comboBox.setSelectedItem(noText);
			else
                comboBox.setSelectedIndex(idKey3);
			
		}
		comboBox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setPanelsVisibility();
			}
		});

		return comboBox;
	}
	catch (java.lang.Throwable t) {
		// System.out.println(t);
            logger.log(Level.SEVERE, "SortDialog getJComboBox error"+t);
	}
	return null;
    }


    /**
    * .
    * Instanciation of a button
    * @param x int : horizontal position
    * @param y int : vertical position
    * @param texte String :text button.
    * @return JButton : button
    */
    private JButton getJButton(int x, int y, String texte) {
	JButton button = null;
	try
	{
            button = new javax.swing.JButton() {
                @Override
			public Dimension getMinimumSize() {
                            return new Dimension (85, 25);
			}
                @Override
			public Dimension getPreferredSize() {
                            return new Dimension (85, 25);
			}
		};
		button.setName(texte);
		button.setText(texte);
		button.setFont(new Font("Dialog",1,12));

		button.addActionListener(
			new ActionListener()
			{
                            @Override
                            public void actionPerformed(ActionEvent e)
				{
                                    if (e.getActionCommand().equals(owner.getBundleString("BUTTON_OK"))){
                                        actionOk();
                                    }else if (e.getActionCommand().equals(owner.getBundleString("BUTTON_CANCEL"))){
                                        actionCancel();
                                    }
                                }
                        }
                );


	} catch (java.lang.Throwable e){
		// System.out.println("Error construction button "+e);
            logger.log(Level.SEVERE, "SortDialog getJCombogetJButtonBox error"+e);
	}
	return button;
}


    /* cancel action */
    private void actionCancel(){
        this.setVisible(false);
        this.dispose();
    }

    /* ok action*/
    private void actionOk(){
        Component [] lesComposants = panelSort.getComponents();
	JPanel key1Panel = (JPanel) lesComposants[0];
	JPanel key2Panel = (JPanel) lesComposants[1];
	JPanel key3Panel = null;
	if (!thirdPanelHidden)
            key3Panel = (JPanel) lesComposants[2];

        /* first key */
        /* column */
	String column1 = (String) ((JComboBox) ((JPanel)(key1Panel.getComponents()[0])).getComponents()[0]).getSelectedItem();
        if (column1==null || column1.equals("") || column1.equals(" ")){
            this.setVisible(false);
            this.dispose();
            return ;
	}
        idKey1= ((JComboBox) ((JPanel)(key1Panel.getComponents()[0])).getComponents()[0]).getSelectedIndex();
        /* order*/
	JRadioButton croi1 = (JRadioButton) ((JPanel) key1Panel.getComponents()[2]).getComponents()[0];
	/* 0 : descending sort
	 *  1 : ascending sort */
	int order1 = ( (isKey1Croi = croi1.isSelected()) ? 1 : 0);

	keySort1 = new ElementToSort(column1,order1);

	/* 2nd key */
	/* column */
	String column2 = (String) ((JComboBox) ((JPanel)(key2Panel.getComponents()[0])).getComponents()[0]).getSelectedItem();
	if (!column2.equals(noText)){
            idKey2 = ((JComboBox) ((JPanel)(key2Panel.getComponents()[0])).getComponents()[0]).getSelectedIndex();
            /* order*/
            JRadioButton croi2 = (JRadioButton) ((JPanel) key2Panel.getComponents()[2]).getComponents()[0];
            int order2 = ( (isKey2Croi=croi2.isSelected()) ? 1 : 0);
            keySort2 = new ElementToSort(column2,order2);
	}

	/* 3rd keye */
	/* column */
        if (!thirdPanelHidden) {
            String column3 = (String) ((JComboBox) ((JPanel)(key3Panel.getComponents()[0])).getComponents()[0]).getSelectedItem();
            if (!column3.equals(noText)){
                idKey3 = ((JComboBox) ((JPanel)(key3Panel.getComponents()[0])).getComponents()[0]).getSelectedIndex();
		/* order*/
		JRadioButton croi3 = (JRadioButton) ((JPanel) key3Panel.getComponents()[2]).getComponents()[0];
		int order3 = ( (isKey3Croi=croi3.isSelected()) ? 1 : 0);
		keySort3 = new ElementToSort(column3,order3);

            }
	}
        
        owner.executeSort(keySort1, keySort2, keySort3);
        this.setVisible(false);
        this.dispose();
    }


    public void setPanelsVisibility() {
        Component [] lesComposants = panelSort.getComponents();

        JPanel key1Panel, key2Panel, key3Panel;
	String column1, column2, column3 = null;
	boolean hideThirdPanel, hidePeriodPanel = true;
	int i = 0;

	key1Panel = (JPanel) lesComposants[i++];
	//column1 = (String) ((JComboBox) (key1Panel.getComponents()[0])).getSelectedItem();
        column1 = (String) ((JComboBox) ((JPanel)(key1Panel.getComponents()[0])).getComponents()[0]).getSelectedItem();

	key2Panel = (JPanel) lesComposants[i++];
	//column2 = (String) ((JComboBox) (key2Panel.getComponents()[0])).getSelectedItem();
        column2 = (String) ((JComboBox) ((JPanel)(key2Panel.getComponents()[0])).getComponents()[0]).getSelectedItem();

	if (!thirdPanelHidden) {
            key3Panel = (JPanel) lesComposants[i++];
            //column3 = (String) ((JComboBox) (key3Panel.getComponents()[0])).getSelectedItem();
            column3 = (String) ((JComboBox) ((JPanel)(key3Panel.getComponents()[0])).getComponents()[0]).getSelectedItem();
	}


        // si the option "no cirteria" is selected, then hide the next combobox
	hideThirdPanel = column2.equals(noText);
	if (hideThirdPanel != thirdPanelHidden) {
            if (hideThirdPanel) {
                panelSort.remove(thirdPanel);
            }
            else {
		panelSort.add(thirdPanel, 2);
            }
            thirdPanelHidden = hideThirdPanel;
	}


        this.setResizable(true);
        this.pack();
        this.setResizable(false);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelSort = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(this.owner.getBundleString("TITLE_DIALOG_SORT"));

        panelSort.setLayout(new javax.swing.BoxLayout(panelSort, javax.swing.BoxLayout.Y_AXIS));
        getContentPane().add(panelSort, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    /**
    * @param args the command line arguments
    */
    public static void main(String args[]) {
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                SortDialog dialog = new SortDialog(new javax.swing.JFrame(), true);
                dialog.addWindowListener(new java.awt.event.WindowAdapter() {
                    public void windowClosing(java.awt.event.WindowEvent e) {
                        System.exit(0);
                    }
                });
                dialog.setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel panelSort;
    // End of variables declaration//GEN-END:variables

}
