package hr.helix.monadologie.monads

import spock.lang.Specification
import static hr.helix.monadologie.monads.Monoids.asMonoid
import static hr.helix.monadologie.monads.Option.some
import static hr.helix.monadologie.monads.Option.none
import java.text.SimpleDateFormat

/**
 * Monoids must follow the monoid laws:
 * <ul>
 *     <li> left identity:  <p>
 *     <code> identity ∘ x ≡ x  </code>
 *
 *     <li> right identity: <p>
 *     <code> x ∘ identity ≡ x  </code>
 *
 *     <li> associativity:  <p>
 *     <code> (x ∘ y) ∘ z ≡ x ∘ (y ∘ z) </code>
 * </ul>
 * where ∘ is the <code> append </code> operator.
 *
 * @author Dinko Srkoč
 * @since 2011-04-12
 */
class MonoidSpec extends Specification {

//    @spock.lang.Unroll('m1: #monoid1, m2: #monoid2, m3: #monoid3')
    def 'Monoids should obey their three laws'() {
        expect:
        monoid1.identity.append(monoid2) == monoid2 // left identity
        monoid1.append(monoid2.identity) == monoid1 // right identity
        monoid1.append(monoid2).append(monoid3) == monoid1.append(monoid2.append(monoid3)) // associativity

        where:
        monoid1                        | monoid2                        | monoid3
        asMonoid("aa")                 | asMonoid('bb')                 | asMonoid('cc')
        asMonoid(11)                   | asMonoid(22)                   | asMonoid(33)
        asMonoid(1.5)                  | asMonoid(1.8)                  | asMonoid(2.1)
        asMonoid(true)                 | asMonoid(true)                 | asMonoid(true)
        asMonoid(true)                 | asMonoid(false)                | asMonoid(false)
        asMonoid([1, 2])               | asMonoid([3, 4])               | asMonoid([5, 6])
        asMonoid([1, 2] as Set)        | asMonoid([3, 4] as Set)        | asMonoid([5, 6] as Set)
        asMonoid([1, 2] as LinkedList) | asMonoid([3, 4] as LinkedList) | asMonoid([5, 6] as LinkedList)
        asMonoid([aa:1, bb:2])         | asMonoid([bb:55, cc:3])        | asMonoid([dd: 9])
        asMonoid((Option)some(3))      | asMonoid((Option)some(5))      | asMonoid((Option)some(2))
        asMonoid((Option)none())       | asMonoid((Option)some(5))      | asMonoid((Option)some(1))
        asMonoid((Option)none())       | asMonoid((Option)none())       | asMonoid((Option)none())
        asMonoid((Option)some('aa'))   | asMonoid((Option)some('bb'))   | asMonoid((Option)none())
    }

    def 'Converting unsupported types should throw IllegalArgumentException'() {
        when:
        asMonoid(new SimpleDateFormat())

        then:
        thrown(IllegalArgumentException)
    }
}
