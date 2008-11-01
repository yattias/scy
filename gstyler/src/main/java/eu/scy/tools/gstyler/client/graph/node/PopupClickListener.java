package eu.scy.tools.gstyler.client.graph.node;

import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.ui.ClickListener;
import com.google.gwt.user.client.ui.PopupPanel;
import com.google.gwt.user.client.ui.Widget;


public class PopupClickListener implements ClickListener {

    private NodeView<?> nodeView;
    NodePopup popup = null;

    public PopupClickListener(NodeView<?> nodeView) {
        this.nodeView = nodeView;
    }

    public void onClick(Widget sender) {
        if (popup != null) {
            popup.hide();
            popup = null;
            return;
        }

        popup = new NodePopup(nodeView.getNode());
        DOM.setStyleAttribute(popup.getElement(), "zIndex", "100");

        popup.setPopupPositionAndShow(new PopupPanel.PositionCallback() {
          public void setPosition(int offsetWidth, int offsetHeight) {
            int left = nodeView.getNode().getParentGraph().getWidgetLeft(nodeView);
            int top = nodeView.getNode().getParentGraph().getWidgetTop(nodeView);
            popup.setPopupPosition(left+30, top-10);
          }
        });
      }
}
