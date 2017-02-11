package com.meetup.utils;

import com.meetup.utils.base.IHttpRequester;
import com.meetup.utils.base.IHttpResponse;
import com.meetup.utils.base.IHttpResponseFactory;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import okhttp3.*;

import javax.inject.Inject;
import java.io.IOException;
import java.util.Map;
import java.util.concurrent.Callable;

public class OkHttpRequester implements IHttpRequester {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private final IHttpResponseFactory responseFactory;

    private final OkHttpClient httpClient;

    @Inject
    public OkHttpRequester(IHttpResponseFactory responseFactory) {
        this.responseFactory = responseFactory;
        this.httpClient = new OkHttpClient();
    }

    public Observable<IHttpResponse> get(final String url) {
        return Observable.defer(new Callable<ObservableSource<? extends IHttpResponse>>() {
            @Override
            public ObservableSource<? extends IHttpResponse> call() throws Exception {
                Request request = new Request.Builder()
                        .url(url)
                        .build();

                return createResponse(request);
            }
        });
    }

    public Observable<IHttpResponse> get(final String url, final Map<String, String> headers) {
        return Observable.defer(new Callable<ObservableSource<? extends IHttpResponse>>() {
            @Override
            public ObservableSource<? extends IHttpResponse> call() throws Exception {
                Request.Builder requestBuilder = new Request.Builder()
                        .url(url);

                for (Map.Entry<String, String> pair : headers.entrySet()) {
                    requestBuilder.addHeader(pair.getKey(), pair.getValue());
                }

                Request request = requestBuilder.build();
                return createResponse(request);
            }
        });
    }

    public Observable<IHttpResponse> post(final String url, final String body) {
        return Observable.defer(new Callable<ObservableSource<? extends IHttpResponse>>() {
            @Override
            public ObservableSource<? extends IHttpResponse> call() throws Exception {
                RequestBody requestBody = RequestBody.create(JSON, body);

                Request request = new Request.Builder()
                        .url(url)
                        .post(requestBody)
                        .build();

                return createResponse(request);
            }
        });
    }

    public Observable<IHttpResponse> post(final String url, final String body, final Map<String, String> headers) {
        return Observable.defer(new Callable<ObservableSource<? extends IHttpResponse>>() {
            @Override
            public ObservableSource<? extends IHttpResponse> call() throws Exception {
                RequestBody requestBody = RequestBody.create(JSON, body);

                Request.Builder requestBuilder = new Request.Builder()
                        .url(url)
                        .post(requestBody);

                for (Map.Entry<String, String> pair : headers.entrySet()) {
                    requestBuilder.addHeader(pair.getKey(), pair.getValue());
                }

                Request request = requestBuilder.build();
                return createResponse(request);
            }
        });
    }

    public Observable<IHttpResponse> put(final String url, final String body) {
        return Observable.defer(new Callable<ObservableSource<? extends IHttpResponse>>() {
            @Override
            public ObservableSource<? extends IHttpResponse> call() throws Exception {
                RequestBody requestBody = RequestBody.create(JSON, body);

                Request request = new Request.Builder()
                        .url(url)
                        .put(requestBody)
                        .build();

                return createResponse(request);
            }
        });
    }

    public Observable<IHttpResponse> put(final String url, final String body, final Map<String, String> headers) {
        return Observable.defer(new Callable<ObservableSource<? extends IHttpResponse>>() {
            @Override
            public ObservableSource<? extends IHttpResponse> call() throws Exception {
                RequestBody requestBody = RequestBody.create(JSON, body);

                Request.Builder requestBuilder = new Request.Builder()
                        .url(url)
                        .put(requestBody);

                for (Map.Entry<String, String> pair : headers.entrySet()) {
                    requestBuilder.addHeader(pair.getKey(), pair.getValue());
                }

                Request request = requestBuilder.build();
                return createResponse(request);
            }
        });
    }

    private Observable<IHttpResponse> createResponse(Request request) {
        try {
            Response response = this.httpClient.newCall(request).execute();

            IHttpResponse responseParsed = responseFactory.createResponse(
                    response.headers().toMultimap(), response.body().string(),
                    response.message(), response.code());

            return Observable.just(responseParsed);
        } catch (IOException e) {
            return Observable.error(e);
        }
    }
}
