package hr.helix.monadologie.monads

import spock.lang.Specification

class EitherSpec extends Specification {

    Either left
    Either right

    def setup() {
        left = Either.left(1)
        right = Either.right(2)
    }

    def 'left should be left and right should be right'() {
        expect:
        left.isLeft()
        !left.isRight()

        right.isRight()
        !right.isLeft()
    }

    def '`either` should apply either left or right function, depending on Either type'() {
        expect:
        e.either(fnL, fnR) == res
        
        where:
        e               | fnL         | fnR         | res
        Either.left(1)  | { it + 1 }  | { it + 10 } | 2
        Either.right(2) | { it + 10 } | { it + 1 }  | 3
    }

    def '`reduce` should return either left or right value, depending on Either type'() {
        expect:
        Either.reduce(e) == res
        
        where:
        e               | res
        Either.left(1)  | 1
        Either.right(2) | 2
    }

    def '`lefts` and `rights` should extract left and right values respectively from the list of Eithers'() {
        given:
        List<Either> eithers = [ Either.left(1), Either.left(2), Either.right(0), Either.left(3), Either.right(2) ]

        expect:
        Either.lefts(eithers).sum()  == 6
        Either.rights(eithers).sum() == 2
    }

    def 'monad functions should behave as expected'() {
        expect:
        left.left().unit(4)   == Either.left(4)
        left.right().unit(5)  == Either.right(5)
        right.left().unit(6)  == Either.left(6)
        right.right().unit(7) == Either.right(7)

        left.left().bind { Either.left(it + 1) }  == Either.left(2)
        left.right().bind { Either.left(it + 1) } == left
        right.left().bind { Either.left(it + 2) } == right
        right.right().bind { Either.right(it + 3) } == Either.right(5)
    }
}
