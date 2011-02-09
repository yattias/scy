package eu.scy.tools.math.ui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.plaf.FontUIResource;

import net.miginfocom.swing.MigLayout;

import org.apache.commons.lang.StringUtils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXHyperlink;
import org.jdesktop.swingx.JXLabel;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTextField;

import eu.scy.tools.math.ui.UIUtils;
import eu.scy.tools.math.ui.actions.FormulaHelpAction;
import eu.scy.tools.math.ui.actions.NotationHelpAction;
import eu.scy.tools.math.ui.paint.RoundedBorder;

public class Calculator extends JXPanel {

	private static final String _0_00 = "0.00";
	private static Logger log = Logger.getLogger("Calculator.class"); //$NON-NLS-1$
	private ArrayList<JXButton> symbolicButtons;
	private ArrayList<JXButton> adderButtons;
	private ArrayList<JXButton> numberButtons;
	private Map<String, ExpressionModel> expModels = new HashMap<String, ExpressionModel>();
	private Map<String, JXTextField> textFields = new HashMap<String, JXTextField>();
	private JXTextField sumTextField;
	private JXButton equalsButton;
	private JXButton radiusButton;
	private JXButton widthButton;
	private JXButton heightButton;
	private JXButton addButton;
	private JXButton subtractButton;
	private JXLabel resultLabel;
	private String type;
	private JXPanel calcButtonPanel;
	private Font buttonFont;
	private ExpressionModel expressionModel;
	private JXLabel textField;
	
	public Calculator(String type) {
		setLayout(new MigLayout("fill, inset 5 5 5 5"));
		this.type = type;
		init();
		initButtonPanel();
	}
	
	
	private void initButtonPanel() {
		  setCalcButtonPanel(new JXPanel(new BorderLayout(5,5)));
		    getCalcButtonPanel().setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
		    
		    setResultLabel(new JXLabel(_0_00));
		    this.modResultLabel(getResultLabel());
		    
		    JXPanel l = new JXPanel(new FlowLayout());
		    l.add(getResultLabel());
		    l.setOpaque(false);
		    getCalcButtonPanel().add(l,BorderLayout.WEST);
		    
		    
		    
		    setAddButton(new JXButton());
		    getAddButton().putClientProperty(UIUtils.TYPE, this.type);
		    getAddButton().setEnabled(false);
		    getAddButton().setToolTipText("Adds the calculation to the table.");

		    setSubtractButton(new JXButton());
		    getSubtractButton().putClientProperty(UIUtils.TYPE, this.type);
		    getSubtractButton().setToolTipText("Subtracts the calculation to the table.");
		    
		    JXPanel b = new JXPanel(new FlowLayout());
		    b.setOpaque(false);
//		    JXHyperlink notationHelpLink = new JXHyperlink(new NotationHelpAction());
//		    b.add(notationHelpLink);
		    
		    if( type.equals(UIUtils._2D)) {
		    	
				b.add(getSubtractButton());
				b.add(getAddButton());
				
		    }
		    getCalcButtonPanel().add(b, BorderLayout.EAST);
	}


	public void resetLabel() {
		this.resultLabel.setText(_0_00);
	}
	private void modResultLabel(JXLabel label) {
		Font font = label.getFont();
		Font bigFont = new FontUIResource(font.deriveFont(Font.BOLD, font.getSize2D() * 1.3f)); 
		label.setFont(bigFont);
		label.setForeground(Color.blue);
	}
	
	private void init() {
		this.setBorder(new RoundedBorder(5));
		
		
		setSumTextField(new JXTextField("Select a shape to start"));
		getSumTextField().setColumns(15);
		getSumTextField().setBackground(Color.WHITE);
		getSumTextField().setOpaque(true);
		getSumTextField().putClientProperty(UIUtils.TYPE, type);
		getSumTextField().setEnabled(true);
		textFields.put(type, getSumTextField());
		getSumTextField().addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
				// TODO Auto-generated method stub
				// System.out
//						.println("Calculator.init().new KeyListener() {...}.keyTyped()");
				 JXTextField source = (JXTextField) e.getSource();
		           // System.out.println("text " + source.getText());
				expressionModel.replaceExpression(source.getText());
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				// System.out
//						.println("Calculator.init().new KeyListener() {...}.keyReleased()");
				 JXTextField source = (JXTextField) e.getSource();
				 
				 int key = e.getKeyCode();
				 
		           // System.out.println("key " + key);
		           expressionModel.replaceExpression(source.getText());
			}
			
