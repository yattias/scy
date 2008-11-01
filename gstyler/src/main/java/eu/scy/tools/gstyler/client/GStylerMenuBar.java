package eu.scy.tools.gstyler.client;

import com.google.gwt.user.client.Command;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MenuBar;
import com.google.gwt.user.client.ui.MenuItem;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

import eu.scy.tools.gstyler.client.common.GraphApplication;

public class GStylerMenuBar extends MenuBar {

    public GStylerMenuBar(GraphApplication gstyler) {
        setAnimationEnabled(true);
        setAutoOpen(true);

        MenuBar helpMenu = new MenuBar(true);
        helpMenu.addItem(new MenuItem("About", new Command() {

            public void execute() {
                final DialogBox dialogBox = new DialogBox();
                dialogBox.setText("About");
                VerticalPanel dialogContents = new VerticalPanel();
                dialogContents.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
                dialogContents.setSpacing(20);
                dialogBox.add(dialogContents);

                dialogContents.add(new Label(GStyler.VERSION));

                // Add a close button at the bottom of the dialog
                Button closeButton = new Button("Close", new ClickListener() {

                    public void onClick(Widget sender) {
                        dialogBox.hide();
                    }
                });
                dialogContents.add(closeButton);
                DOM.setStyleAttribute(dialogBox.getElement(), "zIndex", "100");
                dialogBox.center();
                dialogBox.show();
            }

        }));
        addItem(new MenuItem("File", helpMenu));
    }
}
