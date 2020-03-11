package com.kronos.model;

public class GenericParser<T> {

    private T objectToGenerify;

    public GenericParser(T objectToGenerify) {
        this.objectToGenerify = objectToGenerify;
    }

    public T getObjectToGenerify() {
        return objectToGenerify;
    }

    public void setObjectToGenerify(T objectToGenerify) {
        this.objectToGenerify = objectToGenerify;
    }

    public Class getObjectClass() {
        return objectToGenerify.getClass();
    }
}