			@Override
			public void keyPressed(KeyEvent e) {
				// System.out.println(e.getKeyChar());
				 
				 int key = e.getKeyCode();
			        
			        if (key != KeyEvent.VK_ENTER && key != KeyEvent.VK_BACK_SPACE && key != KeyEvent.VK_DELETE) {
			           JXTextField source = (JXTextField) e.getSource();
			           expressionModel.replaceExpression(source.getText()+e.getKeyChar());
			           // System.out.println("exp " + expressionModel.getExpressionDisplay());
			           getResultLabel().setText(_0_00);
			           getAddButton().setEnabled(false);
//			        } else if (key == KeyEvent.VK_BACK_SPACE || key == KeyEvent.VK_DELETE) {
////				           JXTextField source = (JXTextField) e.getSource();
////				           String chop = StringUtils.chop(source.getText());
////				           expressionModel.replaceExpression(chop);
////				           // System.out.println("exp " + expressionModel.getExpressionDisplay());
////				           getResultLabel().setText(_0_00);
////				           getAddButton().setEnabled(false);
				        }
			        
				
			}
		});
		JXPanel eqPanel = new JXPanel(new MigLayout("insets 2 2 2 2"));
		eqPanel.setOpaque(false);
//		textField = new JXLabel();
//		textField.putClientProperty(UIUtils.TYPE, type);
//		textField.setPreferredSize(new Dimension(20,30));
//		textField.setBackgroundPainter(UIUtils.getSubPanelBackgroundPainter());
//		textField.setBackground(Color.white);
//		textField.setBorder(new LineBorder(Color.BLACK, 1));
//		textField.setText("Formula");
//		textField.setOpaque(true);
//		
		 setExpressionModel(new ExpressionModel());
		
		 expModels.put(type, expressionModel);
		
		
		 eqPanel.add(getSumTextField(),"growx, wrap");
		
		JXButton piButton = new JXButton("<html>&#960</html>");
		
		buttonFont = piButton.getFont().deriveFont(Font.BOLD, 12f);
		
		piButton.setName(ExpressionModel.PI);
		piButton.addActionListener(buttonAction);
		this.modSymbolButton(piButton);
		
		JXButton sqButton = new JXButton("<html>x<sup>2</sup></html>");
		sqButton.setName(ExpressionModel.cubedsym);
		sqButton.addActionListener(buttonAction);
		this.modSymbolButton(sqButton);
		
		JXButton sqrtButton = new JXButton("<html>&#8730</html>");
		sqrtButton.setName(ExpressionModel.SQRT);
		sqrtButton.addActionListener(buttonAction);
		
		this.modSymbolButton(sqrtButton);
		sqrtButton.setBackgroundPainter(UIUtils.getRootButtonPainter());
		
		JXButton cbrtButton = new JXButton("<html>&#179 &#8730;</html>");
		cbrtButton.setName(ExpressionModel.CBRT);
		cbrtButton.addActionListener(buttonAction);
		this.modSymbolButton(cbrtButton);
		cbrtButton.setBackgroundPainter(UIUtils.getRootButtonPainter());

		
		JXButton clearButton = new JXButton("<html>C</html>");
		clearButton.setName("c");
		this.modSymbolButton(clearButton);
		clearButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				clearForumla();
			}
		});
		
		
		JXButton deleteButton = new JXButton("<html>DEL</html>");
		deleteButton.setName("del");
		modSymbolButton(deleteButton);
		deleteButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				String text = sumTextField.getText();
				
				if( StringUtils.stripToNull(text) != null) {
					String chop = StringUtils.chop(text);
					sumTextField.setText(chop);
					expressionModel.replaceExpression(chop);
				}
					
			}
		});
		
		JXButton leftButton = new JXButton("<html>(</html>");
		leftButton.setName("(");
		leftButton.addActionListener(buttonAction);
		this.modRegularButton(leftButton);
		
		JXButton rightButton = new JXButton("<html>)</html>");
		rightButton.setName(")");
		rightButton.addActionListener(buttonAction);
		this.modRegularButton(rightButton);
		
		radiusButton = new JXButton("<html>r</html>");
		radiusButton.setName("r");
		radiusButton.addActionListener(buttonAction);
		this.modRegularButton(radiusButton);
		
		widthButton = new JXButton("<html>w</html>");
		widthButton.addActionListener(buttonAction);
		widthButton.setName("w");
		this.modRegularButton(widthButton);
		
		heightButton = new JXButton("<html>h</html>");
		heightButton.setName("h");
		heightButton.addActionListener(buttonAction);
		this.modRegularButton(heightButton);
		
		JXButton mulButton = new JXButton("<html>*</html>");
		mulButton.setName("*");
		mulButton.addActionListener(buttonAction);
		this.modSymbolButton(mulButton);
		
		JXButton pointButton = new JXButton("<html>.</html>");
		pointButton.setName(".");
		pointButton.addActionListener(buttonAction);
		this.modSymbolButton(pointButton);
		
		
		JXButton minusButton = new JXButton("<html>-</html>");
		minusButton.setName("-");
		minusButton.addActionListener(buttonAction);
		this.modSymbolButton(minusButton);
		
		JXButton divButton = new JXButton("<html>&#247</html>");
		divButton.setName("/");
		divButton.addActionListener(buttonAction);
		this.modSymbolButton(divButton);
		
		JXButton plusButton = new JXButton("<html>+</html>");
		plusButton.setName("+");
		plusButton.addActionListener(buttonAction);
		this.modSymbolButton(plusButton);
		
		JXButton eb = new JXButton("<html>=</html>");
		eb.setName("=");
		eb.putClientProperty(UIUtils.TYPE, type);
		setEqualsButton(eb);
		eb.setBackgroundPainter(UIUtils.getEqualButtonPainter());
		modSymbolButton(eb);
		
		JXButton enterButton = new JXButton("<html>&#8629</html>");
		enterButton.setName("=");
		enterButton.putClientProperty(UIUtils.TYPE, type);
		enterButton.setBackgroundPainter(UIUtils.getEqualButtonPainter());
		modSymbolButton(enterButton);
		
		
		
		
		JXButton fourButton = new JXButton("<html>4</html>");
		fourButton.setName("4");
		fourButton.addActionListener(buttonAction);
		this.modNumberButton(fourButton);
		
		JXButton fiveButton = new JXButton("<html>5</html>");
		fiveButton.setName("5");
		fiveButton.addActionListener(buttonAction);
		this.modNumberButton(fiveButton);
		
		JXButton threeButton = new JXButton("<html>3</html>");
		threeButton.setName("3");
		threeButton.addActionListener(buttonAction);
		this.modNumberButton(threeButton);
		
		JXButton nineButton = new JXButton("<html>9</html>");
		nineButton.setName("9");
		nineButton.addActionListener(buttonAction);
		this.modNumberButton(nineButton);
		
		JXButton oneButton = new JXButton("<html>1</html>");
		oneButton.setName("1");
		oneButton.addActionListener(buttonAction);
		this.modNumberButton(oneButton);
		
		JXButton twoButton = new JXButton("<html>2</html>");
		twoButton.setName("2");
		twoButton.addActionListener(buttonAction);
		this.modNumberButton(twoButton);
		
		JXButton eightButton = new JXButton("<html>8</html>");
		eightButton.setName("8");
		eightButton.addActionListener(buttonAction);
		this.modNumberButton(eightButton);
		
		JXButton sixButton = new JXButton("<html>6</html>");
		sixButton.setName("6");
		sixButton.addActionListener(buttonAction);
		this.modNumberButton(sixButton);
		
		JXButton zeroButton = new JXButton("<html>0</html>");
		zeroButton.setName("0");
		zeroButton.addActionListener(buttonAction);
		this.modNumberButton(zeroButton);
		
		JXButton sevenButton = new JXButton("<html>7</html>");
		sevenButton.setName("7");
		sevenButton.addActionListener(buttonAction);
		this.modNumberButton(sevenButton);
		
		
		JXPanel buttonPanel = new JXPanel(new MigLayout("insets 0 0 0 0"));
		//top row
		
		
		
		buttonPanel.add(piButton);
		buttonPanel.add(sqButton);
		buttonPanel.add(sqrtButton);
		buttonPanel.add(cbrtButton);
		buttonPanel.add(clearButton,"wrap");
		
		
		
		buttonPanel.add(minusButton);
		buttonPanel.add(plusButton);
		buttonPanel.add(mulButton);
		buttonPanel.add(divButton);
		buttonPanel.add(deleteButton ,"wrap");
		
		
		
	
		buttonPanel.add(leftButton);
		buttonPanel.add(rightButton);
		buttonPanel.add(radiusButton);
		buttonPanel.add(widthButton);
		buttonPanel.add(heightButton,"wrap");
		
		buttonPanel.add(fiveButton);
		buttonPanel.add(sixButton); 
		buttonPanel.add(sevenButton);
		buttonPanel.add(eightButton);
		buttonPanel.add(nineButton,"wrap");
		
		buttonPanel.add(zeroButton);
		buttonPanel.add(oneButton);
		buttonPanel.add(twoButton);
		buttonPanel.add(threeButton);
		buttonPanel.add(fourButton,"wrap");
		
		
		buttonPanel.add(pointButton);
