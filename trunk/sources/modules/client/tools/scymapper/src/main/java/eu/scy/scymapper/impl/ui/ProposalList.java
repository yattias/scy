package eu.scy.scymapper.impl.ui;

import java.awt.Color;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ProposalList extends JPanel {

	private List<ProposalEntry> proposalList = null;

	private int proposalCount = 0;

	private int maxLength;
	
	public ProposalList(int maxLength) {
		this.proposalList = new ArrayList<ProposalEntry>();
		this.maxLength = maxLength;
		this.setLayout(new GridLayout(maxLength, 1));
		this.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
	}
	
	public boolean addEntry(ProposalEntry entry, int index) {
	    if (index >= maxLength) {
	        return false;
	    }
	    ProposalEntry deletedEntry = null;
	    if (index < proposalList.size()) {
	        deletedEntry = proposalList.get(index);
	    }
	    if (deletedEntry != null) {
	        remove(deletedEntry);
	        proposalList.set(index, entry);
	    } else {
	        proposalCount++;
	        proposalList.add(entry);
	    }
	    entry.setBorder(new LineBorder(Color.RED, 3));
            add(entry, index);
            validate();
            return true;
	}

        public boolean removeEntry(ProposalEntry entry) {
            boolean removed = proposalList.remove(entry);
            if (removed) {
                proposalCount--;
                remove(entry);
            }
            return removed;
        }
	
	public ProposalEntry getEntry(int i) {
		return proposalList.get(i);
	}
	
	public List<ProposalEntry> getEntries() {
		return proposalList;
	}
	
	public void clear() {
		proposalCount = 0;
		proposalList.clear();
		removeAll();
	}

    public void addEntries(List<ProposalEntry> newEntries) {
        ArrayList<Integer> remainingIndexes = new ArrayList<Integer>();
        ArrayList<Integer> notInsertedIndexes = new ArrayList<Integer>();
        for (int i = 0; i < proposalList.size(); i++) {
            proposalList.get(i).setBorder(new EmptyBorder(3,3,3,3));
            String proposalText = proposalList.get(i).getTextLabel().getText();
            for (int j = 0; j < newEntries.size(); j++) {
                if (proposalText.equals(newEntries.get(j).getTextLabel().getText())) {
                    remainingIndexes.add(i);
                    notInsertedIndexes.add(j);
                    break;
                }
            }
        }
       for (int i = 0; i < newEntries.size(); i++) {
           if (!notInsertedIndexes.contains(i)) {
               ProposalEntry insertedEntry = newEntries.get(i);
               int insertionIndex = -1;
               for (int j = 0; j < maxLength; j++) {
                   if (!remainingIndexes.contains(j)) {
                       remainingIndexes.add(j);
                       insertionIndex = j;
                       break;
                   }
               }
               if (insertionIndex != -1) {
                   addEntry(insertedEntry, insertionIndex);
               }
           }
       }
    }
}
