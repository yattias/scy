package eu.scy.scymapper.impl.configuration;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.api.diagram.model.INodeModel;
import eu.scy.scymapper.api.diagram.model.INodeModelConstraints;
import eu.scy.scymapper.api.styling.INodeStyle;
import eu.scy.scymapper.impl.model.ConnectorModel;
import eu.scy.scymapper.impl.model.DefaultNodeStyle;

import javax.swing.*;

/**
 * Created by IntelliJ IDEA.
 * User: Bjoerge
 * Date: 30.okt.2009
 * Time: 13:25:49
 */
public class ConnectorFactory implements INodeFactory {
	private String name;
	private String description;
	private int height = 25;
	private int width = 100;
	private INodeStyle nodeStyle = new DefaultNodeStyle();
	private Icon icon;

	@Override
	public Icon getIcon() {
		if (icon == null) {
			icon = new ImageIcon(getClass().getResource("/eu/scy/scymapper/impl/icons/connector.png"));
		}
		return icon;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public void setNodeStyle(INodeStyle nodeFactory) {
		this.nodeStyle = nodeFactory;
	}

	public INodeStyle getNodeStyle() {
		return nodeStyle;
	}

	@Override
	public INodeModel create() {
		ConnectorModel node = new ConnectorModel();
		node.setLabel("New connector");
		node.setWidth(width);
		node.setHeight(height);

		INodeStyle style = new DefaultNodeStyle();
		style.setBackground(nodeStyle.getBackground());
		style.setForeground(nodeStyle.getForeground());
		style.setMinHeight(nodeStyle.getMinHeight());
		style.setMinWidth(nodeStyle.getMinWidth());
		style.setOpaque(nodeStyle.isOpaque());
		style.setStroke(nodeStyle.getStroke());
		style.setBorder(nodeStyle.getBorder());
		style.setPaintShadow(nodeStyle.getPaintShadow());

		node.setConstraints(new Constraints());
		node.setStyle(style);

		return node;
	}

	private static class Constraints implements INodeModelConstraints {

		@Override
		public boolean getCanMove() {
			return true;
		}

		@Override
		public void setCanMove(boolean b) {
		}

		@Override
		public boolean getCanDelete() {
			return true;
		}

		@Override
		public void setCanDelete(boolean b) {

		}

		@Override
		public boolean getCanResize() {
			return true;
		}

		@Override
		public void setCanResize(boolean b) {

		}

		@Override
		public boolean getCanEditLabel() {
			return true;
		}

		@Override
		public void setCanEditLabel(boolean b) {

		}

		@Override
		public boolean getCanConnect() {
			return true;
		}

		@Override
		public void setCanConnect(boolean b) {

		}

		@Override
		public void setCanSelect(boolean canSelect) {

		}

		@Override
		public boolean getCanSelect() {
			return true;
		}

		@Override
		public void setCanChangeStyle(boolean b) {

		}

		@Override
		public boolean getCanChangeStyle() {
			return true;
		}

		@Override
		public void setAlwaysOnTop(boolean alwaysOnTop) {

		}

		@Override
		public boolean getAlwaysOnTop() {
			return false;
		}
	}
}