package com.jun.tools.View;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.jun.tools.R;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/4/27.
 */

public class JunListAdapter extends BaseAdapter {

    private ArrayList<String> mList;
    private Context mContext;
    private LayoutInflater mInflater;

    private int mLayoutId;
    private int mTxtId;

    int mChoiceMode = ListView.CHOICE_MODE_NONE;


    public ArrayList<Integer> mCurPositions;

    private JunItemClickedListener mListener;

    public JunListAdapter(Context context, ArrayList<String> list){

        int resource = R.layout.listview_item;
        int txtId = R.id.text_item_name;
        init(context, resource, txtId, list);

    }

    public JunListAdapter(Context context, int resource, int txtId, ArrayList<String> list) {

        init(context, resource, txtId, list);

    }

    private void init(Context context, int resource, int txtId, ArrayList<String> list){
        mList = list;
        mContext = context;
        mInflater = LayoutInflater.from(mContext);

        mLayoutId = resource;
        mTxtId = txtId;
        mCurPositions = new ArrayList<Integer>();

        mListener = new JunItemClickedListener();
    }

    /**
     *
     * @return
     */
    public JunItemClickedListener getItemListener(){
        return mListener;
    }

    /**
     *
     * @param mode
     */
    public void setChoiceMode(int mode){
        mChoiceMode = mode;
    }

    private boolean mEnableLongClick = false;
    public void enableLongClick(boolean enable){
        mEnableLongClick = enable;
    }

    @Override
    protected void finalize() throws Throwable {
        mCurPositions.clear();
        super.finalize();
    }

    @Override
    public int getCount() {

        return mList != null ? mList.size():0;
    }

    @Override
    public Object getItem(int position) {

        return mList != null ? mList.get(position) : null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mInflater.inflate(mLayoutId, null);
            convertView.setTag(position);
            if(mCurPositions.contains(position)){
                convertView.setSelected(true);
            }
        } else {
            if(mCurPositions.contains(position)){
                convertView.setSelected(true);
            }else if(convertView.isSelected()){
                convertView.setSelected(false);
            }
        }
        TextView textView = (TextView) convertView.findViewById(mTxtId);
        if (textView != null) {
            textView.setText(mList.get(position));
        }
        return convertView;
    }


    /**************** JunItemClickedListener *****************/
    public class JunItemClickedListener  implements AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener {

        private boolean isLongClicked = false;
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            if(!isLongClicked && !mEnableLongClick) {
                selectItem(parent, view, position, id);
            }else {
                isLongClicked = false;
            }
        }

        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            isLongClicked = true;

            if(mEnableLongClick) {
                boolean result = selectItem(parent, view, position, id);

                return result;
            } else {
                return false;
            }
        }


        public JunItemClickedListener() {

        }

        private View mCurView;

        private boolean selectItem(AdapterView<?> parent, View view, int position, long id) {
            boolean result = true;

            switch (mChoiceMode) {

                case ListView.CHOICE_MODE_SINGLE: {
                    if(mCurPositions.size() >0) {
                        int curPos = mCurPositions.get(0);
                        if (curPos == position) {
                            view.setSelected(false);
                            mCurPositions.clear();
                        } else if (curPos != position) {
                            if (mCurView != null)
                                mCurView.setSelected(false);
                            view.setSelected(true);
                        }
                    } else {
                        view.setSelected(true);
                    }

                    if(view.isSelected()){
                        mCurView = view;
                    } else {
                        mCurView = null;
                    }

                }
                break;
                case ListView.CHOICE_MODE_MULTIPLE: {

                    boolean isSelected = true;
                    for (int curPos : mCurPositions) {
                        if (curPos == position) {
                            isSelected = false;
                            mCurPositions.remove((Integer) position);
                            break;
                        }
                    }
                    view.setSelected(isSelected);
                    if(isSelected)
                        mCurPositions.add(position);

                }
                break;
                default:
                    result = false;
                    break;
            }
            return result;

        }
    }

}
