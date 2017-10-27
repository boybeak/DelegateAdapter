package com.github.boybeak.adapter.extension;

/**
 * Created by gaoyunfei on 2017/8/26.
 */

public interface Controller {
    void check (Checkable checkable);
    void start ();
    void stop ();

    boolean isStarted ();
}