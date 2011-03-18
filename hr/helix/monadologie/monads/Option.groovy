package hr.helix.monadologie.monads

abstract class Option<A> {
    abstract A get()

    static <T> Option<T> Some(final T a) { new Some<T>(a) }
    static <T> Option<T> None() { new None<T>() }

    Boolean isSome() { this in Some }
    Boolean isNone() { this in None }

    A orSome(final A a) { isSome() ? get() : a }

    Option<A> orElse(final Option<A> o) { isSome() ? this : o }
}

