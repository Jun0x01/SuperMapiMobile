package com.supermap.imobile.DrawerLeftWorkspace;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.jun.tools.View.JunBaseListAdapter;
import com.supermap.data.Dataset;
import com.supermap.data.DatasetType;
import com.supermap.data.Datasource;
import com.supermap.data.Workspace;
import com.supermap.imobile.ActivityMain.DispathMessage;
import com.supermap.imobile.R;
import com.supermap.mapping.Map;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/4/26.
 */

public class PagerDatasets extends JunBaseListAdapter implements View.OnClickListener, JunBaseListAdapter.ListItenInterface{

    /**
     * The root view in the specified xml resource
     */
    private View mViewRoot;

    private DrawerFragmentWorkspace mParentFragment;

    private ArrayList<Integer> mSelectedItems;

    public PagerDatasets(DrawerFragmentWorkspace parent, LayoutInflater inflater, ViewGroup root){

        mParentFragment = parent;
        int resource = R.layout.drawer_left_workspace_pager_datasets;
        mViewRoot = inflater.inflate(resource, root, false);
        mSelectedItems = new ArrayList<Integer>();
        initListView();

        initView();
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

    private Map mMapGL;
    public void setGLMap(Map map){
        mMapGL = map;
    }

    private Datasource mDatasource;
    public void setDatasource(Datasource datasource){
        mDatasource = datasource;
    }

    public boolean isSameDatasource(Datasource datasource){

        boolean isSame = false;
        if(mDatasource != null && datasource != null){
            if(mDatasource.getAlias().equals(datasource.getAlias())){
                isSame = true;
            }
        }

        return isSame;
    }

    /**********************************************/
    private ListView mListView;
    private void initListView(){
        mListView = (ListView) mViewRoot.findViewById(R.id.list_datasets);

        int layoutId = R.layout.listview_item_dataset;
        int txtId = R.id.text_ItemName;

        init(mListView, layoutId, txtId);
        mListView.getCheckedItemPositions();
        setListItemInterface(this);


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

    class ItemSelectedListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id){
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent){
        }
    }


    /******************* Initialize Views ***********************/

    private View mSetting_Datasets;
    private void initView(){
        mViewRoot.findViewById(R.id.btn_AddLayers).setOnClickListener(this);
        mViewRoot.findViewById(R.id.btn_Cancel).setOnClickListener(this);
        mViewRoot.findViewById(R.id.btn_AddLayers1).setOnClickListener(this);
        mViewRoot.findViewById(R.id.btn_Cancel1).setOnClickListener(this);
        mViewRoot.findViewById(R.id.btn_Open).setOnClickListener(this);
        mViewRoot.findViewById(R.id.btn_Close).setOnClickListener(this);

        mSetting_Datasets = mViewRoot.findViewById(R.id.layout_setting_datasets);
        mSetting_Datasets.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
    }


    /******************* OnItemClickListener ***********************/

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.btn_AddLayers:
            case R.id.btn_AddLayers1:
                addLayers();
                break;
            case R.id.btn_Cancel:
            case R.id.btn_Cancel1:
                clearChoices();
                break;
            case R.id.btn_Open:
                mSetting_Datasets.setVisibility(View.VISIBLE);
                break;
            case R.id.btn_Close:
                mSetting_Datasets.setVisibility(View.GONE);
                break;
            default:
                break;
        }
    }

    private void addLayers(){
        final ArrayList<Integer> selectedPos = getCheckedPositions();
        if(selectedPos.size() > 0 && mDatasource != null){
            MapState.mapLoadStartTime = System.currentTimeMillis();
            int oldCount = mMap.getLayers().getCount();
            Dataset dataset = null;
            for (int i=0; i<selectedPos.size(); i++){
                dataset = mDatasource.getDatasets().get(selectedPos.get(i));
                if(dataset.getName().contains("GLCache") && mMapGL != null){
                    mMapGL.getLayers().add(dataset, true);
                    continue;
                }
                mMap.getLayers().add(dataset, true);
            }
            mMap.refresh();
            if(mMap.getLayers().getCount() > 0)
                MapState.isMapOpen = true;

            if(MapState.isNewMap && oldCount == 0 && MapState.isMapOpen && mMap.getLayers().getCount() > 0){
                String name = mMap.getLayers().get(mMap.getLayers().getCount() -1).getName();
                int i=0;
                while (mWorkspace.getMaps().indexOf(name)>=0){
                    i++;
                    name = name + "_" + i;
                }
                MapState.mapName = name;
            }
            DispathMessage.reloadLayers();
            clearChoices();
        }
    }

    @Override
    public void onGetView(int position, View itemView) {
        if(itemView != null) {
            TextView textNum = (TextView) itemView.findViewById(R.id.item_Num);
            if(textNum != null){
                textNum.setText(position +"");
            }
            int id = R.id.item_img;
            ImageView img = (ImageView) itemView.findViewById(id);
            if(img != null && mDatasource != null){
                Dataset dataset = mDatasource.getDatasets().get(position);
                if(dataset != null){
                    int drawableId = 0;
                    DatasetType type = dataset.getType();
                    if(type == DatasetType.POINT){
                        drawableId = R.drawable.dataset_point;
                    }else if(type == DatasetType.LINE){
                        drawableId = R.drawable.dataset_line;
                    }else if(type == DatasetType.REGION){
                        drawableId = R.drawable.dataset_region;
                    }else if(type == DatasetType.CAD){
                        drawableId = R.drawable.dataset_cad;
                    }else if(type == DatasetType.TEXT){
                        drawableId = R.drawable.dataset_text;
                    }else if(type == DatasetType.GRID){
                        drawableId = R.drawable.dataset_grid;
                    }else if(type == DatasetType.IMAGE){
                        drawableId = R.drawable.dataset_image;
                    }else if(type == DatasetType.NETWORK){
                        drawableId = R.drawable.dataset_network;
                    }else if(type == DatasetType.LINE3D){
                        drawableId = R.drawable.dataset_line3d;
                    }else if(type == DatasetType.POINT3D){
                        drawableId = R.drawable.dataset_point3d;
                    }else if(type == DatasetType.REGION3D){
                        drawableId = R.drawable.dataset_region3d;
                    }else if(type == DatasetType.TABULAR){
                        drawableId = R.drawable.dataset_tabular;
                    }else{
                        drawableId = R.drawable.dataset_default;
                    }

                    if(drawableId !=0){
                        img.setImageResource(drawableId);
                    }
                }
            }

        }
    }
}
