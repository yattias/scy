/**
 * 
 */
package eu.scy.lab.client.usermanagement;

import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.gwtext.client.core.EventObject;
import com.gwtext.client.data.Record;
import com.gwtext.client.data.SimpleStore;
import com.gwtext.client.data.Store;
import com.gwtext.client.widgets.Button;
import com.gwtext.client.widgets.DatePicker;
import com.gwtext.client.widgets.event.ButtonListenerAdapter;
import com.gwtext.client.widgets.event.DatePickerListenerAdapter;
import com.gwtext.client.widgets.form.ComboBox;

import com.gwtext.client.widgets.form.event.ComboBoxCallback;  
import com.gwtext.client.widgets.form.DateField;
import com.gwtext.client.widgets.form.Field;
import com.gwtext.client.widgets.form.FieldSet;
import com.gwtext.client.widgets.form.FormPanel;
import com.gwtext.client.widgets.form.TextField;
import com.gwtext.client.widgets.form.VType;
import com.gwtext.client.widgets.form.event.ComboBoxListenerAdapter;

/**
 * @author Sven Manske
 * 
 */
public class CreateUserPanel extends FormPanel {

	/**
	 * 
	 */
	private Button register;
	private  CreateUserConstants constants;
	
	public CreateUserPanel() {
		// Localization
		constants = (CreateUserConstants) GWT.create(CreateUserConstants.class);

		createFields();
		createButtons();
	}

	public void createFields() {

		setFrame(true);
		setTitle(constants.createUserTitle());

		// Two FieldSets for grouping (userdata - logindata)
		FieldSet logindata = new FieldSet(constants.logindata());
		FieldSet userdata = new FieldSet(constants.userdata());

		// LOGIN-DATA
		TextField username = new TextField(constants.userName(), "username");
		username.setAllowBlank(false);
		username.setWidth(190);
		logindata.add(username);

		TextField password = new TextField(constants.password(), "password");
		password.setAllowBlank(false);
		password.setInputType("password");
		password.setWidth(190);
		logindata.add(password);

		TextField passwordRetype = new TextField(constants.reEnterPassword(),
				"passwordRetype");
		passwordRetype.setAllowBlank(false);
		passwordRetype.setInputType("password");
		passwordRetype.setWidth(190);
		logindata.add(passwordRetype);

		// USER-DATA
		
		//The Combo-box (title)
		ComboBox title = new ComboBox(constants.title(), "title");
		title.setEmptyText(constants.selectTitle());
		title.setWidth(190);
		
		// create a Store using local array data (for titles)
		String[] titleData = new String[3];
		titleData[0]=constants.titleMale();
		titleData[1]=constants.titleFemale();
		titleData[2]=constants.other();
		
		final Store store = new SimpleStore("title", titleData);
		store.load();
		
		//Combobox attributes
		title.setForceSelection(true);
		title.setMinChars(1);
		title.setStore(store);
		title.setDisplayField("title");
		title.setMode(ComboBox.LOCAL);
		title.setTriggerAction(ComboBox.ALL);
		title.setEmptyText(constants.selectTitle());
		title.setLoadingText(constants.loading());
		title.setTypeAhead(true);
		title.setSelectOnFocus(true);
		title.setHideTrigger(false);
		
		//Combobox-Listener-Adapter
		//TODO integrate some functionality???
		title.addListener(new ComboBoxListenerAdapter() {
			public boolean doBeforeQuery(ComboBox comboBox, ComboBoxCallback cb) {
				System.out.println("ComboBox::doBeforeQuery()");
				return true;
			}

			public boolean doBeforeSelect(ComboBox comboBox, Record record,
					int index) {
				System.out.println("ComboBox::doBeforeSelect("
						+ record.getAsString("states") + ")");
				return super.doBeforeSelect(comboBox, record, index);
			}

			public void onCollapse(ComboBox comboBox) {
				System.out.println("ComboBox::onCollapse()");
			}

			public void onExpand(ComboBox comboBox) {
				System.out.println("ComboBox::onExpand()");
			}

			public void onSelect(ComboBox comboBox, Record record, int index) {
				System.out.println("ComboBox::onSelect('"
						+ record.getAsString("states") + "')");
			}

			public void onBlur(Field field) {
				System.out.println("ComboBox::onBlur()");
			}

			public void onChange(Field field, Object newVal, Object oldVal) {
				System.out.println("ComboBox::onChange(" + oldVal + "-->"
						+ newVal + ")");
			}

			public void onFocus(Field field) {
				System.out.println("ComboBox::onFocus()");
			}

			public void onInvalid(Field field, String msg) {
				super.onInvalid(field, msg);
			}

			public void onSpecialKey(Field field, EventObject e) {
				System.out.println("ComboBox::onSpecialKey(key code "
						+ e.getKey() + ")");
			}
		});
		userdata.add(title);

		TextField firstName = new TextField(constants.firstName(), "firstName");
		firstName.setAllowBlank(false);
		firstName.setWidth(190);
		userdata.add(firstName);

		TextField surName = new TextField(constants.surName(), "surName");
		surName.setAllowBlank(true);
		surName.setWidth(190);
		userdata.add(surName);

		TextField lastName = new TextField(constants.lastName(), "lastName");
		lastName.setAllowBlank(false);
		lastName.setWidth(190);
		userdata.add(lastName);

		TextField email = new TextField(constants.email(), "email");
		email.setAllowBlank(false);
		email.setVtype(VType.EMAIL);
		email.setWidth(190);
		userdata.add(email);

		DateField datefield = new DateField(constants.birth(), "birth", 190);
		datefield.setAllowBlank(false);
		datefield.setWidth(190);
		userdata.add(datefield);

		add(logindata);
		add(userdata);

	}

	public void createButtons() {

		// Listener-Adapter for the Buttons
		// TODO Insert real actions
		ButtonListenerAdapter buttonListener = new ButtonListenerAdapter() {
			public void onClick(Button button, EventObject e) {
				if (button.equals(register)) {
					Window.alert("register geklickt");
				}

			}

		};

		register = new Button(constants.register(), buttonListener);

		// storing Buttons in Array to set them as FormPanel-Buttons
		Button[] buttons = new Button[1];
		buttons[0] = register;

		setButtons(buttons);
		// setMinButtonWidth(50);
		// setDraggable(true);

		setShadow(true);
		setPaddings(5);
	}

	// TODO Implement register-procedure
	public void register() {
		Window.alert("Registrierung durchgeführt");
	}


}
