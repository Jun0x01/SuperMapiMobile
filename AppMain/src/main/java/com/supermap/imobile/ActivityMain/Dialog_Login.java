package com.supermap.imobile.ActivityMain;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import com.supermap.data.CloudLicenseManager;
import com.supermap.data.LicenseStatus;
import com.supermap.imobile.ActivitySetting.SharedPreferenceManager;
import com.supermap.imobile.R;

import java.util.ArrayList;

/**
 * Created by Jun on 2017/8/14.
 */

public class Dialog_Login extends Dialog implements View.OnClickListener{

    public Dialog_Login(Context context) {
        super(context);
        init(context);
    }

    public Dialog_Login(Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected Context mContext;
    private void init(Context context){
        mContext = context;
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        // Remove the default title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        setContentView(R.layout.layout_license_configuration_cloud);

        initView();
    }


    private AutoCompleteTextView mEdit_UserName;
    private EditText mEdit_Password;
    private CheckBox mCheckPassword, mCheckDefaultAccount;

    private ArrayAdapter<String> mAdapterCodes;
    private ArrayList<String> mUserNames, mSavedAccount;

    final String
            mKey_UserNames = "UserNames",
            mKey_SavedAccount = "SavedAccount",
            mDefault_User = "_Jun",
            mDefault_Password = "159753",
            tag = "Login";


    protected void initView() {

        mEdit_UserName = (AutoCompleteTextView) findViewById(R.id.edit_username);
        mEdit_Password = (EditText) findViewById(R.id.edit_password);

        mCheckPassword = (CheckBox) findViewById(R.id.checkbox_remember_password);
        mCheckDefaultAccount = (CheckBox) findViewById(R.id.checkbox_default_account);

        mCheckDefaultAccount.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mEdit_UserName.setText(mDefault_User);
                    mEdit_Password.setText(mDefault_Password);
                } else {
                    mEdit_UserName.setText("");
                    mEdit_Password.setText("");
                }
            }
        });

        mUserNames = SharedPreferenceManager.getStrings(mKey_UserNames, null);
        mAdapterCodes = new ArrayAdapter<String>(getContext(), R.layout.listview_item_normal, R.id.text_ItemName, mUserNames);
        mEdit_UserName.setAdapter(mAdapterCodes);
        mEdit_UserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEdit_UserName.showDropDown();
            }
        });

        mEdit_UserName.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {

				if(hasFocus && mUserNames.size() >0){
                    mEdit_UserName.showDropDown();
				}
			}
		});

        mEdit_UserName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.length() == 0)
                    mEdit_Password.setText("");
            }
        });

        mSavedAccount = SharedPreferenceManager.getStrings(mKey_SavedAccount, null);
        if(mSavedAccount.size() == 2){
            mEdit_UserName.setText(mSavedAccount.get(0));
            mEdit_Password.setText(mSavedAccount.get(1));
            mCheckPassword.setChecked(true);
        }

        findViewById(R.id.btn_SignIn).setOnClickListener(this);
        initCloudLicenseManager();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_SignIn:
                signIn();
                break;
            default:
                break;
        }
    }

    CloudLicenseManager mCloudLicenseManager;

    private void initCloudLicenseManager(){
        mCloudLicenseManager = CloudLicenseManager.getInstance(getContext());
        mCloudLicenseManager.setLoginCallback(new CloudLicenseListener());
    }


    class CloudLicenseListener implements CloudLicenseManager.LicenseLoginCallback{

        @Override
        public void loginSuccess(LicenseStatus licenseStatus) {
            mProgressDialog.dismiss();

            boolean isValid = licenseStatus.isLicenseValid();

            if(isValid){
                String user = mEdit_UserName.getText().toString();
                String password = mEdit_Password.getText().toString();

                if(!mCheckDefaultAccount.isChecked() && !user.equals(mDefault_User)){
                    SharedPreferenceManager.addString(mKey_UserNames, user);

                    if (mCheckPassword.isChecked()){

                        SharedPreferenceManager.setString(mKey_SavedAccount, user + "," + password);
                    }
                }

                dismiss();
            } else {
                showMessage(getString(R.string.license_sign_invalid));
            }

            SharedPreferenceManager.setBoolean("BLogined", isValid);

        }

        @Override
        public void loginFailed(String s) {
            mProgressDialog.dismiss();
            showMessage(s);
        }
    }

    ProgressDialog mProgressDialog;

    private void signIn() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.license_sign_in));
            mProgressDialog.setCancelable(false);
        }

        String user = mEdit_UserName.getText().toString();
        String password = mEdit_Password.getText().toString();

        if (user.length() < 4) {
            showMessage(getString(R.string.license_sign_username_less));
            return;
        }

        if (password.length() < 6) {
            showMessage(getString(R.string.license_sign_password_less));
            return;
        }

        mCloudLicenseManager.login(user, password);
        mProgressDialog.show();

    }

    private void showMessage(String msg){
        Toast toast = Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        Log.d(tag, msg);
    }

    private String getString(int id){
        return getContext().getResources().getString(id);
    }
}
