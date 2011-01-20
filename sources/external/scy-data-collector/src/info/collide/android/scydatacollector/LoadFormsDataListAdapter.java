package info.collide.android.scydatacollector;

import java.util.ArrayList;
import java.util.Hashtable;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.CompoundButton.OnCheckedChangeListener;

public class LoadFormsDataListAdapter extends BaseAdapter {

    private Button _btnDownload;

    private Boolean _ownFormulars;

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

    private int _selectedIndex;

    private Activity _context;

    // private DataCollectorContentProvider _dccp = new
    // DataCollectorContentProvider();

    public LoadFormsDataListAdapter(final Activity context, ListView listview, Boolean ownForms) {

        // save the activity/context ref
        _ownFormulars = ownForms;
        _context = context;
        _selectedIndex = -1;
        _btnDownload = ((Button) _context.findViewById(R.id.btnDownload));
        // bind this model (and cell renderer) to the listview
        // listview.setAdapter(this);

        bindListViewListener(listview);

        // load some data into the model
        getForms();
    }

    private void bindListViewListener(ListView listview) {
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parentView, View childView, int position, long id) {
                setSelected(position);
            }
        });
    }

    private void getForms() {
        try {
            WebServicesController wsc = new WebServicesController(_context);
            if (_ownFormulars == true) {
                DataCollectorConfiguration config = new DataCollectorConfiguration(_context);
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
            notifyDataSetChanged();
        } catch (Exception ex) {
            MessageDialog md = new MessageDialog(_context);
            android.content.DialogInterface.OnClickListener oclYes = new android.content.DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog, int which) {
                    _context.finish();
                }
            };

            md.createInfoDialog(_context.getResources().getString(R.string.msgNoConnectionToRepository), oclYes);
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
        cellRendererView.display(index, _selectedIndex == index);

        return cellRendererView;

    }

    public void setSelected(int index) {

        if (index == -1) {
            // unselected
        } else {
            // selected index...
        }

        _selectedIndex = index;

        // notify the model that the data has changed, need to update the view
        notifyDataSetChanged();

        // Log.i(getClass().getSimpleName(),
        // "updating _selectionIndex with index and firing model-change-event: index="
        // + index);
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

            super(_context);

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
            TableRow row = new TableRow(_context);

            // fill the first row with: icon/flag, name
            {
                _lblFormTitle = new TextView(_context);
                _lblFormTitle.setTextSize(20f);
                row.addView(_lblFormTitle);
            }

            // create the 2nd row with: description
            {
                _lblFormDescription = new TextView(_context);
                _lblFormDescription.setPadding(10, 10, 10, 10);
                _cbDownload = new CheckBox(_context);
                _cbDownload.setOnCheckedChangeListener(new OnCheckedChangeListener() {

                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (_selectedIndex > -1) {
                            ((DataFormPreviewModel) getItem(_selectedIndex)).setDownload(isChecked);
                            LoadFormsDataListAdapter.this.notifyDataSetChanged();
                            _btnDownload.setEnabled(getDownloadCount(alDfpm) > 0);
                        }
                    }
                });
                _cbDownload.setText(getResources().getString(R.string.chkDownlaod));
                _btnDownload.setEnabled(false);
            }

            // add the rows to the table
            {
                addView(row);
                addView(_lblFormDescription);
                addView(_cbDownload);
            }
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

    public void update(Boolean ownforms) {
        // TODO Auto-generated method stub
        _ownFormulars = ownforms;
        getForms();
    }

}

// }
