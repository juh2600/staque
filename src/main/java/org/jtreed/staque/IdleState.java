package org.jtreed.staque;

/**
 * The IdleState is always started, never ended, and never aborted. It may be
 * paused, but this does nothing other than set the "pause" flag in the object.
 * All user-configurable options are set to no-ops.
 */
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
