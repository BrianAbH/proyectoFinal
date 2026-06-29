package ec.edu.ug.proyectofinal.CapaServicio.Network;

import java.security.SecureRandom;
import java.security.cert.X509Certificate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import okhttp3.OkHttpClient;

public class UnsafeOkHttpClient {

    private UnsafeOkHttpClient(){}

    public static OkHttpClient getClient() {

        try {

            final TrustManager[] trustAllCerts = new TrustManager[]{
                    new X509TrustManager() {

                        @Override
                        public void checkClientTrusted(X509Certificate[] chain, String authType){}

                        @Override
                        public void checkServerTrusted(X509Certificate[] chain, String authType){}

                        @Override
                        public X509Certificate[] getAcceptedIssuers() {
                            return new X509Certificate[]{};
                        }
                    }
            };

            SSLContext sslContext = SSLContext.getInstance("SSL");
            sslContext.init(null, trustAllCerts, new SecureRandom());

            return new OkHttpClient.Builder()
                    .sslSocketFactory(
                            sslContext.getSocketFactory(),
                            (X509TrustManager) trustAllCerts[0]
                    )
                    .hostnameVerifier((hostname, session) -> true)
                    .build();

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}