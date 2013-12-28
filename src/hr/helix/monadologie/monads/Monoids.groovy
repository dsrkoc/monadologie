package hr.helix.monadologie.monads

/**
 * Converts standard data types such as Collection to Monoids.
 *
 * @author Dinko Srkoč
 * @since 2011-04-12
 */
class Monoids {

    private static abstract class BaseMonoid<A> implements Monoid {
        protected final A val

        BaseMonoid(A val) { this.val = val }

        @Override Monoid<A> append(Monoid other) { asMonoid(val + other.get()) }
        @Override A get() { val }

        String toString() { "Monoid($val)" }

        boolean equals(o) {
            if (this.is(o)) return true
            if (getClass() != o.class) return false

            BaseMonoid that = (BaseMonoid) o

            if (val != that.val) return false

            return true
        }

        int hashCode() { val.hashCode() }
    }

    private static Monoid standardMonoid(obj) {
        new BaseMonoid(obj) {
            @Override Monoid getIdentity() { asMonoid(obj.getClass().newInstance()) }
        }
    }

    static Monoid<String> asMonoid(String s) {
        new BaseMonoid<String>(s) {
            @Override Monoid<String> getIdentity() { asMonoid('') }
        }
    }

    static Monoid<Number> asMonoidSum(Number n) {
        new BaseMonoid<Number>(n) {
            @Override Monoid<Number> getIdentity() { asMonoid(0) }
            @Override Monoid<Number> append(Monoid other) { asMonoid(n + other.get()) }
        }
    }

    static Monoid<Number> asMonoidProd(Number n) {
        new BaseMonoid<Number>(n) {
            @Override Monoid<Number> getIdentity() { asMonoid(1) }
            @Override Monoid<Number> append(Monoid other) { asMonoid(n * other.get()) }
        }
    }

    static Monoid<Number> asMonoid(Number n) { asMonoidSum(n) } // num sum by default

    static Monoid<Boolean> asMonoidAny(Boolean b) {
        new BaseMonoid<Boolean>(b) {
            @Override Monoid<Boolean> getIdentity() { asMonoid(false) }
            @Override Monoid<Boolean> append(Monoid other) { asMonoid(b || other.get()) }
        }
    }

    static Monoid<Boolean> asMonoidAll(Boolean b) {
        new BaseMonoid<Boolean>(b) {
            @Override Monoid<Boolean> getIdentity() { asMonoid(true) }
            @Override Monoid<Boolean> append(Monoid other) { asMonoid(b && other.get()) }
        }
    }

    static Monoid<Boolean> asMonoid(Boolean b) { asMonoidAny(b) } // bool any as default

    /**
     * Converts an Option to monoid. <p>
     *
     * Identity is <em> None </em> and append resolves according to the
     * following rules:
     * <ul>
     *     <li> if this object is None - result is other object
     *     <li> if other object is None - result is this object
     *     <li> else result is the value of this <code> :append: </code> the
     *          value of other
     * </ul>
     *
     * @param o an Option
     * @return option monoid
     */
    static Monoid<Option> asMonoid(Option o) {
        Monoid<Option> self
        //noinspection GroovyVariableNotAssigned
        self=new BaseMonoid<Option>(o) {
            @Override Monoid<Option> getIdentity() { asMonoid((Option)Option.none()) }

            @Override Monoid<Option> append(Monoid other) {
                if (o.isNone())
                    other
                else if (other.get().isNone())
                    self
                else
                    asMonoid((Option)Option.some(
                            asMonoid(o.get()).append(asMonoid(other.get().get())).get()))
            }
        }
    }

    /**
     * Converts a collection to a collection monoid. <p>
     *
     * Identity is empty list and append is plus.
     *
     * @param c a collection
     * @return collection monoid
     */
    // since Option has @Delegate List it can end up here, so we must nudge it towards asMonoid(Option)
    static Monoid<Collection> asMonoid(Collection c) { c in Option ? asMonoid((Option)c) : standardMonoid(c) }

    /**
     * Converts a map to a map monoid. <p>
     *
     * Identity is empty map and append is plus.
     *
     * @param m a map
     * @return map monoid
     */
    static Monoid<Map> asMonoid(Map m) { standardMonoid(m) }

    static Monoid asMonoid(Monoid m) { m }

    /**
     * Throws <code> IllegalArgumentException </code> because there is no
     * support for converting this particular type to monoid.
     *
     * @param obj an object
     * @return it doesn't return but rather throws exception
     * @throw new IllegalArgumentException because <code> obj </code> argument is not
     *        supported as a monoid
     */
    static Monoid asMonoid(obj) {
        throw new IllegalArgumentException("Unsupported monoid type: ${obj.getClass().name} ($obj)")
    }
}
