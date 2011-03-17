package hr.helix.monadologie.monads

abstract class OptionMonad<A> extends Option<A> implements Monad<Option<A>> {

    Option unit(A a) { Option.Some(a) }

    Option bind(Closure f) {
        someOrNone { f(get()) }
    }

    Option filter(Closure f) {
        someOrNone { f(get()) ? this : Option.None }
    }

    private Option someOrNone(Closure someVal) {
        this in None ? this : someVal()
    }
}
