package eu.scy.scymapper.impl.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Arrays;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;

public class ProposalList extends JPanel {

	private ProposalEntry[] proposalList = null;

	private int proposalCount = 0;
	
	public ProposalList(int entries) {
		this.proposalList = new ProposalEntry[entries];
		this.setLayout(new GridLayout(entries, 1));
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}
	
	public boolean addEntry(ProposalEntry entry) {
		if(proposalCount == proposalList.length) {
			return false;
		}

		for(int i = 0; i < proposalList.length; i++) {
			if(proposalList[i] == null) {
				proposalList[i] = entry;
				add(proposalList[i]);
				proposalCount++;
				return true;
			}
		}
		return false;
	}

	public ProposalEntry getEntry(int i) {
		return proposalList[i];
	}
	
	public ProposalEntry[] getEntries() {
		return proposalList;
	}
	
	public void clear() {
		Arrays.fill(proposalList, null);
		proposalCount = 0;
	}
}
