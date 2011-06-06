package eu.scy.scymapper.impl.ui;

import javax.swing.JLabel;

public class KeywordLabel extends JLabel {

	private String keyword;
	
	public KeywordLabel(String link, String keyword) {
		super("<html><a href=\"#\">" + link + "</a></html>");
		this.keyword = keyword;
	}
	
	public String getKeyword() {
		return keyword;
	}
	
	public void setKeyword(String newKeyword) {
		this.keyword = newKeyword;
	}
	
	public void setLink(String newLink) {
		this.setText("<html><a href=\"#\">" + newLink + "</a></html>");
	}
}
