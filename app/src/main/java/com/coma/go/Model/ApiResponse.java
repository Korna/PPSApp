package com.coma.go.Model;

import android.support.annotation.Nullable;

import com.coma.go.Utils.Logger;

import java.io.IOException;

import retrofit2.Response;


public class ApiResponse<T> {

    public final int code;
    @Nullable
    public final T body;
    @Nullable
    public final Throwable error;


    public ApiResponse(Throwable error) {
        code = 500;
        body = null;
        this.error = error;
    }

    public ApiResponse(Response<T> response) {
        code = response.code();
        if(response.isSuccessful()) {
            body = response.body();
            error = null;
        } else {
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Logger.e("ERROR", "error while parsing response", ignored);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            error = new IOException(message);
            body = null;
        }
    }

    public ApiResponse(final int code, final T body) {
        this.code = code;
        if(code == 200) {
            this.body = body;
            error = null;
        } else {
            String message = "Error code:" + String.valueOf(code);
            error = new IOException(message);
            this.body = null;
        }
    }

    public ApiResponse(final int code, final Throwable throwable, final T body) {
        this.code = code;
        this.error = throwable;
        this.body = body;
    }

    public boolean isSuccessful() {
        return code >= 200 && code < 300;
    }


    public int getCode() {
        return code;
    }

    @Nullable
    public T getBody() {
        return body;
    }

    @Nullable
    public Throwable getError() {
        return error;
    }
}