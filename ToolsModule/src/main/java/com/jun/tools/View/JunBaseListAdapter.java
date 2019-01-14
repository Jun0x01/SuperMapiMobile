package com.jun.tools.View;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jun on 2017/4/28.
 */

public class  JunBaseListAdapter{

    protected ArrayList<String> mList;

    protected ArrayAdapter<String> mAdapter;

    protected ListView mListView;

    private  String tag = "PagerBase";

    private ListItenInterface mListItemInterface;
    public JunBaseListAdapter(){

    }

    public JunBaseListAdapter(ListView listView, int layoutId, int textId){
        init(listView, layoutId, textId);
    }

    /**
     *
     * @param listView
     * @param layoutId
     * @param textId
     */
    protected void init(ListView listView, int layoutId, int textId) {
        tag = getClass().getSimpleName();
        mList = new ArrayList<String>();
        mListView = listView;
//        mAdapter = new ArrayAdapter<String>(listView.getContext(), layoutId, textId, mList);
        mAdapter = new InnerArrayAdapter<String>(listView.getContext(), layoutId, textId, mList);
        mListView.setAdapter(mAdapter);
    }

    public boolean addItem(String value){
        boolean isAdded = false;
        if (!mList.contains(value))
            isAdded = mList.add(value);

        return isAdded;
    }

    public boolean removeItem(String value){
        int index = mList.indexOf(value);
        boolean isTrue = mList.remove(value);
        if(isTrue)
            mListView.setItemChecked(index, false);
        return isTrue;
    }

    public String removeItem (int index){
        String oldValue = mList.remove(index);
        if(oldValue != null)
            mListView.setItemChecked(index, false);
        return oldValue;
    }

    public String getItem(int index){
        return mList.get(index);
    }
    public int getItemCount(){
        return mList.size();
    }

    public int indexOfItem(String value){
        return mList.indexOf(value);
    }

    public void clearItems(){
        mList.clear();
        mListView.clearChoices();
    }

    public ArrayList<Integer> getCheckedPositions(){
        ArrayList<Integer> checkedList = new ArrayList<>();

        SparseBooleanArray stateArray = mListView.getCheckedItemPositions();
        if(stateArray.size() != mList.size()){
            Log.d(tag, "List's size is not equal with sateArray's. List's size: " + mList.size() + ", StateArray's size: " + stateArray.size());
        }
        for(int i=0; i<mList.size(); i++){
            if(stateArray.get(i)){
                checkedList.add(i);
            }
        }
        return checkedList;
    }

    public void setItemChecked(int position, boolean isChecked){
        if (position < mList.size())
            mListView.setItemChecked(position, true);
    }

    /**
     *
     */
    public void clearChoices(){
        mListView.clearChoices();
        mListView.invalidateViews();
//        mListView.invalidate();
    }

    /**
     * Notifies the attached DataSet has changed, the ListView should refresh itself. This method
     * should be called by the application.
     */
    public void notifyDataSetChanged(){
        mAdapter.notifyDataSetChanged();
    }

    public View getView(){
        return mListView;
    }


    public void setListItemInterface(ListItenInterface itemInterface) {
        mListItemInterface = itemInterface;
    }

    class InnerArrayAdapter<T> extends ArrayAdapter{
        public InnerArrayAdapter(Context context, int resource,int textViewResourceId, List<?> objects) {
            super(context, resource, textViewResourceId, objects);
        }

        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

            View item = super.getView(position, convertView, parent);
            if(mListItemInterface != null)
                mListItemInterface.onGetView(position, item);
            return item;
        }
    }

    public interface ListItenInterface {
        void  onGetView(int position, View itemView);
    }
}
