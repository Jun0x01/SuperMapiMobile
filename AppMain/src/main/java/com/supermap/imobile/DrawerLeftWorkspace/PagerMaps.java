package com.supermap.imobile.DrawerLeftWorkspace;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jun.tools.View.JunBaseListAdapter;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.imobile.ActivityMain.DispathMessage;
import com.supermap.imobile.R;
import com.supermap.mapping.Map;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/4/26.
 */

public class PagerMaps extends JunBaseListAdapter  implements JunBaseListAdapter.ListItenInterface {

    /**
     * The root view in the specified xml resource
     */
    private View mViewRoot;

    private DrawerFragmentWorkspace mParentFragment;

    private String tag = "PagerMaps";

    public PagerMaps(DrawerFragmentWorkspace parent, LayoutInflater inflater, ViewGroup root){

        mParentFragment = parent;

        int resource = R.layout.drawer_left_workspace_pager_maps;
        mViewRoot = inflater.inflate(resource, root, false);

        initListView();
    }

    /**
     * Get the inflated view hierarchy, it is the root view of the specified xml resource
     * @return  Root view in the specified xml resource
     */
    public View getView(){

        return mViewRoot;
    }

    private Map mMap;
    private Workspace mWorkspace;
    private String mPath;

    public void setMap(Map map){
        mMap = map;
        mWorkspace = mMap.getWorkspace();
    }

    private Datasource mDatasource;
    public void setDatasource(Datasource datasource){
        mDatasource = datasource;
    }

    /**********************************************/
    private ListView mListView;
//    private ArrayList<String> mList;
//    private ArrayAdapter mAdapter;

//    private JunListAdapter mAdapter;
    private void initListView(){
        mListView = (ListView) mViewRoot.findViewById(R.id.list_maps);

        mList = new ArrayList<String>();
        int layoutId = R.layout.listview_item_map;
        int txtId = R.id.text_ItemName;

//        mAdapter = new JunListAdapter(mViewRoot.getContext(), mLayoutId, txtId, mList);
//        mAdapter = new ArrayAdapter(mViewRoot.getContext(), mLayoutId, txtId, mList);
//        mListView.setAdapter(mAdapter);

        init(mListView, layoutId, txtId);

        ItemClickedListener listener = new ItemClickedListener();
        mListView.setOnItemClickListener(listener);
        mListView.setOnItemLongClickListener(listener);
        mListView.setOnItemSelectedListener(new ItemSelectedListener());

        setListItemInterface(this);
    }

    /**
     * Notifies the attached DataSet has changed, the ListView should refresh itself. This method
     * should be called by the application.
     */
    public void notifyDataSetChanged(){
        mAdapter.notifyDataSetChanged();
    }

    /**
     * Get the list of String resources which will be used in the ListView. You can change
     * the content of the list. But, the method, {@link #notifyDataSetChanged()}, should be called
     * by the application if the data set has changed.
     * @return A list of String resources
     */
//    public ArrayList<String> getDataList(){
//        return  mList;
//    }
//
    public void selectNew(){
        mListView.clearChoices();
        mListView.setItemChecked(mList.size()-1, true);
    }

    /************************************************/
    class ItemClickedListener  implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        boolean isLongClicked = false;  // Distinguish between long click and click
        int mCurPos = -1;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(tag, "Click: " + isLongClicked);
            if (!isLongClicked && mCurPos != position) {
                mMap.close();
                MapState.mapLoadStartTime = System.currentTimeMillis();
                boolean isOpen = mMap.open(mWorkspace.getMaps().get(position));
                mMap.refresh();
                if(isOpen) {
                    MapState.isNewMap = false;
                }
                MapState.isMapOpen = isOpen;

                DispathMessage.reloadLayers();
            }else {
                isLongClicked = false;
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            boolean result = false;
            Log.d(tag, "LongClick");
            isLongClicked = true;
            return result;
        }

    }


    class ItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
            mMap.close();
            MapState.mapLoadStartTime = System.currentTimeMillis();
            MapState.isMapOpen = mMap.open(mWorkspace.getMaps().get(position));
            mMap.refresh();

            MapState.isNewMap = false;
            MapState.mapName = mMap.getName();
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent){
            mMap.close();
            mMap.refresh();

            MapState.isMapOpen = false;
        }
    }

    @Override
    public void onGetView(int position, View itemView) {
        if (itemView != null) {
            TextView textNum = (TextView) itemView.findViewById(R.id.item_Num);
            if (textNum != null) {
                textNum.setText(position + "");
            }

            int id = R.id.item_img;
            ImageView img = (ImageView) itemView.findViewById(id);
            if(img != null && mWorkspace != null){
                img.setImageResource(R.drawable.dataset_default);

            }
        }
    }
}
