package org.jtreed.staque;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

/*
 * NOTE use underscore-prefixed methods of State in this library's code, so
 * that we can ensure State state gets handled without the user having to call
 * super-methods.
 */
public class StateQueue {
    protected final List<State> queue = new ArrayList<>();
    protected static final State idle = new IdleState();

    /**
     * @return whether there is an explicit state in the queue
     */
    public boolean hasStateAvailable() {
        return queue.size() > 0;
    }

    /**
     * Return the current state in the queue. If the queue is empty, return an
     * IdleState, which is quite immortal. Always use this method unless fiddling
     * with an empty queue is the point of the calling code.
     * 
     * @return current state or IdleState
     */
    public State getCurrentState() {
        if (hasStateAvailable())
            return queue.get(0);
        return idle;
    }

    /**
     * Append a new state to the end of the queue.
     * 
     * @param s state to enqueue
     * @return this, for chaining
     */
    public StateQueue append(State s) {
        queue.add(s);
        return this;
    }

    /**
     * Append a new state to the end of the queue.
     * 
     * @param s_supplier function that returns a state to enqueue
     * @return
     */
    public StateQueue append(Supplier<State> s_supplier) {
        queue.add(s_supplier.get());
        return this;
    }

    /**
     * Remove the current state from the queue. This function does not attempt to
     * terminate the state.
     * 
     * @return this, for chaining
     */
    protected StateQueue pop() {
        if (hasStateAvailable())
            queue.remove(0);
        return this;
    }

    /**
     * Abort the current action if it is active, and clear the queue.
     * 
     * @return this, for chaining
     */
    public StateQueue abortAll() {
        abortOne(); // cauterize the queue before emptying it
        queue.removeIf(anything -> true);
        return this;
    }

    /**
     * Remove the current state from the queue. If the current state is active,
     * abort it first.
     * 
     * @return this, for chaining
     */
    public StateQueue abortOne() {
        if (getCurrentState().isActive())
            getCurrentState()._abort();
        pop();
        return this;
    }

    /**
     * If the current state has not been started, then start it. Otherwise, do
     * nothing.
     * 
     * @return this, for chaining
     */
    public StateQueue startState() {
        if (!getCurrentState().hasStarted())
            getCurrentState()._enter();
        return this;
    }

    /**
     * If the current state is active, end it and remove it from the queue.
     * Otherwise, do nothing.
     * 
     * @return this, for chaining
     */
    public StateQueue endState() {
        if (getCurrentState().isActive()) {
            getCurrentState()._exit();
            pop();
        }
        return this;
    }

    /**
     * If the current state is running, end it and start the next one; otherwise,
     * start the current state.
     * 
     * @return this, for chaining
     */
    public StateQueue next() {
        if (getCurrentState().isActive())
            endState();
        startState();
        return this;
    }

    /**
     * If the current state would like to end, end it and move on to the next state.
     * Then, run one maintenance call for the current state.
     * 
     * @return this, for chaining
     */
    public StateQueue onLoop() {
        if (getCurrentState()._exitCondition())
            next();
        getCurrentState()._maintain();
        return this;
    }
}
