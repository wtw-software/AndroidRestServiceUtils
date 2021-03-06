package no.wtw.android.restserviceutils.resource;

import com.google.gson.annotations.SerializedName;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import no.wtw.android.restserviceutils.exceptions.LinkNotResolvedException;
import no.wtw.android.restserviceutils.exceptions.RestServiceException;

public class Link<T> implements Serializable {

    private static final String TAG = Link.class.getSimpleName();

    public static final String LINK_SELF = "self";

    private transient T resource;
    private transient Class<T> clazz;

    @SerializedName("key")
    protected String relation;

    @SerializedName("href")
    protected String url;

    private Map<String, String> queryParams;

    public Link() {
    }

    public Link(String relation, String url) {
        this.relation = relation;
        this.url = url;
    }

    public String getRelation() {
        return relation;
    }

    public String getUrl() {
        return url;
    }

    public T httpGet(final RestTemplate restTemplate) throws RestServiceException {
        if (clazz == null)
            throw new RuntimeException("Class of return object must be set");
        if (getQueryParams() != null)
            return httpGet(restTemplate, getQueryParams());
        return executeHttpCall(() -> {
            resource = restTemplate.getForObject(getUrl(), clazz);
            return resource;
        });
    }

    public T httpGet(final RestTemplate restTemplate, final JsonEncodedQuery query) throws RestServiceException {
        if (clazz == null)
            throw new RuntimeException("Class of return object must be set");
        return executeHttpCall(() -> {
            String url = getUrl().replace("?data=Base64", ""); // TODO: remove this hack
            resource = restTemplate.getForObject(url + "?data=" + query.encode(true), clazz);
            return resource;
        });
    }

    public T httpGet(final RestTemplate restTemplate, final Map<String, String> queryParams) throws RestServiceException {
        if (clazz == null)
            throw new RuntimeException("Class of return object must be set");
        return executeHttpCall(() -> {
            String queryString = "";
            for (String key : queryParams.keySet())
                queryString += key + "=" + queryParams.get(key) + "&";
            resource = restTemplate.getForObject(getUrl() + "?" + queryString.substring(0, queryString.length() - 1), clazz);
            return resource;
        });
    }

    public T httpPut(final RestTemplate restTemplate, final T body) throws RestServiceException {
        return executeHttpCall(() -> {
            HttpEntity<T> requestEntity = new HttpEntity<>(body, new HttpHeaders());
            ResponseEntity<T> responseEntity = restTemplate.exchange(URI.create(getUrl()), HttpMethod.PUT, requestEntity, clazz);
            if (HttpStatus.OK.equals(responseEntity.getStatusCode()))
                resource = responseEntity.getBody();
            return resource;
        });
    }

    public T httpPost(final RestTemplate restTemplate, final Object body) throws RestServiceException {
        return executeHttpCall(() -> restTemplate.postForEntity(getUrl(), body, clazz, new HashMap<>()).getBody());
    }

    public void httpDelete(final RestTemplate restTemplate) throws RestServiceException {
        executeHttpCall((HttpCall<Void>) () -> {
            restTemplate.delete(getUrl());
            return null;
        });
    }

    public T getResource() throws LinkNotResolvedException {
        if (resource == null)
            throw new LinkNotResolvedException();
        return resource;
    }

    public boolean isResolved() {
        return resource != null;
    }

    public void setQueryParams(Map<String, String> queryParams) {
        this.queryParams = queryParams;
    }

    public Map<String, String> getQueryParams() {
        return queryParams;
    }

    public void setClass(Class<T> clazz) {
        this.clazz = clazz;
    }

    private <RT, C extends HttpCall<RT>> RT executeHttpCall(C call) throws RestServiceException {
        try {
            return call.httpCall();
        } catch (Exception e) {
            if (e.getMessage() != null)
                System.out.println(e.getMessage());
            throw RestServiceException.getInstance(e);
        }
    }

    interface HttpCall<RT> {
        RT httpCall() throws Exception;
    }

}
