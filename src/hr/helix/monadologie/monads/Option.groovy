package hr.helix.monadologie.monads

abstract class Option<A> implements Monad<Option<A>> {

    abstract A get()

    static <T> Option<T> some(final T a) { new Some<T>(a) }

    static <T> Option<T> none() { new None<T>() }

    abstract Boolean isSome()

    abstract Boolean isNone()

    A orSome(final A a) { isSome() ? get() : a }

    Option<A> orElse(final Option<A> o) { isSome() ? this : o }

    private static final class Some<A> extends Option<A> {
        @Delegate
        private List wrapper
        private final A value

        private Some(final A val) {
            value = val
            wrapper = [val]
        }

        @Override
        Boolean isSome() { return true }

        @Override
        Boolean isNone() { return false }

        A get() { value }

        @Override
        String toString() { "Some($value)" }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            Some some = (Some) o;

            if (value != some.value) return false

            return true
        }

        int hashCode() {
            return (value != null ? value.hashCode() : 0)
        }
    }

    private static final class None<A> extends Option<A> {
        @Delegate
        private List wrapper

        private None() { wrapper = [] }

        A get() { throw new RuntimeException('Cannot resolve value on None') }

        @Override
        Boolean isSome() { false }

        @Override
        Boolean isNone() { true }

        @Override
        String toString() { 'None' }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false
            return true
        }

        int hashCode() {
            return super.hashCode()
        }
    }

    // --- Monad interface implementation ---

    Option unit(Object a) { some(a) }

    Option bind(Closure f) {
        someOrNone { f(get()) }
    }

    Option filter(Closure f) {
        someOrNone { f(get()) ? this : none() }
    }

    protected Option someOrNone(Closure someVal) {
        isNone() ? this : someVal()
    }
}
