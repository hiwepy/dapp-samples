package com.github.hiwepy.api.util;

import okhttp3.CipherSuite;
import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OkHttpClientCreater {

    public static OkHttpClient createOKHttpClient(HttpLoggingInterceptor.Level level) {

        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
        loggingInterceptor.setLevel(level);

        final OkHttpClient.Builder builder = new OkHttpClient.Builder().connectionSpecs(CONNECTION_SPEC_LIST);
        return builder
                .addInterceptor(loggingInterceptor)
                .addNetworkInterceptor(loggingInterceptor)
                .retryOnConnectionFailure(true)
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(90, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .build();
    }


    private static final CipherSuite[] INFURA_CIPHER_SUITES =
            new CipherSuite[]{
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_CHACHA20_POLY1305_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_CHACHA20_POLY1305_SHA256,

                    // Note that the following cipher suites are all on HTTP/2's bad cipher suites list.
                    // We'll
                    // continue to include them until better suites are commonly available. For example,
                    // none
                    // of the better cipher suites listed above shipped with Android 4.4 or Java 7.
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_ECDHE_ECDSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_128_GCM_SHA256,
                    CipherSuite.TLS_RSA_WITH_AES_256_GCM_SHA384,
                    CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA,
                    CipherSuite.TLS_RSA_WITH_3DES_EDE_CBC_SHA,

                    // Additional INFURA CipherSuites
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_128_CBC_SHA256,
                    CipherSuite.TLS_ECDHE_RSA_WITH_AES_256_CBC_SHA384,
                    CipherSuite.TLS_RSA_WITH_AES_128_CBC_SHA256,
                    CipherSuite.TLS_RSA_WITH_AES_256_CBC_SHA256
            };

    private static final ConnectionSpec INFURA_CIPHER_SUITE_SPEC =
            new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                    .cipherSuites(INFURA_CIPHER_SUITES)
                    .build();

    /**
     * The list of {@link ConnectionSpec} instances used by the connection.
     */
    private static final List<ConnectionSpec> CONNECTION_SPEC_LIST =
            Arrays.asList(INFURA_CIPHER_SUITE_SPEC,  okhttp3.ConnectionSpec.CLEARTEXT);

}
