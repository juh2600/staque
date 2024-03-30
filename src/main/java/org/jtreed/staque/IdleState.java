package org.jtreed.staque;

public class IdleState extends State {

    @Override
    public void enter() {
    }

    @Override
    public void maintain() {
    }

    @Override
    public void abort() {
    }

    @Override
    public void exit() {
    }

    @Override
    public boolean exitCondition() {
        return false;
    }

    @Override
    public boolean hasStarted() {
        return true;
    }

    @Override
    public boolean hasEnded() {
        return false;
    }

    @Override
    public boolean hasAborted() {
        return false;
    }
}
