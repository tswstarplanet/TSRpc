package com.wts.tsrpc.common.utils;

import com.wts.tsrpc.exception.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class NetworkUtils {
    private static final Logger logger = LoggerFactory.getLogger(NetworkUtils.class);

    public static String getLocalHost() {
        try {
            InetAddress localHost = InetAddress.getLocalHost();
            return localHost.getHostAddress();
        } catch (UnknownHostException e) {
            logger.error("Get local host error", e);
            throw new SystemException("Get local host error", e);
        }
    }

}
