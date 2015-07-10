package com.eliasbagley.take;

/**
 * Created by eliasbagley on 7/10/15.
 */
public interface MediaPickerListener<T> {
    void success(T t);
    void failure(String reason);
}
