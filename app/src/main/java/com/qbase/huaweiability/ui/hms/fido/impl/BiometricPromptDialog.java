package com.qbase.huaweiability.ui.hms.fido.impl;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.qbase.huaweiability.R;

/**
 * BiometricPromptDialog
 */
public class BiometricPromptDialog extends DialogFragment {

    public static final int STATE_NORMAL = 1;
    public static final int STATE_FAILED = 2;
    public static final int STATE_ERROR = 3;
    public static final int STATE_SUCCEED = 4;
    private TextView tv_status, tv_cancel;
    private Activity mActivity;
    private OnBiometricPromptDialogActionCallback mDialogActionCallback;

    public interface OnBiometricPromptDialogActionCallback {
        void onDialogDismiss();

        void onCancel();
    }

    public static BiometricPromptDialog newInstance() {
        BiometricPromptDialog dialog = new BiometricPromptDialog();
        return dialog;
    }

    public void setOnBiometricPromptDialogActionCallback(OnBiometricPromptDialogActionCallback callback) {
        mDialogActionCallback = callback;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupWindow(getDialog().getWindow());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_bioauthn, container);

        RelativeLayout rootView = view.findViewById(R.id.root_view);
        rootView.setClickable(false);

        tv_status = view.findViewById(R.id.tv_status);
        tv_cancel = view.findViewById(R.id.tv_cancel);

        tv_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mDialogActionCallback != null) {
                    mDialogActionCallback.onCancel();
                }
                dismiss();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (Activity) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity = activity;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawableResource(R.color.alpha_half);
        }
        return dialog;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);

        if (mDialogActionCallback != null) {
            mDialogActionCallback.onDialogDismiss();
        }
    }

    private void setupWindow(Window window) {
        if (window != null) {
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.gravity = Gravity.CENTER;
            lp.dimAmount = 0;
            lp.flags |= WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(lp);
            window.setBackgroundDrawableResource(R.color.alpha_half);
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public void setState(int state) {
        switch (state) {
            case STATE_NORMAL:
                tv_status.setTextColor(ContextCompat.getColor(mActivity, R.color.Black));
                tv_status.setText("认证");
                tv_cancel.setVisibility(View.VISIBLE);
                break;
            case STATE_FAILED:
                tv_status.setTextColor(ContextCompat.getColor(mActivity, R.color.Red));
                tv_status.setText("认证Failed");
                tv_cancel.setVisibility(View.VISIBLE);
                break;
            case STATE_ERROR:
                tv_status.setTextColor(ContextCompat.getColor(mActivity, R.color.Red));
                tv_status.setText("认证Error");
                tv_cancel.setVisibility(View.VISIBLE);
                break;
            case STATE_SUCCEED:
                tv_status.setTextColor(ContextCompat.getColor(mActivity, R.color.Green_500));
                tv_status.setText("认证成功");
                tv_cancel.setVisibility(View.VISIBLE);

                tv_status.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dismiss();
                    }
                }, 500);
                break;
        }
    }

}
