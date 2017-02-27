package com.nulldreams.adapter.throwable;

/**
 * Created by gaoyunfei on 2017/2/27.
 */

public class ConstructorException extends RuntimeException {
    public ConstructorException () {
        super("you must provider a non arguments public constructor method");
    }
}
