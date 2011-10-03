import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.event.ActionListener;
import java.awt.event.MouseListener;
import java.awt.geom.GeneralPath;


import javax.swing.*;


public class MyInfoBubble extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1521697603688783156L;
	private JEditorPane textVisualisation;
	private int textX, textY, textWidth, textHeight, offsetX,offsetY, strokeSize,x,y;
	private Color bgColor;
	private int focusX, focusY, legLength, bubbleWidth, bubbleHeight, globalWidth,
				globalHeight;
	private GeneralPath envelope;
	private JButton closeButt;

	public MyInfoBubble(int foX, int foY, int w, int h){
		super();
		init();
		String s = "<html><center><b>Station 4</b></center><br><b>Made by:</b> Jimmy<br><b>Made with:</b> scissors<br><b>Creation time:</b> 0:47, April 17th 1668</html>";
		updateStructure(foX,foY,40,w,h,s);
	}
	public MyInfoBubble(int foX, int foY, int ll, int w, int h){
		super();
		init();
		String s = "<html><center><b>Station 4</b></center><br><b>Made by:</b> Jimmy<br><b>Made with:</b> scissors<br><b>Creation time:</b> 0:47, April 17th 1668</html>";
		updateStructure(foX,foY,ll,w,h,s);
	}
	
	public MyInfoBubble(int foX, int foY, int w, int h, String s){
		super();
		init();
		updateStructure(foX,foY,40,w,h,s);
	}
	
	public MyInfoBubble(int foX, int foY, int ll, int w, int h, String s){
		super();
		init();
		updateStructure(foX,foY,ll,w,h,s);
	}
	
	private void init(){
			bgColor = new Color(245, 250, 255);
			setOpaque(false);
			textVisualisation = new JEditorPane();
			textVisualisation.setEditable(false);
			textVisualisation.setContentType("text/html");
			textVisualisation.setBackground(bgColor);
			setLayout(null);
			add(textVisualisation,-1);
			textVisualisation.setVisible(true);
			closeButt = new JButton("<html><font face=arial size=2>x</font></html>");
			closeButt.setMargin(new Insets(1, 1, 5, 1));
			add(closeButt,getComponentCount());
			setComponentZOrder(textVisualisation, 1);
			setComponentZOrder(closeButt, 0);
			closeButt.setVisible(true);
			validate();
			Component C = getParent();
			if(C != null) C.validate();
		
	}
		
	public void updateStructure(int foX, int foY, int ll,int w, int h, String s) {
		bubbleHeight=h;
		bubbleWidth = w;
		globalWidth=w;
		legLength=ll;
		globalHeight=bubbleHeight+legLength;
		focusX=w/5;
		focusY=globalHeight-3;
		strokeSize=1;
		offsetY = 7;
		offsetX = 7;
		textX=offsetX;
		textWidth = w-offsetX-textX;
		textY=offsetY;
		textHeight= h-offsetY-textY;
		x = foX-bubbleWidth/5;
		y =  foY-globalHeight;
		textVisualisation.setText(s);
		textVisualisation.setBounds(textX, textY, textWidth, textHeight);
		closeButt.setBounds(w-21, 8, 13, 13);
		setBounds(x,y, globalWidth, globalHeight);
		envelope = new GeneralPath();
		generateEnvelope();
		
		
        
        
	}
	
	public void hideCloseButton(){
		closeButt.setVisible(false);
	}
	
	public void showCloseButton(){
		closeButt.setVisible(true);
	}
	
	public int getFocusX(){
		return x + bubbleWidth/5;
	}
	
	public int getFocusY(){
		return y + globalHeight;
	}
	
	public void setFocusX(int fx){
		x = fx - bubbleWidth/5;
		setBounds(x, y, globalWidth, globalHeight);
	}
	
	public void setFocusY(int fy){
		y = fy - globalHeight; 
		setBounds(x, y, globalWidth, globalHeight);
	}
	
	public void myUpdate(){
		setBounds(x, y,globalWidth,globalHeight);
	}
	
	public void moveLocation(int dx, int dy) {
		x=x+dx; 
		y=y + dy;
		setBounds(x, y,globalWidth,globalHeight);
		
		
	}
	
	private void generateEnvelope(){
		
		int xPP[] = {bubbleWidth*6/15,focusX,bubbleWidth*4/15,15};
		int yPP[] = {bubbleHeight+-strokeSize,focusY,bubbleHeight-strokeSize,bubbleHeight-strokeSize};
        envelope.moveTo(xPP[0], yPP[0]);
        for(int i=0;i<xPP.length;i++){
        	envelope.lineTo(xPP[i], yPP[i]);
        }
        envelope.quadTo( strokeSize, bubbleHeight-strokeSize,strokeSize, bubbleHeight-strokeSize-15);
        envelope.lineTo(strokeSize, strokeSize+15);
        envelope.quadTo( strokeSize, strokeSize, strokeSize + 15, strokeSize);
        envelope.lineTo(bubbleWidth-strokeSize-15, strokeSize);
        envelope.quadTo(bubbleWidth-strokeSize,strokeSize, bubbleWidth-strokeSize, strokeSize+15);
        envelope.lineTo(bubbleWidth-strokeSize, bubbleHeight-strokeSize-15);
        envelope.quadTo(bubbleWidth-strokeSize,bubbleHeight-strokeSize, bubbleWidth-strokeSize-15,bubbleHeight-strokeSize);
        envelope.closePath();
	}
	
	public void setText(String s){
		textVisualisation.setText(s);
	}
	
	public void show(int foX, int foY, int ll,int w, int h, String s) {
		setVisible(false);
		updateStructure(foX, foY, ll, w, h, s);
		setVisible(true);
	}
	
	protected void paintComponent(Graphics g) {
        
        Graphics2D gr = (Graphics2D) g;
        gr.setColor(Color.red);
		gr.setStroke(new BasicStroke(strokeSize));
		gr.setColor(bgColor);
		/*
		RoundRectangle2D block = new RoundRectangle2D.Double(strokeSize, strokeSize, textWidth+2*(offsetX-strokeSize), textHeight+2*(offsetY-strokeSize), 25, 25);
		
		gr.fill(block);
		gr.setColor(Color.gray);
		gr.draw(block);
		
		GeneralPath triang = new GeneralPath();
		int xP[] = {focusX,(textWidth+2*offsetX)*6/15,(textWidth+2*offsetX)*4/15,focusX};
		int yP[] = {focusY,textHeight+2*(offsetY-strokeSize),textHeight+2*(offsetY-strokeSize),focusY};
        triang.moveTo(xP[0], yP[0]);
        for(int i=0;i<xP.length;i++){
        	triang.lineTo(xP[i], yP[i]);
        }
        triang.closePath();
        gr.setColor(bgColor);
        gr.fill(triang);
        */
        
        gr.fill(envelope);
        gr.setColor(Color.gray);
        gr.draw(envelope);
      super.paintComponent(g);
    }
	
	public void setMouseListener(MouseListener l) {
		textVisualisation.addMouseListener(l);
		
	}
	
	public void setActionListener(ActionListener l) {
		closeButt.addActionListener(l);
	}
	
}
