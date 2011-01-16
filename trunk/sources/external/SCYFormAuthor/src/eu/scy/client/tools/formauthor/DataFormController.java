package eu.scy.client.tools.formauthor;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class DataFormController {
	private DataFormModel dfm;
	private DataFormView dfv;
	private DataForm df;
	private static final String SCY_FORM = "scy/form";
	private static final String SCY_FORMTEMPLATE = "scy/formtemplate";
	private static final String SCY_FORM_EXTEMSION = ".form";
	private static final String SCY_FORMTEMPLATE_EXTENSTION = ".formtmp";

	public DataFormController(DataFormModel model, DataFormView view, DataForm df) {
		this.dfm = model;
		this.dfv = view;
		this.df = df;

		dfv.addAddFieldListener(new AddDataFieldListener());
		dfv.addDeleteFieldListener(new DeleteDataFieldListener());
		dfv.addMoveUpFieldListener(new MoveUpDataFieldListener());
		dfv.addMoveDownFieldListener(new MoveDownDataFieldListener());
		dfv.addSaveDataFormListener(new SaveDataFormListener());
		dfv.addOpenDataFormListener(new OpenDataFormListener());
		dfv.addFormTitleChangeListener(new FormTitleChangeListener());
		dfv.addFormDescriptionChangeListener(new FormDescriptionChangeListener());
		dfv.addCloseListener(new CloseListener());
		dfv.addConfigurationListener(new OpenConfigurationListener());
		dfv.addSaveDataFormRepositorListener(new SaveDataFormRepositorListener());
		dfv.addOpenTemplatesRepositoryListener(new OpenTemplatesRepositorListener());
		dfv.addOpenRepositoryListener(new OpenRepositorListener());
		dfv.addNewFormListener(new NewFormListener());
	}

	private class FormTitleChangeListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}

		public void insertUpdate(DocumentEvent e) {
			dfm.setTitle(dfv.getFormTitle());
		}

		public void removeUpdate(DocumentEvent e) {
			dfm.setTitle(dfv.getFormTitle());
		}
	}

	private class FormDescriptionChangeListener implements DocumentListener {
		public void changedUpdate(DocumentEvent e) {
		}

		public void insertUpdate(DocumentEvent e) {
			dfm.setDescription(dfv.getFormDescription());
		}

		public void removeUpdate(DocumentEvent e) {
			dfm.setDescription(dfv.getFormDescription());
		}
	}

	private class SaveDataFormRepositorListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			WebServicesController wsc = new WebServicesController();
			wsc.postFormJSON(dfm);
		}

	}

	private class OpenTemplatesRepositorListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			WebServicesController wsc = new WebServicesController();
			ArrayList<DataFormPreviewModel> alDfpm;
			try {
				alDfpm = wsc.getEloForms("false", SCY_FORMTEMPLATE);
				ArrayList<String> choices = new ArrayList<String>();
				for (DataFormPreviewModel dfpm : alDfpm) {
					choices.add(dfpm.getTitle() + "[" + dfpm.getAuthor() + "]" + " - " + dfpm.getDescription());
				}
				if (choices.size() > 0) {
					Integer selectedPos = LoadFormsDialog.showDialog(df, Localizer.getString("DIALOG_OPEN_REPOSITORY_TITLE"), Localizer.getString("DIALOG_OPEN_REPOSITORY_MESSAGE"), (String[]) choices.toArray(new String[] {}), choices.get(0), null);
					if (selectedPos != -1) {
						wsc.fetchDCFMFromServer(alDfpm.get(selectedPos).getURI(), dfm);
					}
				} else {
					JOptionPane.showMessageDialog(df, Localizer.getString("NO_TEMPLATES"));
				}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, Localizer.getString("ERROR_URL_INCORRECT"), Localizer.getString("ERROR"), JOptionPane.ERROR);
				e1.printStackTrace();
			}

		}
	}

	private class OpenRepositorListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			WebServicesController wsc = new WebServicesController();
			ArrayList<DataFormPreviewModel> alDfpm;
			try {
				alDfpm = wsc.getEloForms("false", SCY_FORM);
				ArrayList<String> choices = new ArrayList<String>();
				for (DataFormPreviewModel dfpm : alDfpm) {
					choices.add(dfpm.getTitle() + "[" + dfpm.getAuthor() + "]" + " - " + dfpm.getDescription());
				}
				
				if (choices.size() > 0) {
					Integer selectedPos = LoadFormsDialog.showDialog(df, Localizer.getString("DIALOG_OPEN_REPOSITORY_TITLE"), Localizer.getString("DIALOG_OPEN_REPOSITORY_MESSAGE"), (String[]) choices.toArray(new String[] {}), choices.get(0), null);
					if (selectedPos != -1) {
						wsc.fetchDCFMFromServer(alDfpm.get(selectedPos).getURI(), dfm);
						
					}
				} else {
					JOptionPane.showMessageDialog(df, Localizer.getString("NO_FORMS"));
				}
			} catch (Exception e1) {
				JOptionPane.showMessageDialog(null, Localizer.getString("ERROR_URL_INCORRECT"), Localizer.getString("ERROR"), JOptionPane.ERROR);
				e1.printStackTrace();
			}
		}
	}

	private class SaveDataFormListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			final JFileChooser jfc = new JFileChooser();
			jfc.setDialogType(JFileChooser.SAVE_DIALOG);
			int result = jfc.showSaveDialog(dfv);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					dfm.toFile(jfc.getSelectedFile().toString());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		}

	}
	
	private class NewFormListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			dfm.clear();
		}
		
	}

	private class OpenDataFormListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			final JFileChooser jfc = new JFileChooser();
			jfc.setDialogType(JFileChooser.OPEN_DIALOG);
			int result = jfc.showOpenDialog(dfv);
			if (result == JFileChooser.APPROVE_OPTION) {
				try {
					dfm.fromFile(jfc.getSelectedFile().toString());
				} catch (Exception e1) {
					// H
					JOptionPane.showMessageDialog(null, Localizer.getString("ERROR_FORM_LOAD_INVALID"), Localizer.getString("ERROR"), JOptionPane.OK_CANCEL_OPTION);
					e1.printStackTrace();
				}
			}
		}

	}

	private class AddDataFieldListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			dfm.addDataField();
		}
	}

	private class DeleteDataFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = dfm.getDfElements().size() - 1; i >= 0; i--) {
				if (dfm.getDfElements().get(i).isSelected())
					dfm.removeDataField((DataFormElementModel) dfm.getDfElements().get(i));
			}
		}
	}

	private class MoveDownDataFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = dfm.getDfElements().size() - 1; i >= 0; i--) {
				if (dfm.getDfElements().get(i).isSelected())
					dfm.moveElementDown((DataFormElementModel) dfm.getDfElements().get(i));
			}
		}
	}

	private class MoveUpDataFieldListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			for (int i = 0; i < dfm.getDfElements().size(); i++) {
				if (dfm.getDfElements().get(i).isSelected())
					dfm.moveElementUp((DataFormElementModel) dfm.getDfElements().get(i));
			}
		}
	}

	private class CloseListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			df.dispose();
		}
	}

	private class OpenConfigurationListener implements ActionListener {

		public void actionPerformed(ActionEvent e) {
			ConfigDialog cd = new ConfigDialog(df, true);
			cd.setSize(600, 285);
			cd.setLocationRelativeTo(null);
			cd.setVisible(true);

		}
	}
}
