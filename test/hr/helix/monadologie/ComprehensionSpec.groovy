package hr.helix.monadologie

import spock.lang.Specification
import static hr.helix.monadologie.MonadComprehension.foreach
import static hr.helix.monadologie.monads.Option.some
import static hr.helix.monadologie.monads.Option.none

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
        monads                  | operation               | expected
        [[1, 2], [3, 4]]        | { a, b -> a + b }       | [4, 5, 5, 6]
        [[1, 2], [3, 4]]        | { a, b -> a * b }       | [3, 4, 6, 8]
        [[2], [3, 4, 5]]        | { a, b -> a + b }       | [5, 6, 7]
        [1..3, 1..3]            | { a, b -> a * b }       | [1, 2, 3, 2, 4, 6, 3, 6, 9]
//        [[a:1,b:2],[c:3]]     | { a, b -> a.value + b.value } | [] not good
        [[1, 2], [c:4]]         | { a, b -> a + b.value } | [5, 6]
        [some(1), some(2)]      | { a, b -> a + b }       | some(3)
        [some(3), none() ]      | { a, b -> a + b }       | none()
        [some(3),none(),some(4)]| { a, b -> a + b }       | none()
    }
}
