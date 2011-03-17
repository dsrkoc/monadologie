package hr.helix.monadologie.monads

abstract class Option<A> {
    abstract A get()

    private static final NONE = new None<A>()

    static Option Some(A a) { new Some<A>(a) }
    static Option None = NONE
}

