package hr.helix.monadologie.monads

/**
 * Interface needed for <code>guard</code> function.
 * <code>MonadPlus</code> may implement monad classes that are are
 * also monoids.
 *
 * @since 2011-06-30
 * @author Dinko Srkoč
 */
public interface MonadPlus<M> extends Monad<M> {
    // `m` is prepended to the names `zero` and `plus` because
    // method plus would change the `+` operation of given type M

    /**
     * <tt>mzero</tt> provides neutral element for the operation.
     * It is the equivalent of monoid's <tt>identity</tt>. <p>
     *
     * For example, neutral element for lists is an empty list.
     *
     * @return neutral element (identity)
     */
    MonadPlus<M> getMzero()

    /**
     * Joins this and other monadplus.
     * It is equivalent of monoid's <tt>append</tt>. <p>
     *
     * Taking list as an example, that would mean concatenation.
     *
     * @param other the other object to join with this.
     * @return new monadplus that is the result of this + other
     */
    MonadPlus<M> mplus(MonadPlus<M> other)

}
