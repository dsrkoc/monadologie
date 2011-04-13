package hr.helix.monadologie.monads

/**
 * @author Dinko Srkoč
 * @since 2011-04-14
 * @param < A > the functor type
 */
public interface Functor<A> {

    A fmap(Closure f)

}
