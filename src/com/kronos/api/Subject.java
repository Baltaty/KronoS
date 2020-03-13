package com.kronos.api;

public interface Subject {

    public void attach(Observer observer);

    public void detach(Observer observer);
}
