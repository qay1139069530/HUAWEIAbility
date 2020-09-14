package com.qbase.huaweiability.ui.hms.fido.impl;

import android.os.CancellationSignal;
import androidx.annotation.NonNull;

/**
 * IBiometricPromptImpl
 */
interface IBiometricPromptImpl {

    void authenticate(boolean loginFlg, @NonNull CancellationSignal cancel,
                      @NonNull BiometricPromptManager.OnBiometricIdentifyCallback callback);

}
