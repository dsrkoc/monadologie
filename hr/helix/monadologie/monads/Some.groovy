package hr.helix.monadologie.monads

class Some<A> extends OptionMonad<A> {
    private final A value

    Some(A val) { value = val }

    A get() { value }

    @Override String toString() { "Some($value)" }
}
