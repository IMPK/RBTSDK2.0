package com.onmobile.rbt.baseline.reciever;

public interface SmsListener {
    void messageReceived(String phone_number, String messageText, String sender);
}
