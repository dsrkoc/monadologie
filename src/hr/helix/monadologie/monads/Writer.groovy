package hr.helix.monadologie.monads

import static hr.helix.monadologie.monads.Monoids.asMonoid

/**
 * The Writer monad. <p>
 *
 * From wikipedia: The writer monad allows a program to compute various kinds
 * of auxiliary output which can be "composed" or "accumulated" step-by-step,
 * in addition to the main result of a computation. <p>
 *
 * Writer monad is typically used for logging but may be used for any purpose
 * where accumulation of result is needed.
 * 
 * @author Dinko Srkoč
 * @since 2011-04-10
 */
class Writer<A, V> implements Monad<Writer> {

    private Monoid<A> cumulative
    private V value

    private Writer(V value, Monoid<A> aggregate) {
        this.value = value
        this.cumulative = aggregate
    }

    /* f mora vratiti Monoid */
//    static Writer write(Closure f, A value) { write(f(value) as Monoid, value) }

    static Writer<A, V> write(V value, A aggr) { new Writer(value, asMonoid(aggr)) }

    V getValue() { value }
    A getAggregate() { cumulative.get() }

    List get() { [getValue(), getAggregate()] }

    Monoid<A> tell(A a) { cumulative = cumulative.append(asMonoid(a)) }

    // ----------------------------

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Writer writer = (Writer) o

        if (cumulative != writer.cumulative) return false
        if (value != writer.value) return false

        true
    }

    int hashCode() {
        int result = (cumulative != null ? cumulative.hashCode() : 0)
        31 * result + (value != null ? value.hashCode() : 0)
    }

    String toString() { "Writer($value, $cumulative)" }

    // ----- monad implementation -----

    @Override
    Writer unit(Object a) {
        new Writer(a, cumulative.identity)
    }

    @Override
    Writer bind(Closure f) {
        Writer<A, V> w = f(value)
        write(w.value, cumulative.append(w.cumulative))
    }
}