//		eb.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				
//				log.info("new exp to display " + getExpressionModel().getExpressionDisplay());
//				log.info("new exp to EVAL " + getExpressionModel().getExpressionEval());
//				
//				
//			}
//		});
		buttonPanel.add(eb,"growx, span 4 0 , wrap");
		buttonPanel.setOpaque(false);
		
		
		buttonPanel.setOpaque(false);
		eqPanel.add(buttonPanel);
		
		
	
		this.setBackgroundPainter(UIUtils.getCalcBackgroundPainter());
		this.add(eqPanel, "top");
		
		JXPanel nPanel = new JXPanel(new MigLayout());
		nPanel.setOpaque(false);
		 JXHyperlink notationHelpLink = new JXHyperlink(new NotationHelpAction());
		nPanel.add(notationHelpLink,"wrap");
		
		nPanel.add(new JXLabel(),"wrap");
		
		nPanel.add(new JXHyperlink(new FormulaHelpAction(type)));
				
		this.add(nPanel, "top");
	}

	public void modSymbolButton(JXButton sButton) {
		sButton.setOpaque(true);
		
		if( !sButton.getName().equals("="))
			sButton.setBackgroundPainter(UIUtils.getSymbolButtonPainter());
		
		 
		
		sButton.setForeground(Color.WHITE);
		sButton.setBorderPainted(true);
		sButton.setBorder(new LineBorder(Color.WHITE, 1));
		sButton.setRolloverEnabled(false);
		this.modButton(sButton);
	}
	
	public void modRegularButton(JXButton regularButton) {
		regularButton.setForeground(Color.RED);
		regularButton.setBackgroundPainter(UIUtils.getSymbolButtonPainter());
		regularButton.setBorderPainted(true);
		regularButton.setBorder(new LineBorder(Color.WHITE, 1));
		this.modButton(regularButton);
	}
	
	public void modNumberButton(JXButton numberButton ){
		numberButton.setBackgroundPainter(UIUtils.getNumButtonPainter());
		numberButton.setForeground(Color.WHITE);
		numberButton.setBorderPainted(true);
		numberButton.setBorder(new LineBorder(Color.WHITE, 1));
		
		if( !numberButton.getName().equals("0"));
			this.modButton(numberButton);
	}

	private void modButton(JXButton button) {
		button.setFont(buttonFont);
		button.setPreferredSize(new Dimension(40, 30));
	}


	public void setSumTextField(JXTextField sumTextField) {
		this.sumTextField = sumTextField;
	}


	public JXTextField getSumTextField() {
		return this.sumTextField;
	}


	public void setEqualsButton(JXButton equalsButton) {
		this.equalsButton = equalsButton;
	}


	public JXButton getEqualsButton() {
		return equalsButton;
	}


	public void setCalcButtonPanel(JXPanel calcButtonPanel) {
		this.calcButtonPanel = calcButtonPanel;
	}


	public JXPanel getCalcButtonPanel() {
		return calcButtonPanel;
	}


	public void setAddButton(JXButton addButton) {
		this.addButton = addButton;
	}


	public JXButton getAddButton() {
		return addButton;
	}


	public void setSubtractButton(JXButton subtractButton) {
		this.subtractButton = subtractButton;
	}


	public JXButton getSubtractButton() {
		return subtractButton;
	}


	public void setResultLabel(JXLabel resultLabel) {
		this.resultLabel = resultLabel;
	}


	public void setResultValue(String result) {
		this.resultLabel.setText(result);
	}
	public JXLabel getResultLabel() {
		return resultLabel;
	}

	ActionListener buttonAction = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			getResultLabel().setText(_0_00);
			getAddButton().setEnabled(false);
			JXButton button = (JXButton) e.getSource();
			
			
			if( button.getName().equals(ExpressionModel.SQRT) || button.getName().equals(ExpressionModel.CBRT)) {
					String popupRootDialog = popupRootDialog();
					
				if( popupRootDialog != null )	
					getExpressionModel().addExpression(button.getName() + "(" +popupRootDialog + ")");
				
			} else {
				getExpressionModel().addExpression(button.getName());
			}
			
			setForumla(getExpressionModel().getExpressionDisplay());
			
		}
	};
	
	protected String popupRootDialog() {
		String showInputDialog = JOptionPane.showInputDialog(null, UIUtils.rootAddMessage, "Add a Root", JOptionPane.INFORMATION_MESSAGE);
		return showInputDialog;
	}


	public void setExpressionModel(ExpressionModel expressionModel) {
		this.expressionModel = expressionModel;
	}


	public ExpressionModel getExpressionModel() {
		return expressionModel;
	}
	
	public void setForumla(String forumla) {
		this.getSumTextField().setText(forumla);
		this.getExpressionModel().replaceExpression(forumla);
	}
	
	public String getForumla() {
		return this.getSumTextField().getText();
		
	}
	
	public void clearForumla() {
		this.getSumTextField().setText("");
		this.getExpressionModel().clear();
	}

	
	
}
