package hy.tmc.cli.backendCommunication.Authorization;

import org.apache.commons.codec.binary.Base64;

public class Authorization {

    public static String encode(String data) {
        return Base64.encodeBase64String(data.getBytes());
    }

}
