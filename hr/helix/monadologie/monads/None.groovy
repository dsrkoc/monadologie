package hr.helix.monadologie.monads

class None<A> extends OptionMonad<A> {
    A get() { throw new RuntimeException('Cannot resolve value on None') }

    @Override String toString() { 'None' }
}
