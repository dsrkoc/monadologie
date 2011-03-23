package hr.helix.monadologie

import spock.lang.Specification
import static hr.helix.monadologie.MonadComprehension.CollectionCategory.bind as c_bind
import static hr.helix.monadologie.MonadComprehension.CollectionCategory.unit as c_unit
import static hr.helix.monadologie.MonadComprehension.ListCategory.bind as l_bind
import static hr.helix.monadologie.MonadComprehension.ListCategory.unit as l_unit
import static hr.helix.monadologie.MonadComprehension.MapCategory.bind as m_bind
import static hr.helix.monadologie.MonadComprehension.MapCategory.unit as m_unit
import hr.helix.monadologie.monads.*

/**
 * @author Dinko Srkoč
 * @since 2011-03-22
 */
class MonadLawsSpec extends Specification {

    def 'built-in Collection support should obey the monad laws'() {
        given:
        def a = 1
        def f = { c_unit(monad, it + 1) }
        def g = { c_unit(monad, it * 2) }

        when:
         // law: identity
        def idLeft  = c_bind(monad, { x -> c_unit(monad, x) })
        def idRight = monad

        // law: unit
        def unitLeft  = c_bind(c_unit(monad, a), f),
            unitRight = f(a)

        // law: composition
        def cmpLeft  = c_bind(c_bind(monad, { x -> g(x) }), { y -> f(y) }),
            cmpRight = c_bind(monad, { x -> c_bind(g(x), { y -> f(y) })})

        then:
        idLeft   == idRight
        unitLeft == unitRight
        cmpLeft  == cmpRight

        where:
        monad << [ [1,2,3], [1,2] as Set]
    }

    def 'built-in List support should obey the monad laws'() {
        given:
        def a = 1
        def f = { l_unit(monad, it + 1) }
        def g = { l_unit(monad, it * 2) }

        when:
         // law: identity
        def idLeft  = l_bind(monad, { x -> l_unit(monad, x) })
        def idRight = monad

        // law: unit
        def unitLeft  = l_bind(l_unit(monad, a), f),
            unitRight = f(a)

        // law: composition
        def cmpLeft  = l_bind(l_bind(monad, { x -> g(x) }), { y -> f(y) }),
            cmpRight = l_bind(monad, { x -> l_bind(g(x), { y -> f(y) })})

        then:
        idLeft   == idRight
        unitLeft == unitRight
        cmpLeft  == cmpRight

        where:
        monad << [ [1,2,3], 1..4 ]
    }

    def 'built-in Map support should obey the monad laws'() {
        given:
        def a = 1
        def f = { m_unit(monad, it.value + 1) }
        def g = { m_unit(monad, it.value * 2) }

        when:
         // law: identity
        def idLeft  = m_bind(monad, { x -> m_unit(monad, x) })
        def idRight = monad

        // law: unit
        def unitLeft  = m_bind(m_unit(monad, a), f),
            unitRight = f(a)

        // law: composition
        def cmpLeft  = m_bind(m_bind(monad, { x -> g(x) }), { y -> f(y) }),
            cmpRight = m_bind(monad, { x -> m_bind(g(x), { y -> f(y) })})

        then:
        idLeft   == idRight
        unitLeft == unitRight
        cmpLeft  == cmpRight

        where:
        monad << [ [a: 1, b: 2], [c: 5] ]
    }

    def 'Option monad should obey the monad laws'() {
        given:
        def f = { monad.unit(it + 1) }
        def g = { monad.unit(it * 2) }

        when:
         // law: identity
        def idLeft  = monad.bind({ x -> monad.unit(x) })
        def idRight = monad

        // law: unit
        def unitLeft  = monad.bind(f),
            unitRight = f(a)

        // law: composition
        def cmpLeft  = monad.bind({ x -> g(x) }).bind({ y -> f(y) }),
            cmpRight = monad.bind({ x -> g(x).bind({ y -> f(y) })})

        then:
        idLeft   == idRight
        unitLeft == unitRight
        cmpLeft  == cmpRight

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

}
