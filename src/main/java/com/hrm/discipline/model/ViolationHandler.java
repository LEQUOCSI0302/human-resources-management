package com.hrm.discipline.model;

public abstract class ViolationHandler {
    protected ViolationHandler nextHandler;

    public void setNext(ViolationHandler handler) {
        this.nextHandler = handler;
    }

    public abstract void handleViolation(Violation v);
}
