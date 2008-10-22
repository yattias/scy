package eu.scy.tools.webbrowsingtool.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.http.client.URL;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.ui.Frame;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.core.Ext;
import com.gwtext.client.core.ExtElement;
import com.gwtext.client.data.HttpProxy;
import com.gwtext.client.util.DOMUtil;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.HTMLPanel;
import com.gwtext.client.widgets.Panel;
import com.gwtext.client.widgets.Toolbar;
import com.gwtext.client.widgets.ToolbarButton;
import com.gwtext.client.widgets.Viewport;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.Label;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;
import com.gwtext.client.widgets.layout.FitLayout;

public class WebBrowsingTool extends Panel implements EntryPoint {

	private TextField address;
	
	private Frame frame;
	
	private Toolbar topToolbar;
	
	private Toolbar bottomToolbar;
	
	private String documentHTML;
	
	public WebBrowsingTool(){
		buildGUI();
		
	}
	
	public void onModuleLoad() {
}

	private void buildGUI() {
		
		setLayout(new FitLayout());
		
		//building address-panel
		topToolbar = new Toolbar();
		Label addressLabel = new Label();
		addressLabel.setText("URL:");
		address = new TextField("URL:","URL:",200);
		address.setBlankText("incorrect URL. Please retype");
		address.focus();
		address.setHideLabel(false);
		address.setVtype(VType.URL);
		
		address.addListener(new TextFieldListenerAdapter(){

			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey()==EventObject.ENTER){
					loadURL();
				}
			}
			
		});
		
		ToolbarButton go = new ToolbarButton("Go!",new ButtonListenerAdapter(){

			public void onClick(Button button, EventObject e) {
				loadURL();
			}
			
		});
		
		topToolbar.addText("URL:");
		topToolbar.addField(address);
		topToolbar.addButton(go);
		
		//building frame-panel
		frame = new Frame("http://www.google.com");
		
		//adding components to the viewport
		add(frame);
		setTopToolbar(topToolbar);
		
		@SuppressWarnings("unused")
		Viewport vp = new Viewport(this);
		
	}
	
	
	private void correctURL() {
		String url = address.getText();
		if (url.startsWith("www.")) {
			address.setValue("http://"+url);
		}
	}
	
	private void loadURL(){
		correctURL();
		frame.setUrl(address.getText());
		DOMUtil.setID(frame, "frame");
		
//		DOM dom = new DOM();
//		String html = dom.getInnerHTML(this.getElement());
//		System.out.println("html:"+html);
		
		String html = DOM.getInnerHTML(this.getElement());
		System.out.println(html);
	}
	
	

}
