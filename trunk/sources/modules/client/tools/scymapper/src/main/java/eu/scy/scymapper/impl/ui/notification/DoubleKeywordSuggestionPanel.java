package eu.scy.scymapper.impl.ui.notification;

import java.awt.GridLayout;
import java.util.Collection;
import java.util.List;

import eu.scy.scymapper.api.INodeFactory;
import eu.scy.scymapper.impl.ui.ConceptMapPanel;


public class DoubleKeywordSuggestionPanel extends KeywordSuggestionPanel {

    private KeywordSuggestionPanel relationPanel;
    
    private KeywordSuggestionPanel conceptPanel;
    
    public DoubleKeywordSuggestionPanel() {
        super(false);
        setLayout(new GridLayout(2,1));
        relationPanel = new KeywordSuggestionPanel();
        conceptPanel = new KeywordSuggestionPanel();
        add(conceptPanel);
        add(relationPanel);
    }
    
    @Override
    public void setSuggestions(List<String> keywords, Collection<INodeFactory> nodeFactories, ConceptMapPanel panel, String type) {
        if (type.equals("concepts")) {
            conceptPanel.setSuggestions(keywords, nodeFactories, panel, type);
        } else {
            relationPanel.setSuggestions(keywords, nodeFactories, panel, type);
        }
    }
    
}
