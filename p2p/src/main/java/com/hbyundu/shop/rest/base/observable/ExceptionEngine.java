package com.hbyundu.shop.rest.base.observable;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.hbyundu.shop.rest.base.exception.ApiException;
import com.hbyundu.shop.rest.base.model.RestErrorModel;

import org.json.JSONException;

import java.io.IOException;
import java.net.ConnectException;
import java.text.ParseException;

import okhttp3.ResponseBody;
import retrofit2.adapter.rxjava.HttpException;

/**
 * Created by apple on 16/12/7.
 */
public class ExceptionEngine {

    //对应HTTP的状态码
    private static final int UNAUTHORIZED = 401;
    private static final int FORBIDDEN = 403;
    private static final int NOT_FOUND = 404;
    private static final int REQUEST_TIMEOUT = 408;
    private static final int INTERNAL_SERVER_ERROR = 500;
    private static final int BAD_GATEWAY = 502;
    private static final int SERVICE_UNAVAILABLE = 503;
    private static final int GATEWAY_TIMEOUT = 504;
    private static final int API_FAILURE = 422;

    public static ApiException handleException(Throwable e) {
        ApiException ex = null;
        String errorMessage = null;
        if (e instanceof HttpException) {
            HttpException httpException = (HttpException) e;
            switch (httpException.code()) {
                case UNAUTHORIZED:
                case FORBIDDEN:
                case NOT_FOUND:
                case REQUEST_TIMEOUT:
                case GATEWAY_TIMEOUT:
                case INTERNAL_SERVER_ERROR:
                case BAD_GATEWAY:
                case SERVICE_UNAVAILABLE:
                    errorMessage = "网络错误";
                    break;
                case API_FAILURE:
                    ResponseBody body = ((HttpException) e).response().errorBody();
                    try {
                        errorMessage = parseAPIErrorBody(body.string());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                    break;
            }
        } else if (e instanceof JsonParseException
                || e instanceof JSONException
                || e instanceof ParseException) {
            errorMessage = "解析错误";
        } else if (e instanceof ConnectException) {
            errorMessage = "连接失败";
        } else {
            errorMessage = "未知错误";
        }

        return new ApiException(e, errorMessage);
    }

    private static String parseAPIErrorBody(String json) throws IOException {
        Gson gson = new Gson();
        RestErrorModel error = gson.fromJson(json, RestErrorModel.class);
        if (error == null) {
            throw new IOException();
        }
        return error.error;
    }
}
