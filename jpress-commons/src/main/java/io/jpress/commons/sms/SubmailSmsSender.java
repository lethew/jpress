package io.jpress.commons.sms;

import io.jpress.JPressConsts;
import io.jpress.JPressOptions;

public class SubmailSmsSender implements ISmsSender {
    @Override
    public boolean send(SmsMessage sms) {
        String appKey = JPressOptions.get(JPressConsts.OPTION_CONNECTION_SMS_APPID);
        String appSecret = JPressOptions.get(JPressConsts.OPTION_CONNECTION_SMS_APPSECRET);
        return false;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
    }
}
