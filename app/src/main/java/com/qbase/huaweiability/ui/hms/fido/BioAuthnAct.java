package com.qbase.huaweiability.ui.hms.fido;

import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.huawei.hms.support.api.fido.bioauthn.BioAuthnCallback;
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnManager;
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnPrompt;
import com.huawei.hms.support.api.fido.bioauthn.BioAuthnResult;
import com.huawei.hms.support.api.fido.bioauthn.CryptoObject;
import com.qbase.huaweiability.R;
import com.qbase.huaweiability.base.BaseAct;
import com.qbase.huaweiability.ui.hms.fido.impl.ACache;
import com.qbase.huaweiability.ui.hms.fido.impl.KeyGenTool;

import javax.crypto.Cipher;

import butterknife.BindView;

public class BioAuthnAct extends BaseAct {

    @BindView(R.id.textResult)
    TextView mTvResult;

    private KeyGenTool mKeyGenTool;
    private ACache aCache;
    private String password = "huawei&&888899";

    @Override
    protected int initContentViewLayout() {
        return R.layout.bioauthn_act;
    }

    @Override
    protected void onInitialize() {
        initToolBarTitle(getString(R.string.fido_bioauthn));
        mKeyGenTool = new KeyGenTool(this);
        aCache = ACache.get(getApplicationContext());
    }

    private void setText(String text) {
        mTvResult.append("\n");
        mTvResult.append(text);
    }

    public void onCheckBioAuthn(View view) {
        // 是否支持指纹认证
        BioAuthnManager bioAuthnManager = new BioAuthnManager(this);
        int errorCode = bioAuthnManager.canAuth();
        if (errorCode != 0) {
            // 不支持指纹认证
            setText("不支持指纹认证");
            return;
        }
        setText("支持指纹认证");
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onBioAuthnAuth(View view) {
        BioAuthnPrompt bioAuthnPrompt = new BioAuthnPrompt(this, ContextCompat.getMainExecutor(this), authCallback);
        // 构建提示信息
        BioAuthnPrompt.PromptInfo.Builder builder =
                new BioAuthnPrompt.PromptInfo.Builder().setTitle("这是标题")
                        .setSubtitle("这里填写的是二级标题")
                        .setDescription("这里填写的是描述");
        // 首先会提示用户使用指纹认证，但是也提供选项，可以使用PIN码、图形解锁或锁屏密码进行认证。
        // 但是，如果这里设置为true，则不能设置setNegativeButtonText(CharSequence)。
//        builder.setDeviceCredentialAllowed(true);
        // 设置取消按钮标题。如果设置了该值，则不能setDeviceCredentialAllowed(true)。
         builder.setNegativeButtonText("This is the 'Cancel' button.");

        BioAuthnPrompt.PromptInfo info = builder.build();
        CryptoObject cryptoObject = new CryptoObject(mKeyGenTool.getEncryptCipher());
        bioAuthnPrompt.auth(info, cryptoObject);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public void onBioAuthnLogin(View view) {
        BioAuthnPrompt bioAuthnPrompt = new BioAuthnPrompt(this, ContextCompat.getMainExecutor(this), loginCallback);
        // 构建提示信息
        BioAuthnPrompt.PromptInfo.Builder builder =
                new BioAuthnPrompt.PromptInfo.Builder().setTitle("这是标题")
                        .setSubtitle("这里填写的是二级标题")
                        .setDescription("这里填写的是描述");
        // 首先会提示用户使用指纹认证，但是也提供选项，可以使用PIN码、图形解锁或锁屏密码进行认证。
        // 但是，如果这里设置为true，则不能设置setNegativeButtonText(CharSequence)。
//        builder.setDeviceCredentialAllowed(true);
        // 设置取消按钮标题。如果设置了该值，则不能setDeviceCredentialAllowed(true)。
         builder.setNegativeButtonText("This is the 'Cancel' button.");
        BioAuthnPrompt.PromptInfo info = builder.build();
        String ivStr = aCache.getAsString("iv");
        byte[] iv = Base64.decode(ivStr, Base64.URL_SAFE);
        CryptoObject cryptoObject = new CryptoObject(mKeyGenTool.getDecryptCipher(iv));
        bioAuthnPrompt.auth(info, cryptoObject);
    }

    // 回调
    BioAuthnCallback authCallback = new BioAuthnCallback() {
        @Override
        public void onAuthError(int errMsgId, CharSequence errString) {
            // 认证错误
            setText("认证错误");
        }

        @Override
        public void onAuthSucceeded(BioAuthnResult result) {
            // 认证成功
            try {
                // 加密后的密码和iv可保存在服务器,登录时通过接口根据账号获取
                setText("原密码: " + password);
                Cipher cipher = result.getCryptoObject().getCipher();
                byte[] bytes = cipher.doFinal(password.getBytes());
                setText("设置指纹时保存的加密密码: " + Base64.encodeToString(bytes, Base64.URL_SAFE));
                aCache.put("pwdEncode", Base64.encodeToString(bytes, Base64.URL_SAFE));
                byte[] iv = cipher.getIV();
                setText("设置指纹时保存的加密IV: " + Base64.encodeToString(iv, Base64.URL_SAFE));
                aCache.put("iv", Base64.encodeToString(iv, Base64.URL_SAFE));
                setText("认证成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAuthFailed() {
            // 认证失败
            setText("认证失败");
        }
    };

    // 回调
    BioAuthnCallback loginCallback = new BioAuthnCallback() {
        @Override
        public void onAuthError(int errMsgId, CharSequence errString) {
            // 登陆错误
            setText("登陆错误");
        }

        @Override
        public void onAuthSucceeded(BioAuthnResult result) {
            // 登陆成功
            try {
                Cipher cipher = result.getCryptoObject().getCipher();
                String text = aCache.getAsString("pwdEncode");
                setText("获取保存的加密密码: " + text);
                byte[] input = Base64.decode(text, Base64.URL_SAFE);
                byte[] bytes = cipher.doFinal(input);
                // 然后这里用原密码(当然是加密过的)调登录接口
                setText("解密得出的加密的登录密码: " + new String(bytes));
                byte[] iv = cipher.getIV();
                setText("IV: " + Base64.encodeToString(iv, Base64.URL_SAFE));
                setText("登录成功");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onAuthFailed() {
            // 登陆失败
            setText("登陆失败");
        }
    };

}