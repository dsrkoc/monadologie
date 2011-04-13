package hr.helix.monadologie.monads

/**
 * A monoid abstraction. Implementations must follow the monoid laws:
 * <ol>
 *     <li> left identity:  <p>
 *     <code> identity ∙ x ≡ x  </code>
 *
 *     <li> right identity: <p>
 *     <code> x ∙ identity ≡ x  </code>
 *
 *     <li> associativity:  <p>
 *     <code> (x ∙ y) ∙ z ≡ x ∙ (y ∙ z) </code>
 * </ol>
 * where ∙ is the <code> append </code> operator. <p>
 *
 * <em> From wikipedia: </em> Monoid is an algebraic structure with a single
 * associative binary operation and an identity element.
 *
 * @author Dinko Srkoč
 * @since 2011-03-31
 */
public interface Monoid<A> {

    /**
     * Monoid identity (neutral element).
     * In Haskell parlance this is <em> mempty </em>. <p>
     *
     * E.g. for list operations, this is an empty list, for integers with
     * addition this would be 0, for multiplication 1, etc.
     *
     * @return Neutral element for this Monoid.
     */

    Monoid<A> getIdentity()

    /**
     * Appends (joins, sums) this and other Monoid.
     * Haskell has this under the name <em> mappend </em>.
     *
     * @param other the other value to join/append
     * @return new value that is the sum of this and other
     */
    Monoid<A> append(Monoid<A> other)

    /**
     * Returns the underlying monoid value. <p>
     *
     * <em> Note: </em> this method is not a part of what makes
     * a monoid (<em> identity </em>, <em> append </em>), just a convenient
     * way to standardize collecting the underlying value from the monoid.
     * 
     * @return The value that is stored inside the monoid
     */
    A get()
}
