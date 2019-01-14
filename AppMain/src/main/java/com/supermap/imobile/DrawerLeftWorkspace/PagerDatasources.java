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
import com.supermap.data.EngineType;
import com.supermap.data.Workspace;
import com.supermap.imobile.R;
import com.supermap.mapping.Map;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/4/26.
 */

public class PagerDatasources extends JunBaseListAdapter implements JunBaseListAdapter.ListItenInterface {

    /**
     * The root view in the specified xml resource
     */
    private View mViewRoot;

    private DrawerFragmentWorkspace mParentFragment;
    private String tag = "PagerDatasources";

    public PagerDatasources(DrawerFragmentWorkspace parent, LayoutInflater inflater, ViewGroup root){

        mParentFragment = parent;

        int resource = R.layout.drawer_left_workspace_pager_datasources;
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
    int mCurPos = -1;

    public void setMap(Map map){
        mMap = map;
        mWorkspace = mMap.getWorkspace();
        mCurPos = -1;
    }

    @Override
    public void clearItems() {
        super.clearItems();
        mCurPos = -1;
    }

    /**********************************************/
    private ListView mListView;
//    private ArrayList<String> mList;
//    private ArrayAdapter mAdapter;
//    private JunListAdapter mAdapter;
    private void initListView(){
        mListView = (ListView) mViewRoot.findViewById(R.id.list_datasources);

        mList = new ArrayList<String>();
        int layoutId = R.layout.listview_item_datasource;
        int txtId = R.id.text_ItemName;
//        mLayoutId = android.R.layout.simple_list_item_multiple_choice;
//        mAdapter = new ArrayAdapter<String>(mViewRoot.getContext(), mLayoutId, txtId, mList);
//        mAdapter = new JunListAdapter(mViewRoot.getContext(), mLayoutId, txtId, mList);
//        mListView.setAdapter(mAdapter);
        init(mListView, layoutId, txtId);

        ItemClickedListener listener = new ItemClickedListener();
        mListView.setOnItemClickListener(listener);
        mListView.setOnItemLongClickListener(listener);

        setListItemInterface(this);
    }

    /**
     * Get the list of String resources which will be used in the ListView. You can change
     * the content of the list. But, the method, {@link #notifyDataSetChanged()}, should be called
     * by the application if the data set has changed.
     * @return A list of String resources
     */
/*
    public ArrayList<String> getDataList(){
        return  mList;
    }

*/

    /********************************************/
    class ItemClickedListener  implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        boolean isLongClicked = false;  // Distinguish between long click and click

        View mCurView = null;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            Log.d(tag, "Click: " + isLongClicked);
            if (!isLongClicked) {
                if(mCurPos != position){
//                    view.setActivated(true);
//                    if(mCurView != null)
//                        mCurView.setActivated(false);

                    mCurPos = position;
//                    mCurView = view;

                    mParentFragment.updateListView(mWorkspace.getDatasources().get(position), true);

                } else {
                    mParentFragment.showNextPager(mViewRoot);
                }

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
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent){
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
                Datasource datasource = mWorkspace.getDatasources().get(position);
                if(datasource != null){
                    int drawableId = 0;
                    EngineType type = datasource.getConnectionInfo().getEngineType();
                    if(type == EngineType.UDB){
                        drawableId = R.drawable.datasource_udb;
                    }else{
                        drawableId = R.drawable.datasource_image;
                    }

                    if(drawableId !=0){
                        img.setImageResource(drawableId);
                    }
                }
            }


        }
    }
}
