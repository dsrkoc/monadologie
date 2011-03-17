package hr.helix.monadologie.monads

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
