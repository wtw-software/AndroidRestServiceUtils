package no.wtw.android.restserviceutils;

import org.springframework.http.client.SimpleClientHttpRequestFactory;

public class CustomRequestFactory extends SimpleClientHttpRequestFactory {

    public CustomRequestFactory(RestServiceAPI api) {
        super();
        setConnectTimeout(api.getConnectTimeout());
        setReadTimeout(api.getReadTimeout());
    }

}
