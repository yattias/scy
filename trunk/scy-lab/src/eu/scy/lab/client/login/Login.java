/**
 * 
 */
package eu.scy.lab.client.login;



import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.event.TextFieldListenerAdapter;

/**
 * @author Sven Manske
 * 
 */
public class Login extends FormPanel {
    
    private Button login;
    private Button register;
    private Button passwordForgotten;
    private LoginConstants constants;
    private TextField username;
    private TextField password;
    
    public Login() {
        // Localization
        constants = (LoginConstants) GWT.create(LoginConstants.class);
        createFields();
        createButtons();
    }
    
    public Login(String username, String password) {
        this();
        this.username.setValue(username);
        this.password.setValue(password);
    }
    
    public void createFields() {
        
        setIconCls("scylogo16x16");
        setFrame(true);
        setTitle(constants.loginTitle());
        
        username = new TextField(constants.userName(), "username");
        username.setAllowBlank(false);
        add(username);
        
        // FIXME KeyLister doesnt work
        //		int keyCode=112;
        
        
        username.addListener(new TextFieldListenerAdapter(){
            
        });
        
        password = new TextField(constants.password(), "password");
        password.setAllowBlank(false);
        password.setInputType("password");
        add(password);
        
    }
    
    public void createButtons() {
        
        // Listener-Adapter for the Buttons
        // TODO Insert real actions
        
        login = new Button(constants.login(), new ButtonListenerAdapterImpl(this, "login"));
        register = new Button(constants.register(), new ButtonListenerAdapterImpl(this, "register"));
        passwordForgotten = new Button(constants.pwForgotten(), new ButtonListenerAdapterImpl(this, "passwordForgotten"));
        
        // storing Buttons in Array to set them as FormPanel-Buttons
        Button[] buttons = new Button[3];
        buttons[0] = login;
        buttons[1] = register;
        buttons[2] = passwordForgotten;
        
        setButtons(buttons);
        // setMinButtonWidth(50);
        // setDraggable(true);
        
        setShadow(true);
        setPaddings(5);
        
    }
    
    public VerticalPanel getCentredLoginDialog(){
        
        //Positioning in Center
        //wrap Login-Panel in vertical Panel
        VerticalPanel verticalPanel  = new VerticalPanel();
        verticalPanel.setWidth("100%");
        verticalPanel.setHeight("100%");
        verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        verticalPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
        HorizontalPanel horizontalPanel = new HorizontalPanel();
        verticalPanel.add(horizontalPanel);
        horizontalPanel.add(this);
        
        return verticalPanel;
    }
    
    /**
     * Returns the user name typed into the textfield.
     * 
     * @return the user name typed into the textfield
     */
    public String getUsername() {
        return username.getText();
    }
    
    /**
     * Returns the password typed into the textfield.
     * 
     * @return the password typed into the textfield
     */
    public String getPassword() {
        return password.getText();
    }
    
}
