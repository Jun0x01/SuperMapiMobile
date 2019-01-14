package com.supermap.imobile.Menus;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;

import com.supermap.imobile.R;
import com.supermap.mapping.MapControl;
import com.supermap.mapping.SnapMode;
import com.supermap.mapping.SnapSetting;

/**
 * Created by Jun on 2017/8/30.
 */

public class Dialog_SnapSetting extends Dialog_Base {
    public Dialog_SnapSetting(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(true);
    }

    public Dialog_SnapSetting(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
        setCanceledOnTouchOutside(true);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setContentView(R.layout.layout_pop_dialog_snap_setting);
        super.onCreate(savedInstanceState);
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private EditText mEditTolerance;
    private EditText mEditMaxCount;
    private CheckBox mCheck_SnapPoint_Along_Linne;
    private CheckBox mCheck_SnapPoint_EndPoint;
    private CheckBox mCheck_SnapPoint_OnLine;
    private CheckBox mCheck_SnapPoint_Node;

    @Override
    protected void initView() {

        mEditMaxCount = (EditText) findViewById(R.id.edit_snap_max_count);
        mEditTolerance = (EditText) findViewById(R.id.edit_snap_tolerance);

        mCheck_SnapPoint_Along_Linne = (CheckBox) findViewById(R.id.checkbox_snap_along_line);
        mCheck_SnapPoint_EndPoint = (CheckBox) findViewById(R.id.checkbox_snap_end_point);
        mCheck_SnapPoint_OnLine = (CheckBox) findViewById(R.id.checkbox_snap_point_on_line);
        mCheck_SnapPoint_Node = (CheckBox) findViewById(R.id.checkbox_snap_node);

        if(mSnapsetting != null) {
            mEditMaxCount.setText("" + mSnapsetting.getMaxSnappedCount());
            mEditTolerance.setText("" + mSnapsetting.getTolerance());
            mCheck_SnapPoint_Along_Linne.setChecked(mSnapsetting.get(SnapMode.POINT_DRAG_LINE));
            mCheck_SnapPoint_EndPoint.setChecked(mSnapsetting.get(SnapMode.POINT_ON_ENDPOINT));
            mCheck_SnapPoint_OnLine.setChecked(mSnapsetting.get(SnapMode.POINT_ON_LINE));
            mCheck_SnapPoint_Node.setChecked(mSnapsetting.get(SnapMode.POINT_ON_POINT));
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
    }

    private SnapSetting mSnapsetting;
    private MapControl mMapControl;
    public void show(MapControl mapControl, SnapSetting snapSetting){
        mSnapsetting = snapSetting;
        mMapControl = mapControl;
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if(mSnapsetting != null){
                    try {
                        String content = mEditMaxCount.getText().toString();
                        if (!content.isEmpty())
                            mSnapsetting.setMaxSnappedCount(Integer.parseInt(content));
                        content = mEditTolerance.getText().toString();
                        if (!content.isEmpty())
                            mSnapsetting.setTolerance(Integer.parseInt(content));
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    mSnapsetting.set(SnapMode.POINT_DRAG_LINE, mCheck_SnapPoint_Along_Linne.isChecked());
                    mSnapsetting.set(SnapMode.POINT_ON_ENDPOINT, mCheck_SnapPoint_EndPoint.isChecked());
                    mSnapsetting.set(SnapMode.POINT_ON_LINE, mCheck_SnapPoint_OnLine.isChecked());
                    mSnapsetting.set(SnapMode.POINT_ON_POINT, mCheck_SnapPoint_Node.isChecked());
                    mMapControl.setSnapSetting(mSnapsetting);
                    mMapControl = null;
                }
                mSnapsetting = null;
            }
        });
        show();

    }


}
