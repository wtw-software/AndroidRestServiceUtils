package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;

import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException;

public class Link<T> implements Serializable {

    public static final String LINK_SELF = "self";

    private T resource;
    private Class<T> clazz;

    @SerializedName("key")
    protected String relation;

    @SerializedName("href")
    protected URL url;

    public String getRelation() {
        return relation;
    }

    public URL getUrl() {
        return url;
    }

    public T httpGet(RestTemplate restTemplate) {
        if (clazz == null)
            throw new RuntimeException("Class of return object must be set");
        if (resource == null)
            resource = restTemplate.getForObject(getUrl().toExternalForm(), clazz);
        return resource;
    }

    public T httpGet(RestTemplate restTemplate, JsonEncodedQuery query) {
        if (clazz == null)
            throw new RuntimeException("Class of return object must be set");
        if (resource == null) {
            String url = getUrl().toExternalForm();
            url = url.replace("?data=Base64", ""); // TODO: remove this hack
            resource = restTemplate.getForObject(url + "?data=" + query.encode(true), clazz);
        }
        return resource;
    }

    public T httpPut(RestTemplate restTemplate, T body) {
        HttpEntity<T> requestEntity = new HttpEntity<>(body, new HttpHeaders());
        resource = restTemplate.exchange(URI.create(getUrl().toExternalForm()), HttpMethod.PUT, requestEntity, clazz).getBody();
        return resource;
    }

    public T httpPost(RestTemplate restTemplate, Object body) {
        return restTemplate.postForEntity(getUrl().toExternalForm(), body, clazz, new HashMap<String, Object>()).getBody();
    }

    public void httpDelete(RestTemplate restTemplate) {
        restTemplate.delete(getUrl().toExternalForm());
    }

    public T getResource() throws LinkNotResolvedException {
        if (resource == null)
            throw new LinkNotResolvedException();
        return resource;
    }

    public void setClass(Class<T> clazz) {
        this.clazz = clazz;
    }

}
