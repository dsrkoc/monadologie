package hr.helix.monadologie.monads

/**
 * @author Dinko Srkoč
 * @since 2011-03-31
 */
class Reader extends Closure implements Monad<Closure> {
    private Closure func

    private Reader(Closure f) {
        super(null)
        func = f
    }

    Closure unit(Object a) { fn({ x -> a }) }

    Closure bind(Closure f) {{ w -> f(this.call(w))(w) }}

    def call(a) { func(a) }

    static Reader fn(Closure f) { new Reader(f) }
}
