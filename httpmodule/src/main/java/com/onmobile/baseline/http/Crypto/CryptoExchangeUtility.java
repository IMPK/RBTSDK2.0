package com.onmobile.baseline.http.Crypto;

/**
 * Created by prateek.khurana on 13-Jun 2019
 */
public class CryptoExchangeUtility {
    /**
     * Encrypted current-time, SYMMETRIC-KEY, HMAC-KEY Key encryption bytes order:
     * first 4 bytes - unix epoch seconds; next 2 bytes - symmetric-key size; next
     * symmetric-key bytes; next 2 bytes hmac-key size; next hmac-key bytes
     *
     * int keyExchangeSize = 4+2+symmetricKeySize+2+hmacKeySize
     */
    public static byte[] prepareKeyExchangeBytes(final int unixEpochSeconds, final int symmetricKeySizeInBytes,
                                                 final byte[] symmetricKey, final int hmacKeySizeInBytes, final byte[] hmacKey) {

        if (symmetricKey == null || hmacKey == null) {
            throw new IllegalArgumentException("symmetricKey or hmacKey cannot be null!");
        }

        /**
         * In two bytes we can keep 0 to 65535 (2^16 -1)
         *
         */
        final int _2byteMaxRange = 65535;
        if (symmetricKeySizeInBytes > _2byteMaxRange || hmacKeySizeInBytes > _2byteMaxRange) {
            throw new IllegalArgumentException("symmetricKey or hmacKey max limit is :" + _2byteMaxRange);

        }

        if (symmetricKey.length != symmetricKeySizeInBytes) {
            throw new IllegalArgumentException("symmetricKeySize not matches  to the symmetricKey bytes");
        }

        if (hmacKey.length != hmacKeySizeInBytes) {
            throw new IllegalArgumentException("hmacKeySize not matches  to the hmacKey bytes");
        }

        final int keyExchangeSize = 4 + 2 + symmetricKeySizeInBytes + 2 + hmacKeySizeInBytes;

        byte[] keyExchange = new byte[keyExchangeSize];

        // copy epoch bytes
        System.arraycopy(ByteConversionUtility.intToBytes(unixEpochSeconds), 0, keyExchange, 0, 4);

        System.arraycopy(ByteConversionUtility.intTo2Bytes(symmetricKeySizeInBytes), 0, keyExchange, 4, 2);

        System.arraycopy(symmetricKey, 0, keyExchange, 4 + 2, symmetricKeySizeInBytes);

        System.arraycopy(ByteConversionUtility.intTo2Bytes(hmacKeySizeInBytes), 0, keyExchange,
                4 + 2 + symmetricKeySizeInBytes, 2);

        System.arraycopy(hmacKey, 0, keyExchange, 4 + 2 + symmetricKeySizeInBytes + 2, hmacKeySizeInBytes);

        return keyExchange;
    }
}
