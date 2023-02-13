package com.example.innotechsolutionstask.web.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class WebConstant {
    public static final String BLANK_FIELD_MESSAGE = "Field not be empty!";
    public static final String WRONG_ENTRY_MESSAGE = "Price and availability must be positive!";
    public static final String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
}
