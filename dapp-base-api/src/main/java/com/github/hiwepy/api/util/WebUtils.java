package com.github.hiwepy.api.util;


import com.github.hiwepy.api.XHeaders;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;


@Slf4j
public class WebUtils extends org.springframework.web.util.WebUtils {

    private static String[] xheaders = new String[] { "X-Forwarded-For", "x-forwarded-for" };
    private static String[] headers = new String[] { "Cdn-Src-Ip", "Proxy-Client-IP", "WL-Proxy-Client-IP", "X-Real-IP",
            "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR" };
    private static String LOCAL_HOST = "localhost";
    private static String LOCAL_IP6 = "0:0:0:0:0:0:0:1";
    private static String LOCAL_IP = "127.0.0.1";
    private static int IPv4_LENGTH = 15;
    private static String UNKNOWN = "unknown";

    /**
     * 获取请求客户端IP地址，支持代理服务器
     *
     * @param request {@link HttpServletRequest} 对象
     * @return IP地址
     */
    public static String getRemoteAddr(HttpServletRequest request) {

        // 1、获取客户端IP地址，支持代理服务器
        String remoteAddr = UNKNOWN;
        for (String xHeader : xheaders) {
            remoteAddr = request.getHeader(xHeader);
            log.debug(" {} : {} ", xHeader, remoteAddr);
            if (StringUtils.hasText(remoteAddr) && !UNKNOWN.equalsIgnoreCase(remoteAddr)) {
                // 多次反向代理后会有多个ip值，第一个ip才是真实ip
                if (remoteAddr.indexOf(",") != -1) {
                    remoteAddr = remoteAddr.split(",")[0];
                }
                break;
            }
        }
        if (!StringUtils.hasText(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr)) {
            for (String header : headers) {

                remoteAddr = request.getHeader(header);
                log.debug(" {} : {} ", header, remoteAddr);

                if (StringUtils.hasText(remoteAddr) && !UNKNOWN.equalsIgnoreCase(remoteAddr)) {
                    break;
                }
            }
        }

        // 对于通过多个代理的情况，第一个IP为客户端真实IP,多个IP按照','分割
        if (StringUtils.hasText(remoteAddr) && remoteAddr.length() > IPv4_LENGTH) {
            if (remoteAddr.indexOf(",") > 0) {
                remoteAddr = remoteAddr.substring(0, remoteAddr.indexOf(","));
            }
        }

        // 2、没有取得特定标记的值
        if (!StringUtils.hasText(remoteAddr) || UNKNOWN.equalsIgnoreCase(remoteAddr)) {
            remoteAddr = request.getRemoteAddr();
        }
        // 3、判断是否localhost访问
        if (LOCAL_HOST.equals(remoteAddr) || LOCAL_IP6.equals(remoteAddr)) {
            remoteAddr = LOCAL_IP;
        }

        return remoteAddr;
    }

    /**
     *  获得请求的客户端信息【ip,port,name】
     *  @param request {@link HttpServletRequest} 对象
     *  @return 客户端信息[ip,port,name]
     */
    public static String[] getRemoteInfo(HttpServletRequest request) {
        if (request == null) {
            return new String[] { "", "", "" };
        }
        return new String[] { getRemoteAddr(request), request.getRemotePort() + "", request.getRemoteHost()};
    }

    public static boolean isSameSegment(HttpServletRequest request) {
        String remoteIp = getRemoteAddr(request);
        return InetAddressUtils.isSameSegment(remoteIp);
    }

    public static String getLocalHostAddress() {
        return InetAddressUtils.getLocalHostAddress();
    }

    public static String getLocalHostName() {
        return InetAddressUtils.getLocalHostName();
    }

    public static String getDeviceId(HttpServletRequest request) {
        // 1、判断是否 Apple 设备
        String deviceId = request.getHeader(XHeaders.X_DEVICE_IDFA);
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getHeader(XHeaders.X_DEVICE_OAID);
        }
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getHeader(XHeaders.X_DEVICE_OPENUDID);
        }
        // 2、判断是否 Android 设备
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getHeader(XHeaders.X_DEVICE_IMEI);
        }
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getHeader(XHeaders.X_DEVICE_ANDROIDID);
        }
        if (!StringUtils.hasText(deviceId)) {
            deviceId = request.getHeader(XHeaders.X_DEVICE_OAID);
        }
        return deviceId;
    }

    public static HttpServletRequest getHttpServletRequest() {
        try {
            RequestAttributes requestAttributes = getRequestAttributesSafely();
            if (requestAttributes != null) {
                return ((ServletRequestAttributes) requestAttributes).getRequest();
            }
        } catch (Exception e) {
            log.error(e.getMessage());
        }
        return null;
    }

    public static RequestAttributes getRequestAttributesSafely() {
        RequestAttributes requestAttributes = null;
        try {
            requestAttributes = RequestContextHolder.currentRequestAttributes();
        } catch (IllegalStateException e) {

        }
        return requestAttributes;
    }

}
