package com.ocdl.proxy.util;

public class MessageUtils {

    /**
     * unpack kafka message
     * @param message the value of kafka message
     * @return part of message that is useful
     */
    public static String unpackMessage(String message) {

        // payload should be the form of "projectName + "_" + send_topic"
        int posPaylod = message.indexOf("payload");
        int start = posPaylod < 0 ? -1 : message.indexOf("\"", posPaylod) + 3;
        int end = start < 0 ? -1 : message.indexOf("\"", start);
        String msg = end < 0? "" : message.substring(start, end);

        return msg.indexOf("_") < 0? null : msg;

    }
}
