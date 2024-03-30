package org.jtreed.staque;

/**
 * Represents a state with procedures for entry, exit, maintenance/loop, and
 * abortion.
 */
public abstract class State {
    protected boolean hasStarted = false, hasEnded = false, hasAborted = false, isPaused = false;

    /**
     * Defines the steps to enter the state.
     */
    public abstract void enter();

    /**
     * Defines the steps to take when in this state, as long as the state is not
     * paused.
     */
    public abstract void maintain();

    /**
     * Aborting a state causes an unclean exit. hasEnded() will return true, but
     * typical exit procedures will not run. Safety measures should still be
     * executed when aborting.
     */
    public abstract void abort();

    /**
     * Defines the steps to exit the state normally.
     */
    public abstract void exit();

    /**
     * Defines when the state should be considered "done" and we should move on to
     * the next state.
     */
    public abstract boolean exitCondition();

    /**
     * Pause the state. maintain() will not be called while the state is paused.
     */
    public void pause() {
        isPaused = true;
    }

    /**
     * Unpause the state. maintain() will resume being called.
     */
    public void unpause() {
        isPaused = false;
    }

    /**
     * Toggle whether the state is paused. maintain() will not be called while the
     * state is paused.
     */
    public void togglePause() {
        isPaused = !isPaused();
    }

    public final State _enter() {
        enter();
        hasStarted = true;
        return this;
    };

    public final State _maintain() {
        if (!isPaused())
            maintain();
        return this;
    };

    public final State _abort() {
        abort();
        hasAborted = true;
        hasEnded = true;
        return this;
    }

    public final State _exit() {
        exit();
        hasEnded = true;
        return this;
    };

    public final boolean _exitCondition() {
        return hasEnded() || exitCondition();
    }

    public boolean hasStarted() {
        return hasStarted;
    }

    public boolean hasEnded() {
        return hasEnded;
    }

    public boolean hasAborted() {
        return hasAborted;
    }

    public boolean isActive() {
        return hasStarted() && !hasEnded();
    }

    public boolean isPaused() {
        return isPaused;
    }
}