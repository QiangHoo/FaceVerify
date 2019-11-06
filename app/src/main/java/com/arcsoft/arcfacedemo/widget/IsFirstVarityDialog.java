package com.arcsoft.arcfacedemo.widget;

import android.app.Dialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.arcsoft.arcfacedemo.R;


public class IsFirstVarityDialog extends Dialog implements View.OnClickListener{
    private Context context;
    private Dialog mDialog = null;
    private OnDialogClickListener mClickListener;

    public IsFirstVarityDialog(@NonNull Context context) {
        super(context, R.style.is_first_access_dialog);
        this.context = context;
    }

    public void showDialog(OnDialogClickListener listener, String title, String content,
                           String btnConfirmText, String btnCancelText){
        this.mClickListener = listener;
        View contentView = LayoutInflater.from(context)
                .inflate(R.layout.dialog_content_view, null);
        final TextView btnPositive = contentView.findViewById(R.id.btn_confirm);
        final TextView btnNegative = contentView.findViewById(R.id.btn_cancel);
        TextView tvTitle = contentView.findViewById(R.id.tv_title);
        TextView tvContent = contentView.findViewById(R.id.tv_content);
        btnPositive.setOnClickListener(this);
        btnNegative.setOnClickListener(this);
        tvTitle.setText(title);
        tvContent.setText(content);
        btnPositive.setText(btnConfirmText);
        btnNegative.setText(btnCancelText);
        setContentView(contentView);
        Window window = this.getWindow();
        window.setWindowAnimations(R.style.dialog_animal);
        mDialog = this;
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //点击确定按钮
            case R.id.btn_confirm:
                mClickListener.onConfirm();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                break;
            //点击取消按钮
            case R.id.btn_cancel:
                mClickListener.onCancel();
                if (mDialog != null) {
                    mDialog.dismiss();
                }
                break;
            default:
                break;
        }
    }

    public interface OnDialogClickListener{
        void onConfirm();
        void onCancel();
    }
}
