package hr.helix.monadologie.monads

/**
 * Implementors of this interface may be considered monads.
 * However, they must satisfy monad laws:
 * 
 * 1) identity:
 *    m bind unit ≡ m
 *
 * 2) unit:
 *    unit(x) bind f ≡ f(x)
 *
 * 3) composition:
 *    m bind g bind f ≡ m bind (g(x) bind f)
 */
interface Monad<M> {       // ... zapravo, Monad[M[A]]

    /* param   value of type A
     * returns container of type A
     *
     * unit :: a -> M a */
    M unit(Object a)       // ... unit(A): M[A]

    /* param   m ... container of type A
     * param   f ... transformer function (A => M[B])
     * returns container of type B
     *
     * bind :: M a -> (a -> M b) -> M b */
    /*M bind(M m, Closure f) // ... bind[B](M[A], A => M[B]): M[B]*/
    M bind(Closure f) // ... bind[B](M[A], A => M[B]): M[B]
}
