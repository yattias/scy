package eu.scy.tools.webbrowsingtool.client;

//import rocket.selection.client.Selection;
//import rocket.style.client.Css;
//import rocket.style.client.InlineStyle;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.HTTPRequest;
import com.google.gwt.user.client.ResponseTextHandler;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.widgets.Button;
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

@SuppressWarnings("deprecation")
public class WebBrowsingTool extends Panel implements EntryPoint {

	private TextField address;
	
	private Panel panel;

	private Toolbar topToolbar;

	private Toolbar bottomToolbar;

	private String documentHTML;
	
	public static final String INITIAL_URL = "http://www.google.de";

	public WebBrowsingTool() {
		buildGUI();

	}

	public void onModuleLoad() {
	}

	private void buildGUI() {

		setLayout(new FitLayout());

		// building address-panel
		topToolbar = new Toolbar();
		Label addressLabel = new Label();
		addressLabel.setText("URL:");
		address = new TextField("URL:", "URL:", 200);
		address.setBlankText("incorrect URL. Please retype");
		address.focus();
		address.setHideLabel(false);
		address.setVtype(VType.URL);

		address.addListener(new TextFieldListenerAdapter() {

			public void onSpecialKey(Field field, EventObject e) {
				if (e.getKey() == EventObject.ENTER) {
					loadURL(address.getValueAsString());
				}
			}

		});

		ToolbarButton go = new ToolbarButton("Go!",
				new ButtonListenerAdapter() {

					public void onClick(Button button, EventObject e) {
						loadURL(address.getValueAsString());
					}

				});

		ToolbarButton highlight = new ToolbarButton("Highlight Selection!",
				new ButtonListenerAdapter() {
					public void onClick(Button button, EventObject e) {
//						final Selection selection = Selection.getSelection();
//						final Element element = DOM.createSpan();
//						
//						final InlineStyle inlineStyle = 		
//					                            InlineStyle.getInlineStyle(element);
////						inlineStyle.setString(Css.FONT_SIZE, "larger");
//						inlineStyle.setString(Css.BACKGROUND_COLOR, "#FFFF66");
//						selection.surround(element);

					}
				});
		
		topToolbar.addText("URL:");
		topToolbar.addField(address);
		topToolbar.addButton(go);
		topToolbar.addButton(highlight);

		//This is our frame
		panel = new Panel();
		
		loadURL(INITIAL_URL);
		
		// adding components to the viewport
		add(panel);
		setTopToolbar(topToolbar);

		@SuppressWarnings("unused")
		Viewport vp = new Viewport(this);

	}
	
	private void loadURL(String url){
		
		//new HTTPRequest receiving HTML
		HTTPRequest.asyncGet(correctURL(url), new ResponseTextHandler(){

			public void onCompletion(String arg0) {
				System.out.println(arg0);
				panel.setHtml(arg0);
				
			}
			
		});
		
		address.setValue(url);
	}

	private String correctURL(String url) {
		if (url.startsWith("www.")) {
			url = ("http://" + url);
		}
		return url;
	}
}
