package hr.helix.monadologie.monads

abstract class Option<A> implements MonadPlus<Option<A>> {

    abstract A get()
    abstract boolean isEmpty()

    static <T> Option<T> some(final T a) { new Some<T>(a) }
    static <T> Option<T> none() { _none }

    private static final Option _none = new None()

    Boolean isSome() { !isEmpty() }
    Boolean isNone() {  isEmpty() }

    A orSome(final A a) { isEmpty() ? a : get() }

    /**
     * Return this Option if it is nonempty, otherwise return the result of
     * evaluating alternative.
     *
     * @param alternative the alternative expression
     * @return this or alternative Option
     */
    public <B> Option<A> orElse(final Option<B> alternative) { isEmpty() ? alternative : this }

    private static final class Some<A> extends Option<A> {
        @Delegate private List wrapper
        private final A value

        private Some(final A val) {
            value = val
            wrapper = [val]
        }

        A get() { value }
        @Override boolean isEmpty() { false }

        @Override String toString() { "Some($value)" }

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
        @Delegate private List wrapper

        private None() { wrapper = [] }

        A get() { throw new RuntimeException('Cannot resolve value on None') }
        @Override boolean isEmpty() { true }

        @Override String toString() { 'None' }

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

    @Override Option unit(Object a) { some(a) }

    @Override Option bind(Closure f) {
        someOrNone { f(get()) }
    }

    Option filter(Closure f) {
        someOrNone { f(get()) ? this : none() }
    }

    // --- MonadPlus interface implementation ---

    @Override Option mzero() { none() }

    @Override Option mplus(Option other) { orElse(other) }


    private Option someOrNone(Closure someVal) {
        this.isEmpty() ? this : someVal()
    }
}
