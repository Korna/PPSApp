package com.coma.go.Model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.coma.go.Utils.Logger;

import java.io.IOException;


import retrofit2.Response;

import static com.coma.go.Model.DataResponse.Status.ERROR;
import static com.coma.go.Model.DataResponse.Status.LOADING;
import static com.coma.go.Model.DataResponse.Status.READY;
import static com.coma.go.Model.DataResponse.Status.SUCCESS;


/**
 * Created by Koma on 23.03.2018.
 */


public class DataResponse<T> {

    private Status status = READY;
    private T body = null;
    private Throwable exception = new Throwable("Unknown error");
    private int code = 200;


    public DataResponse(Response<T> response) {
        code = response.code();

        if(response.isSuccessful()) {
            body = response.body();
            exception = null;
            status = SUCCESS;
        } else {
            status = ERROR;
            String message = null;
            if (response.errorBody() != null) {
                try {
                    message = response.errorBody().string();
                } catch (IOException ignored) {
                    Logger.e("ERROR", ignored);
                }catch (NullPointerException npe){
                    Logger.e("ERROR", npe);
                }
            }
            if (message == null || message.trim().length() == 0) {
                message = response.message();
            }
            exception = new IOException(message);
            body = null;
        }
    }


    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public T getData() {
        return body;
    }

    public void setData(T responseBody) {
        this.body = responseBody;
    }


    public DataResponse(Status status, T body) {
        this.status = status;
        this.body = body;
    }

    public DataResponse(Throwable exception) {
        this.status = ERROR;
        this.exception = new Exception(exception);
    }

    public DataResponse(Status status, T body, Throwable exception) {
        this.status = status;
        this.body = body;
        this.exception = exception;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DataResponse<?> that = (DataResponse<?>) o;

       // if (ERROR_CODE != that.ERROR_CODE) return false;
        if (status != that.status) return false;
        return body != null ? body.equals(that.body) : that.body == null;
    }

    @Override
    public int hashCode() {
        int result = status != null ? status.hashCode() : 0;
        result = 31 * result + (body != null ? body.hashCode() : 0);
     //   result = 31 * result + ERROR_CODE;
        return result;
    }

    public Throwable getException() {
        return exception;
    }

    public String getThrowableMessage() {
        if(exception == null)
            return "Unknown error";
        else
            return exception.getMessage();
    }

    public void setException(Exception throwable) {
        this.exception = throwable;
    }

    public static <T> DataResponse<T> success(@NonNull T data) {
        return new DataResponse<>(SUCCESS, data, null);
    }

    public static <T> DataResponse<T> error(Throwable exception, @Nullable T data) {
        return new DataResponse<>(ERROR, data, exception);
    }

    public static <T> DataResponse<T> loading(@Nullable T data) {
        return new DataResponse<>(LOADING, data, null);
    }
    //web api response
    public boolean responseIsSuccessful() {
        return code >= 200 && code < 300;
    }

    //data response at all
    public boolean isSuccessful() {
        return status == SUCCESS;
    }

    public enum Status {
        SUCCESS,
        ERROR,
        LOADING,
        READY
    }

}