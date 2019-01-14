package com.supermap.imobile.Menus;

import android.content.Context;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.jun.tools.View.JunBaseListAdapter;
import com.supermap.imobile.R;

/**
 * Created by Jun on 2017/8/8.
 */

public class PopList extends PopBase {

    private ListView mListView;
    private JunBaseListAdapter mListAdapter;
    public PopList(Context context){
        super(context);
    }

    @Override
    protected void initView() {
        // Override the layout id
        mLayoutId = R.layout.layout_pop_list;
        super.initView();

        mListView = (ListView) mContentView.findViewById(R.id.pop_ListView);

        mListAdapter = new JunBaseListAdapter(mListView, R.layout.listview_item_normal, R.id.text_ItemName);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    public void setOnItemClickedListener(AdapterView.OnItemClickListener listener){
        mListView.setOnItemClickListener(listener);
    }

    public void addItem(String item){
        mListAdapter.addItem(item);
    }
    public void clearItems(){
        mListAdapter.clearItems();
    }

    public String getItem(int position){
        return mListAdapter.getItem(position);
    }
}
