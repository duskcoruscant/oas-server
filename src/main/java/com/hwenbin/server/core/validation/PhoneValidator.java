package com.hwenbin.server.core.validation;

import cn.hutool.core.util.StrUtil;
import com.hwenbin.server.util.ValidationUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

/**
 * @author hwb
 * @create 2022-03-29
 */
public class PhoneValidator implements ConstraintValidator<Phone, String> {

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        // 如果手机号码为空，默认不校验，即校验通过
        if (StrUtil.isEmpty(s)) {
            return true;
        }
        // 校验手机号码
        return ValidationUtils.isPhoneNumber(s);
    }

    @Override
    public void initialize(Phone constraintAnnotation) {

    }

}
