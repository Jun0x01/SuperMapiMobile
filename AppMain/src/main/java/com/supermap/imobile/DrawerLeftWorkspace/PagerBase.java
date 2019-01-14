package com.supermap.imobile.DrawerLeftWorkspace;

import android.util.Log;
import android.util.SparseBooleanArray;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/4/28.
 */

public class PagerBase {

    protected ArrayList<String> mList;

    protected ArrayAdapter<String> mAdapter;

    protected ListView mListView;

    private  String tag = "PagerBase";

    public PagerBase(ListView listView, int layoutId, int textId){
        tag = getClass().getSimpleName();

        mList = new ArrayList<String>();
        mListView = listView;
        mAdapter = new ArrayAdapter<String>(listView.getContext(), layoutId, textId, mList);

        mListView.setAdapter(mAdapter);
    }

    public void addItem(String value){
        mList.add(value);
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
            Log.e(tag, "List's size is not equal with sateArray's.");
        }
        for(int i=0; i<stateArray.size(); i++){
            if(stateArray.get(i)){
                checkedList.add(i);
            }
        }
        return checkedList;
    }
}
