package com.jun.tools.filebrower;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.jun.tools.R;

import java.util.ArrayList;
import java.util.List;

public class FileListAdapter extends BaseAdapter {

	private ArrayList<FileInfo> mList;
	private LayoutInflater mInflater;
	private int mResourceId;
	public FileListAdapter(Context context) {
		mList = new ArrayList<FileInfo>();
		mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		mResourceId = R.layout.layout_item_filelist;
	}
	
	public List<FileInfo> getList(){
		return mList;
	}
	
	public void addAppInfo(FileInfo appInfo){
		mList.add(appInfo);
	}
	
	public boolean removeAppInfo(FileInfo appInfo){
		return mList.remove(appInfo);
	}
	
	public FileInfo removeAppInfo(int index){
		return mList.remove(index);
	}
	
/****************************************************************************/	

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		View v = null;
		if(null == convertView){
			v = mInflater.inflate(mResourceId, parent, false);
		}else {
			v = convertView;
		}
		FileInfo fileInfo = mList.get(position);
		
		ImageView imgView = (ImageView) v.findViewById(R.id.img_item_icon);
		TextView name = (TextView) v.findViewById(R.id.txt_item_name);
		TextView info = (TextView) v.findViewById(R.id.txt_item_info);

		if(position == 0){
			imgView.setImageResource(R.drawable.filedialog_folder_up);
		}else if(fileInfo.mIsDir){
			imgView.setImageResource(R.drawable.filedialog_folder);
		}else {
			imgView.setImageResource(R.drawable.filedialog_file);
		}

		name.setText(fileInfo.mName);
		if(fileInfo.mIsDir){

		}else {
			info.setText(" Size:" + fileInfo.mSize + "B");
		}
		return v;
	}

}
