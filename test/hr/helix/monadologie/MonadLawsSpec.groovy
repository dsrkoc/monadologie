package hr.helix.monadologie

import spock.lang.Specification
import hr.helix.monadologie.MonadComprehension as MC
import static hr.helix.monadologie.MonadComprehension.CollectionCategory.unit as c_unit
import static hr.helix.monadologie.MonadComprehension.ListCategory.unit as l_unit
import static hr.helix.monadologie.MonadComprehension.MapCategory.unit as m_unit
import hr.helix.monadologie.monads.*

/**
 * Running monad laws on unsuspecting monads.
 *
 * @author Dinko Srkoč
 * @since 2011-03-22
 */
class MonadLawsSpec extends Specification {

    def 'built-in Collection support should obey the monad laws'() {
        given:
        def a = 1
        def f = { c_unit(monad, it + 1) }
        def g = { c_unit(monad, it * 2) }

        expect:
        testMonadCategory(MC.CollectionCategory, monad, a, f, g)

        where:
        monad << [ [1,2,3], [1,2] as Set]
    }

    def 'built-in List support should obey the monad laws'() {
        given:
        def a = 1
        def f = { l_unit(monad, it + 1) }
        def g = { l_unit(monad, it * 2) }

        expect:
        testMonadCategory(MC.ListCategory, monad, a, f, g)

        where:
        monad << [ [1,2,3], 1..4 ]
    }

    @SuppressWarnings("GroovyAssignabilityCheck")
    def 'built-in Map support should obey the monad laws'() {
        given:
        def a = 1
        def f = { m_unit(monad, it.value + 1) }
        def g = { m_unit(monad, it.value * 2) }

        expect:
        testMonadCategory(MC.MapCategory, monad, a, f, g)

        where:
        monad << [ [a: 1, b: 2], [c: 5] ]
    }

    def 'Option monad should obey the monad laws'() {
        given:
        def f = { monad.unit(it + 1) }
        def g = { monad.unit(it * 2) }

        expect:
        identityLaw(monad)
        unitLaw(monad, a, f)
        compositionLaw(monad, f, g)

        where:
        monad << [ Option.some(1), Option.some(2)]
        a << [1, 2]
    }

//    @spock.lang.Unroll
    def 'Either monad should obey the monad laws'() {
        given:
        def f = { monad.unit(it + 1) }
        def g = { monad.unit(it * 2) }

        when:
         // law: identity
        def idLeft  = monad.bind({ x -> monad.unit(x) })
        def idRight = monad.either()

        // law: unit
        def unitLeft  = monad.bind(f),
            unitRight = f(a)

        // law: composition
        def cmpLeft  = monad.bind({ x -> g(x) }).right().bind({ y -> f(y) }),
            cmpRight = monad.bind({ x -> g(x).right().bind({ y -> f(y) })})

        then:
        idLeft   == idRight
        unitLeft == unitRight
        cmpLeft  == cmpRight

        where:
        monad << [ Either.right(1).right(), Either.right(2).right() ]
        a << [1, 2]
    }

    private void testMonadCategory(MonadCategory, m, a, f, g) {
        use(MonadCategory) {
            identityLaw(m)
            unitLaw(m, a, f)
            compositionLaw(m, f, g)
        }
    }

    // Monad Laws

    private void identityLaw(m) { // a.k.a. right identity
        assert m.bind({ x -> m.unit(x) }) == m
    }

    private void unitLaw(m, a, Closure f) { // a.k.a. left identity
        assert m.unit(a).bind(f) == f(a)
    }

    private void compositionLaw(m, Closure f, Closure g) { // a.k.a. associativity
        assert m.bind({ x -> g(x) }).bind({ y -> f(y) }) == m.bind({ x -> g(x).bind({ y -> f(y) }) })
    }

}