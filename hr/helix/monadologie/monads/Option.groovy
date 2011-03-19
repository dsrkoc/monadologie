package hr.helix.monadologie.monads

abstract class Option<A> implements Monad<Option<A>> {
    abstract A get()

    static <T> Option<T> some(final T a) { new Some<T>(a) }
    static <T> Option<T> none() { new None<T>() }

    Boolean isSome() { this in Some }
    Boolean isNone() { this in None }

    A orSome(final A a) { isSome() ? get() : a }

    Option<A> orElse(final Option<A> o) { isSome() ? this : o }
    
    private static class Some<A> extends Option<A> {
        private final A value

        Some(final A val) { value = val }

        A get() { value }

        @Override String toString() { "Some($value)" }
    }

    private static class None<A> extends Option<A> {
        A get() { throw new RuntimeException('Cannot resolve value on None') }

        @Override String toString() { 'None' }
    }

    // --- Monad interface implementation ---

    Option unit(A a) { some(a) }

    Option bind(Closure f) {
        someOrNone { f(get()) }
    }

    Option filter(Closure f) {
        someOrNone { f(get()) ? this : none() }
    }

    private Option someOrNone(Closure someVal) {
        this in None ? this : someVal()
    }
}

