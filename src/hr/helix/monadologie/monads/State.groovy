package hr.helix.monadologie.monads

/**
 * The State monad.
 *
 * @author Dinko Srkoč
 * @since 2011-04-07
 */
class State<S, A> implements Monad<State> {

    A value(S s) { this.call(s)[1] }
    S state(S s) { this.call(s)[0] }

    def call = { throw new NoSuchMethodException("Call is not yet assigned") }

    // ----- static initializers -----

    /* (f: S => State(S, A)) */
    /**
     * Returns a new State object that executes the provided function.
     * The function receives the state value and should return a pair (a list of
     * two elements): <code> [new state, value] </code>. <p>
     *
     * Example:
     * <pre>
     * // func receives stateful object and returns a pair
     * // where the first element is new stateful object
     * // and the second element is value
     * def stateFn = { s -> [s + 1, 10] }
     *
     * def monad = State.state(stateFn)
     *
     * assert monad.state([1, 2]) == [1, 2, 1]
     * assert monad.value([1, 2]) == 10
     * </pre>
     *
     * @param f state changing function
     * @return new State instance
     */
    static <S, A> State<S, A> state(Closure f) {
        f.resolveStrategy = Closure.DELEGATE_ONLY
        (State)(f.delegate = new State<S, A>(call: f)) // unnecessary casting to make IDEA happy
    }

    static State init() { state({ s -> [s, s] }) }

    /** (f: S => S) */
    static State modify(Closure f) { init().bind({ s -> state({ x -> [f(s), null] }) }) }

    // ----- monad implementation -----

    @Override
    State unit(Object a) {
        state({ s -> [s, a] })
    }

    /** (f: A => State[S, B]) */
    @Override
    State bind(Closure f) {
        mndState { stt, val -> f(val)(stt) }
    }

    State fmap(Closure f) {
        mndState { stt, val ->  [stt, f(val)] }
    }

    private mndState(Closure returnAction) {
        state { s -> returnAction(this.call(s)) }
    }
}
