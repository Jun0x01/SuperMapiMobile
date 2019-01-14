package com.jun.tools.filebrower;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.jun.tools.R;

import java.io.File;
import java.util.ArrayList;

/**
 * Pick a file or directory
 * input scheme is dir to define root , set extra data, such as "file' and "dir",
 * with the key "type" to define whether select a directory or not.
 * return scheme is file
 *
 */
public class FilePickActivity extends Activity  implements AdapterView.OnItemClickListener{

    FileListAdapter mFileAdapter;
    ListView mListView;

    String mPath;
    String type; // dir, file
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_pick);

        initView();
        initAdapter();
    }
    private void initView(){
        findViewById(R.id.btn_Cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });

        findViewById(R.id.btn_Confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FileInfo fileInfo = null;
                if(type == null){
                    if(mLastCheckedPosition>0){
                        fileInfo = (FileInfo) mFileAdapter.getItem(mLastCheckedPosition);
                    }else {
                        fileInfo = (FileInfo) mFileAdapter.getItem(0);
                    }
                } else if(type.equals("dir")) {
                    fileInfo = (FileInfo) mFileAdapter.getItem(0);
                }else if(type.equals("file")) {
                    if (mLastCheckedPosition > 0) {
                        fileInfo = (FileInfo) mFileAdapter.getItem(mLastCheckedPosition);
                    } else {
                        Toast.makeText(getApplicationContext(), "没有选中任何文件，\n若要取消选择，请单击\"取消\"按钮", Toast.LENGTH_LONG).show();
                    }
                } else {
                    //---
                    return;
                }

                Intent intent = new Intent();
                if (type != null)
                    intent.setType(type);

                if (fileInfo.mIsDir){
                    intent.setData(Uri.parse("dir://" + fileInfo.mPath));
                }else {
                    intent.setData(Uri.parse("file://" + fileInfo.mPath));
                }


                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private void initAdapter() {
        mFileAdapter = new FileListAdapter(this);

        mListView = (ListView) findViewById(R.id.listView);
        Uri uri = getIntent().getData();

        File file = null;
        if(uri != null && "dir".equalsIgnoreCase(uri.getScheme())) {

            mPath = uri.getPath();
            file = new File(mPath);
        }

            if(file == null || !file.exists())
                mPath = Environment.getExternalStorageDirectory().getAbsolutePath().toString();
            updateItems(mPath, -1);


        type = getIntent().getType();

        mListView.setAdapter(mFileAdapter);
        mListView.setOnItemClickListener(this);

    }


    @Override
    protected void onStart() {
        super.onStart();

//        if (mPath != null){
//            updateItems(mPath, -1);
//        }
    }

    private boolean updateItems(String path , int position){

        synchronized (mFileAdapter.getList()) {
            ArrayList<FileInfo> fileInfos = (ArrayList<FileInfo>) mFileAdapter.getList();
            if (path != null) {
                File dirFile = new File(path);
                if(!dirFile.exists() || !dirFile.isDirectory())
                    return false;

                // The path is a directory path and exists.
                FileInfo fileInfo = null;
                File file = new File(path);
                if(position <= 0){
                    if(position == 0) {
                        path = file.getParent();
                        // no parent, current path is system root path
                        if(path == null)
                            return false;
                    }

                    fileInfo = new FileInfo();
                    fileInfo.mIsDir = file.isDirectory();
                    fileInfo.mPath = path;
                    fileInfo.mName = path;
                }else {
                    fileInfo = fileInfos.get(position);
                    fileInfo.mName = fileInfo.mPath;
                }
                fileInfos.clear();
                fileInfos.add(fileInfo);
                FileScaner.listFiles(path, fileInfos);
            }
            mFileAdapter.notifyDataSetChanged();
        }

        return true;
    }

    private int mLastCheckedPosition = -1;
    private View mLastCheckedView;
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileInfo fileInfo = mFileAdapter.getList().get(position);

        if(fileInfo.mIsDir){
//            mListView.setItemChecked(mLastCheckedPosition, false);
            if(mLastCheckedView != null)
                mLastCheckedView.setBackgroundResource(R.drawable.bg_white);
            updateItems(fileInfo.mPath, position);
            mLastCheckedPosition = -1;
            mLastCheckedView = null;
        }else {
            if(mLastCheckedView != null)
                mLastCheckedView.setBackgroundResource(R.drawable.bg_white);
            mLastCheckedPosition = position;
//            mListView.setItemChecked(position, true);
//            mListView.setSelection(position);
           view.setBackgroundResource(R.drawable.bg_blue);
            mLastCheckedView = view;
        }
    }

    @Override
    public void onBackPressed() {

        if(mFileAdapter.getList().size() > 0) {
            FileInfo fileInfo = mFileAdapter.getList().get(0);

            if (fileInfo.mIsDir) {
                boolean isUpdated = updateItems(fileInfo.mPath, 0);
                if (!isUpdated) {
                    super.onBackPressed();
                }
            }
        } else {
            super.onBackPressed();
        }


    }

}
