package com.supermap.imobile;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory.Options;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2015/7/20.
 */
public class ImageAdapterOfActivity extends BaseAdapter {
    private Context mContext;

    private LayoutInflater mLayoutInflater;
    /**
     * A list of  Map:{Name: name, ImgeId: id} <String, int>
     */
    private List<? extends Map<String, String>> mData;
    private List<Map<String, Object>> iMageData;
    private Options mBitMapOptions = null;
    
    public ImageAdapterOfActivity(Context context, List<? extends Map<String, String>> list){
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
        
        
        mBitMapOptions = new Options();
        mBitMapOptions.inPreferredConfig = Bitmap.Config.RGB_565;
        mBitMapOptions.inPurgeable = true;
        mBitMapOptions.inInputShareable = true;
       
        mData = list;
        
    }


    public int getCount(){
        return mData.size();
    }

    public Object getItem(int position){
        return position;
    }

    public long getItemId(int position){
        return position;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent){
    	View view;
    	ViewHolder viewHolder;
        ImageView imageView;
        TextView textView;
//        if(convertView != null){
//        	recycleView(convertView);
//        	convertView = null;
//        }
        if(convertView == null ){ 
        	view = mLayoutInflater.inflate(R.layout.layout_main_image_item, null);
        	viewHolder = new ViewHolder();
        	
        	view.setTag(viewHolder);
        	
        }else {
        	view = convertView;
        	viewHolder = (ViewHolder) view.getTag();
        }
        
        
        String str_name    = mData.get(position).get("Name");
        String str_ImageId = mData.get(position).get("ImageId");
        
        int int_ImageId = Integer.parseInt(str_ImageId);
        
        viewHolder.str_name = str_name;
        viewHolder.int_ImageId = int_ImageId;
        view.setTag(viewHolder);
        
        imageView = (ImageView) view.findViewById(R.id.image);
    	textView  = (TextView)  view.findViewById(R.id.text);
    	
    	textView.setText(str_name);
    	imageView.setImageResource(int_ImageId);
    	
        return  view;
    }

    
    private void recycleView(View view) {
		// TODO Auto-generated method stub
		if (view.getDrawingCache() != null){
			view.getDrawingCache().recycle();
			Log.e("", "recycled");
		}
	}


    private class ViewHolder {
    	public String str_name;
    	public int    int_ImageId;
    }
}
