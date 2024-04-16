package com.apzda.cloud.uc.setting;

import com.apzda.cloud.config.Setting;
import com.apzda.cloud.gsvc.utils.I18nUtils;
import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

@Data
public class UcSetting implements Setting, Serializable {

    @Serial
    private static final long serialVersionUID = -474134184414429761L;

    /**
     * 小于0表示不开启验证码; 等于0表示强制开启验证码; 大于0表示密码错误多少次后开启验证码
     */
    private int thresholdForCaptcha = -1;

    @Override
    public String name() {
        return I18nUtils.t("uc.setting.name", "User Center Setting");
    }

}
