package hr.helix.monadologie

import spock.lang.Specification
import static hr.helix.monadologie.MonadComprehension.foreach
import static hr.helix.monadologie.monads.Option.some
import static hr.helix.monadologie.monads.Option.none
import static hr.helix.monadologie.monads.Either.left as el
import static hr.helix.monadologie.monads.Either.right as er
import static hr.helix.monadologie.monads.State.state as st

/**
 * Testing foreach comprehensions.
 *
 * @author Dinko Srkoč
 * @since 2011-03-23
 */
class ComprehensionSpec extends Specification {

    def 'testing simple comprehensions'() {
        when:
        def res = foreach {
            a = takeFrom { monads[0] }
            b = takeFrom { monads[1] }
            yield { operation(a, b) }
        }

        then:
        res == expected

        where:
        monads                        | operation                     | expected
        [[1, 2], [3, 4]]              | { a, b -> a + b }             | [4, 5, 5, 6]
        [[1, 2], [3, 4]]              | { a, b -> a * b }             | [3, 4, 6, 8]
        [[2], [3, 4, 5]]              | { a, b -> a + b }             | [5, 6, 7]
        [1..3, 1..3]                  | { a, b -> a * b }             | [1, 2, 3, 2, 4, 6, 3, 6, 9]
        [[a:1,b:2],[c:3]]             | { a, b -> a.value + b.value } | [4, 5]
        [[1, 2], [c:4]]               | { a, b -> a + b.value }       | [5, 6]
        [some(1), some(2)]            | { a, b -> a + b }             | some(3)
        [some(3), none() ]            | { a, b -> a + b }             | none()
        [some(3),none(),some(4)]      | { a, b -> a + b }             | none()
        [er(1).right(), er(2).right()]| { a, b -> a + b }             | er(3)
        [el(1).left(), el(2).left()]  | { a, b -> a + b }             | el(3)
        [er(1).right(), el(2).left()] | { a, b -> a + b }             | el(3)
    }

    def 'test comprehensions with different monads'() {
        when:
        def res = foreach {
            a = takeFrom { monads[0] }
            b = takeFrom { monads[1] }
            yield { operation(a, b) }
        }

        then:
        res == expected

        where:
        monads                  | operation               | expected
        [[1, 2], some(3)]       | { a, b -> a + b }       | [4, 5]
        [[1, 2], none()]        | { a, b -> a + b }       | []
        [[a:1, b:2], some(3)]   | { a, b -> a.value }     | [1, 2]
        [[a:1, b:2], [2, 3]]    | { a, b -> a.value + b.value } | [3, 4, 4, 5]
        [[2, 3], [a:1, b:2]]    | { a, b -> a.value + b.value } | [3, 4, 4, 5]
    }

    def 'closures (Reader, State) should also be possible to use in comprehensions'() {
        when:
        def rdrRes = foreach {
            a = takeFrom {{ x -> x + 3 }}
            b = takeFrom {{ x -> x * 2 }}
            yield { a + b }
        }

        def sttRes = foreach {
            a = takeFrom { st({ s -> [s + 10, 10]}) }
            b = takeFrom { st({ s -> [s.tail(),  5]}) }
            yield { a + b }
        }

        then:
        rdrRes(1) == 6
        rdrRes(5) == 18

        sttRes.state([1, 2]) == [2, 10]
        sttRes.value([1, 2]) == 15
        sttRes.state([5]) == [10]
        sttRes.value([5]) == 15
    }

    def 'optional monad methods should be callable from the foreach closure'() {
        given:
        List.metaClass.appendVal = { v -> delegate << v }
        def origList = [1, 2, 3]
        def origSize = origList.size()
        def val = 100
        
        when:
        def list = foreach {
            a = takeFrom { origList }
            appendVal(val)
            yield { a }
        }

        then:
        list.size() == origSize + 1
        list[-1] == val
    }

    def 'nonexistent optional methods should throw MissingMethodException'() {
        given:
        List.metaClass.callMe = { throw new RuntimeException("I should not have been called!") }

        when:
        foreach {
            l = takeFrom { [aa:2, bb:3] }
            m = takeFrom { [1, 2, 3] }
            callMe() // should fail because Map is the monad du jour
            yield { l + m }
        }

        then:
        thrown(MissingMethodException)
    }
}
