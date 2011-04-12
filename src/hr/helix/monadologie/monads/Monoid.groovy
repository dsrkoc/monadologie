package hr.helix.monadologie.monads

/**
 * A monoid abstraction. Implementations must follow the monoid laws:
 * <ul>
 *     <li> left identity:  <p>
 *     <code> identity ∘ x ≡ x  </code>
 *
 *     <li> right identity: <p>
 *     <code> x ∘ identity ≡ x  </code>
 *
 *     <li> associativity:  <p>
 *     <code> (x ∘ y) ∘ z ≡ x ∘ (y ∘ z) </code>
 * </ul>
 * where ∘ is the <code> append </code> operator.
 *
 * From wikipedia: Monoid is an algebraic structure with a single associative
 * binary operation and an identity element.
 *
 * @author Dinko Srkoč
 * @since 2011-03-31
 */
public interface Monoid<A> {

    /**
     * Monoid identity.
     * In Haskell parlance this is <em> mempty </em>. <p>
     *
     * E.g. for list operations, this is an empty list, for integers with
     * addition this would be 0, for multiplication 1.
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

    A get()
}
