package com.onmobile.rbt.baseline.http.Crypto;

import android.util.Log;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public final class HMACContext {
    private final  byte[] hmacKey;
    private final String base64EncodedHmacKey;
    private final int hmacKeySizeInBytes;

    public final byte[] getHmacKey() {
        Log.e("HMACContext.Class", "getHmacKey" + this.hmacKey + "");
        return hmacKey.clone();
    }

    public final String getBase64EncodedHmacKey() {
        return base64EncodedHmacKey;
    }

    public final  int getHmacKeySizeInBytes() {
        return hmacKeySizeInBytes;
    }


    private HMACContext(HMACContextBuilder builder) {
        super();
        this.hmacKey = builder.hmacKey;
        Log.e("HMACContext.Class", "Constructor" + this.hmacKey + "");
        this.base64EncodedHmacKey = builder.base64EncodedHmacKey;
        this.hmacKeySizeInBytes = builder.hmacKeySizeInBytes;
    }

    public static class HMACContextBuilder {

        private byte[] hmacKey;
        private String base64EncodedHmacKey;
        private int hmacKeySizeInBytes;

        public HMACContextBuilder hmacKey(byte[] hmacKey) {
            this.hmacKey = hmacKey;
            return this;
        }

        public HMACContextBuilder base64EncodedHmacKey(String base64EncodedHmacKey) {
            this.base64EncodedHmacKey = base64EncodedHmacKey;
            return this;
        }

        public HMACContextBuilder hmacKeySizeInBytes(int hmacKeySizeInBytes) {
            this.hmacKeySizeInBytes = hmacKeySizeInBytes;
            return this;
        }

        public HMACContext build() {
            return new HMACContext(this);
        }

    }

}
