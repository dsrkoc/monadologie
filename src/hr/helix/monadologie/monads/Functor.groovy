package hr.helix.monadologie.monads

/**
 * Functors are objects that can be mapped over. <p>
 *
 * All functor instances must satisfy the following two laws:
 * <ol>
 *     <li> identity:    <p>
 *     <code> ∀ a. a ≡ a fmap(identity)                  </code>
 *
 *     <li> composition: <p>
 *     <code> ∀ a f g. a fmap(f ∘ g) ≡ a fmap(g) fmap(f) </code>
 * </ol>
 *
 * If we imagine that <tt> List </tt> is a functor than its method
 * <code> collect </code> may assume the role of <code> fmap </code>.
 * 
 * @author Dinko Srkoč
 * @since 2011-04-14
 * @param < A > The functor type
 */
public interface Functor<F> {

    /**
     * The method transforms value(s) inside an environment and returns
     * the transformed value(s) in a new environment.
     * Function <code> f </code> performs the transformation. <p>
     *
     * The type signature of <code> fmap </code> is: <br>
     * <code> fmap(f: A => B): F[B] </code> where <code> fmap </code> is a
     * member of <code> F[A] </code>. <p>
     *
     * @param f the function that converts elements of type <tt> A </tt> into
     *        elements of type <tt> B </tt>
     * @return the new functor of type B
     */
    F fmap(Closure f)

}
