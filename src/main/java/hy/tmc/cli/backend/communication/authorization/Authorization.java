package hy.tmc.cli.backend.communication.authorization;

import org.apache.commons.codec.binary.Base64;

public class Authorization {

    /**
     * Formats the string to Base64 encoded string.
     *
     * @param data to be encoded
     * @return encoded data
     */
    public static String encode(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

}
