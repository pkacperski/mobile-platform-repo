package com.mobileplatform.frontend.controller.api;

import com.fatboyindustrial.gsonjavatime.Converters;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.mashape.unirest.request.GetRequest;
import com.mashape.unirest.request.HttpRequestWithBody;
import com.mobileplatform.frontend.controller.common.SystemProperties;

import java.util.Map;

public class RestHandler<T> {
    private final Gson gson = Converters.registerLocalDateTime(new GsonBuilder()).create(); // to solve a problem with deserializing java.time.LocalDateTime by gson

    private final Class<T> typeParameterClass;
    private final String apiPath = SystemProperties.properties.getProperty("app.api");

    public RestHandler(Class<T> typeParameterClass) {
        this.typeParameterClass = typeParameterClass;
    }

    public T performGet(String path) throws UnirestException {
        return gson.fromJson(Unirest.get(apiPath.concat(path)).asJson().getBody().toString(), typeParameterClass);
    }

    public T performGet(String path, String pathParam) throws UnirestException {
        return gson.fromJson(Unirest.get(apiPath.concat(path).concat("/").concat(pathParam)).asJson().getBody().toString(), typeParameterClass);
    }

    public T performGet(String path, Map<String, String> requestParams) throws UnirestException {
        GetRequest getRequest = Unirest.get(apiPath.concat(path));
        requestParams.keySet().forEach(e -> getRequest.queryString(e, requestParams.get(e)));
        return gson.fromJson(getRequest.asJson().getBody().toString(), typeParameterClass);
    }

    public T performPost(String path, String body, String contentType) throws UnirestException {
        HttpRequestWithBody httpRequestWithBody = Unirest.post(path.contains("http") ? path : apiPath.concat(path));
        httpRequestWithBody.header("Content-Type", contentType);
        httpRequestWithBody.body(body);
        return gson.fromJson(httpRequestWithBody.asJson().getBody().toString(), typeParameterClass);
    }
}
