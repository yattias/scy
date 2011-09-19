package info.collide.android.scydatacollector;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

public class LoadFormsDataListAdapter extends BaseAdapter {

    private Button btnDownload;

    private Boolean ownFormulars;

    private ArrayList<DataFormPreviewModel> alDfpm;

    public ArrayList<DataFormPreviewModel> getAlDfpm() {
        return alDfpm;
    }

    public int getDownloadCount(ArrayList<DataFormPreviewModel> alDfpm) {
        int counter = 0;
        for (DataFormPreviewModel dfpm : alDfpm) {
            if (dfpm.is_download())
                counter++;
        }
        return counter;
    }

    private Hashtable<Integer, DataFormPreviewModel> _data = new Hashtable<Integer, DataFormPreviewModel>();

    private int selectedIndex;

    private Activity context;

    private WebServicesController wsc;

    public LoadFormsDataListAdapter(final Activity context, ListView listview, WebServicesController wsc, Boolean ownForms) {
        this.ownFormulars = ownForms;
        this.context = context;
        this.wsc = wsc;
        selectedIndex = -1;
        btnDownload = ((Button) context.findViewById(R.id.btnDownload));

        bindListViewListener(listview);
    }

    private void bindListViewListener(ListView listview) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                setSelected(position);
            }
        });
    }

    /**
     * @throws Exception
     */
    public void downloadForms(boolean ownForms) {
        ownFormulars = ownForms;
        try {
            wsc = new WebServicesController(context);
            if (ownFormulars) {
                DataCollectorConfiguration config = new DataCollectorConfiguration(context);
                alDfpm = wsc.getEloForms(config.getGroupname());
            } else {
                alDfpm = wsc.getEloForms("");
            }
            _data.clear();
            int id = 0;
            for (DataFormPreviewModel dfpm : alDfpm) {
                _data.put(id, dfpm);
                id++;
            }
        } catch (Exception ex) {
            MessageDialog md = new MessageDialog(context);
            android.content.DialogInterface.OnClickListener oclYes = new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    context.finish();
                }
            };

            md.createInfoDialog(context.getResources().getString(R.string.msgNoConnectionToRepository), oclYes);
        }
    }

    public int getCount() {
        return _data.size();
    }

    public Object getItem(int position) {
        return _data.values().toArray()[position];
    }

    public long getItemId(int position) {
        Integer[] keys = _data.keySet().toArray(new Integer[_data.keySet().size()]);
        return keys[position];
    }

    public View getView(int index, View cellRenderer, ViewGroup parent) {
        CellRendererView cellRendererView = null;

        if (cellRenderer == null) {
            // create the cell renderer
            // Log.i(getClass().getSimpleName(),
            // "creating a CellRendererView object");
            cellRendererView = new CellRendererView();
        } else {
            cellRendererView = (CellRendererView) cellRenderer;
        }

        // update the cell renderer, and handle selection state
        cellRendererView.display(index, selectedIndex == index);

        return cellRendererView;
    }

    public void setSelected(int index) {
        if (index == -1) {
            // unselected
        } else {
            // selected index...
        }

        selectedIndex = index;

        // notify the model that the data has changed, need to update the view
        notifyDataSetChanged();
    }

    /**
     * this class is responsible for rendering the data in the model, given the selection state
     */
    private class CellRendererView extends TableLayout {

        // ui stuff
        private TextView _lblFormTitle;

        // private ImageView _lblIcon;
        private TextView _lblFormDescription;

        // private Button _btnOpenForm;
        // private Button _btnDeleteForm;
        private CheckBox _cbDownload;

        public CellRendererView() {
            super(context);

            _createUI();
        }

        /** create the ui components */
        private void _createUI() {
            setBackgroundResource(R.drawable.gradient);

            // make the 2nd col growable/wrappable
            setColumnShrinkable(1, true);
            setColumnStretchable(1, true);

            // set the padding
            setPadding(10, 10, 10, 10);

            // single row that holds icon/flag & name
            TableRow row = new TableRow(context);

            // fill the first row with: icon/flag, name
            _lblFormTitle = new TextView(context);
            _lblFormTitle.setTextSize(20f);
            row.addView(_lblFormTitle);

            // create the 2nd row with: description
            _lblFormDescription = new TextView(context);
            _lblFormDescription.setPadding(10, 10, 10, 10);
            _cbDownload = new CheckBox(context);
            _cbDownload.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (selectedIndex > -1) {
                        ((DataFormPreviewModel) getItem(selectedIndex)).setDownload(isChecked);
                        LoadFormsDataListAdapter.this.notifyDataSetChanged();
                        btnDownload.setEnabled(getDownloadCount(alDfpm) > 0);
                    }
                }
            });
            _cbDownload.setText(getResources().getString(R.string.chkDownlaod));
            btnDownload.setEnabled(false);

            // add the rows to the table
            addView(row);
            addView(_lblFormDescription);
            addView(_cbDownload);
        }

        /** update the views with the data corresponding to selection index */
        public void display(int index, boolean selected) {
            DataFormPreviewModel dfpm = ((DataFormPreviewModel) getItem(index));
            if (dfpm.is_download()) {
                _lblFormTitle.setText(dfpm.getTitle() + " [DOWNLOAD]");
            } else {
                _lblFormTitle.setText(dfpm.getTitle());
            }
            _lblFormDescription.setText(dfpm.getDescription());
            if (selected) {
                _lblFormDescription.setVisibility(View.VISIBLE);
                _cbDownload.setVisibility(View.VISIBLE);
                if (dfpm.is_download()) {
                    _cbDownload.setChecked(true);
                } else {
                    _lblFormTitle.setText(dfpm.getTitle());
                    _cbDownload.setChecked(false);
                }
            } else {
                _lblFormDescription.setVisibility(View.GONE);
                _cbDownload.setVisibility(View.GONE);
            }
        }
    }
}