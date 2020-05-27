package no.wtw.android.restserviceutils;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class CustomRequestFactory extends SimpleClientHttpRequestFactory {

    public CustomRequestFactory() {
        super();
        setConnectTimeout(10000);
        setReadTimeout(10000);
    }

}
