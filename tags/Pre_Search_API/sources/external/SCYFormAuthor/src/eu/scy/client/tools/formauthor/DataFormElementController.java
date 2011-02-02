package eu.scy.client.tools.formauthor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DataFormElementController {
	private DataFormModel dfm;
	private DataFormElementModel dfem;
	private DataFormElementView dfev;

	public DataFormElementController(DataFormElementModel dfem,
			DataFormElementView dfev, DataFormModel dfm) {
		this.dfem = dfem;
		this.dfev = dfev;
		this.dfm = dfm;

		dfev.addTitleChangeListener(new TitleChangeListener());
		// dfev
		// .addDeleteDataFormElementListener(new
		// DeleteDataFormElementListener());
		// dfev
		// .addMoveUpDataFormElementListener(new
		// MoveUpDataFormElementListener());
		// dfev
		// .addMoveDownDataFormElementListener(new
		// MoveDownDataFormElementListener());
		dfev.addTypeChangeListener(new TypeChangeListener());
		dfev.addCardinalityChangeListener(new CardinalityChangeListener());
		dfev.addEditEventsListener(new EditEvents());
	}

	private class EditEvents implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			DataFormEvents dfee = new DataFormEvents(dfem);
		}
	}

	private class CardinalityChangeListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
		}

		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			dfem.setCardinality(dfev.getCardinality());
		}

		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			dfem.setCardinality(dfev.getCardinality());
		}

	}

	private class MoveUpDataFormElementListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfm.moveElementUp(dfem);
		}
	}

	private class MoveDownDataFormElementListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfm.moveElementDown(dfem);
		}
	}

	private class DeleteDataFormElementListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfm.removeDataField(dfem);
		}
	}

	private class TypeChangeListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfem.setType(dfev.getType());
		}
	}

	private class TitleChangeListener implements DocumentListener {

		public void changedUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub

		}

		public void insertUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			dfem.setTitle(dfev.getTitle());
		}

		public void removeUpdate(DocumentEvent e) {
			// TODO Auto-generated method stub
			dfem.setTitle(dfev.getTitle());
		}

	}
}
