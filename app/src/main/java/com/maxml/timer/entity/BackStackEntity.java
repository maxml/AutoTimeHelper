package com.maxml.timer.entity;

/**
 * Created by nazar on 19.01.18.
 */

public class BackStackEntity {
    private Action action;
    private boolean isBreak;

    public BackStackEntity(Action action, boolean isBreak) {
        this.action = action;
        this.isBreak = isBreak;
    }

    public String getType(){
        return action.getType();
    }

    public Action getAction() {
        return action;
    }

    public void setAction(Action action) {
        this.action = action;
    }

    public boolean isBreak() {
        return isBreak;
    }

    public void setBreak(boolean aBreak) {
        isBreak = aBreak;
    }
}
