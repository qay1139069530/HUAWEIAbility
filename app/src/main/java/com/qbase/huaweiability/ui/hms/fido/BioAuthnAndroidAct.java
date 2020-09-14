package com.qbase.huaweiability.ui.hms.fido;

import android.hardware.biometrics.BiometricPrompt;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Build;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.RequiresApi;

import com.qbase.huaweiability.R;
import com.qbase.huaweiability.base.BaseAct;
import com.qbase.huaweiability.ui.hms.fido.impl.ACache;
import com.qbase.huaweiability.ui.hms.fido.impl.BiometricPromptManager;

import javax.crypto.Cipher;

import butterknife.BindView;

public class BioAuthnAndroidAct extends BaseAct {

    @BindView(R.id.textResult)
    TextView mTvResult;
    private ACache aCache;

    private BiometricPromptManager mManager;

    @Override
    protected int initContentViewLayout() {
        return R.layout.bioauthn_android_act;
    }

    @Override
    protected void onInitialize() {
        initToolBarTitle(getString(R.string.fido_android));
        mTvResult.setMovementMethod(ScrollingMovementMethod.getInstance());
        aCache = ACache.get(getApplicationContext());
    }

    private void setText(String text) {
        mTvResult.append("\n");
        mTvResult.append(text);
    }

    public void onCheckBioAuthn(View view) {
        if (Build.VERSION.SDK_INT >= 23) {
            mManager = BiometricPromptManager.from(this);
            if (mManager.isHardwareDetected() && mManager.hasEnrolledFingerprints() && mManager.isKeyguardSecure()) {
                setText("手机硬件支持指纹登录");
            } else {
                setText("手机硬件不支持指纹登录");
            }
        } else {
            setText("API 低于23,不支持指纹登录");
        }
    }

    public void onBioAuthnAuth(View view) {
        String password = "huawei&&123456";
        if (mManager.isBiometricPromptEnable()) {
            mManager.authenticate(false, new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onSucceeded(FingerprintManager.AuthenticationResult result) {
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
                        setText("开通成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onSucceeded(BiometricPrompt.AuthenticationResult result) {
                    try {
                        Cipher cipher = result.getCryptoObject().getCipher();
                        byte[] bytes = cipher.doFinal(password.getBytes());
                        //保存加密过后的字符串
                        setText("设置指纹保存的加密密码: " + Base64.encodeToString(bytes, Base64.URL_SAFE));
                        aCache.put("pwdEncode", Base64.encodeToString(bytes, Base64.URL_SAFE));
                        byte[] iv = cipher.getIV();
                        setText("设置指纹保存的加密IV: " + Base64.encodeToString(iv, Base64.URL_SAFE));
                        aCache.put("iv", Base64.encodeToString(iv, Base64.URL_SAFE));
                        setText("开通成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onError(int code, String reason) {
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }

    public void onBioAuthnLogin(View view) {
        if (mManager.isBiometricPromptEnable()) {
            mManager.authenticate(true, new BiometricPromptManager.OnBiometricIdentifyCallback() {
                @Override
                public void onUsePassword() {
                }

                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onSucceeded(FingerprintManager.AuthenticationResult result) {
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

                @RequiresApi(api = Build.VERSION_CODES.P)
                @Override
                public void onSucceeded(BiometricPrompt.AuthenticationResult result) {
                    try {
                        Cipher cipher = result.getCryptoObject().getCipher();
                        String text = aCache.getAsString("pwdEncode");
                        setText("获取保存的加密密码: " + text);
                        byte[] input = Base64.decode(text, Base64.URL_SAFE);
                        byte[] bytes = cipher.doFinal(input);
                        // 然后这里用原密码(当然是加密过的)调登录接口
                        setText("解密得出的原始密码: " + new String(bytes));
                        byte[] iv = cipher.getIV();
                        setText("IV: " + Base64.encodeToString(iv, Base64.URL_SAFE));
                        setText("登录成功");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailed() {
                }

                @Override
                public void onError(int code, String reason) {
                }

                @Override
                public void onCancel() {
                }
            });
        }
    }
}