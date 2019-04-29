package com.example.example.fragment;

import android.Manifest;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.EditText;

import com.example.example.Present.FilePresent;
import com.example.example.R;
import com.example.example.app.Constants;
import com.example.example.base.BaseFragment;

import androidx.core.app.ActivityCompat;
import butterknife.BindView;
import butterknife.OnClick;


/**
 * Stone
 * 2019/4/11
 * 文件操作示例
 **/
public class FileFragment extends BaseFragment<FilePresent> {

    @BindView(R.id.et_file)
    EditText etFile;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_file;
    }

    @Override
    public void initView(Bundle savedInstanceState) {
        String[] PERMISSIONS_STORAGE = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        ActivityCompat.requestPermissions(getActivity(), PERMISSIONS_STORAGE , 1);
    }

    @Override
    public FilePresent getPInstance() {
        return new FilePresent();
    }

    @OnClick(R.id.btn_file_write)
    public void onClickFileWrite(View view) {
        getP().write(etFile.getText().toString());
    }

    @OnClick(R.id.btn_file_read)
    public void onClickFileRead(View view) {
        getP().read();
    }

    @OnClick(R.id.btn_file_decode)
    public void onClickFileDecode(View view) {
        showToast(getString(R.string.isComing));
        getP().decode(etFile.getText().toString());
    }

    @OnClick(R.id.btn_file_encode)
    public void onClickFileEncode(View view) {
        showToast(getString(R.string.isComing));
        getP().encode(etFile.getText().toString());
    }

    public void setEtText(String msg){
        etFile.setText(msg);
    }
}
