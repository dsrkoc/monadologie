package hr.helix.monadologie

import spock.lang.Specification
import hr.helix.monadologie.mcategories.MCollectionCategory
import hr.helix.monadologie.mcategories.MListCategory
import hr.helix.monadologie.mcategories.MMapCategory
import hr.helix.monadologie.monads.Option

/**
 * Running them laws on unsuspecting monad+s
 * 
 * @author Dinko Srkoč
 * @since 2011-07-08
 */
class MonadPlusLawsSpec extends Specification {

    def 'built-in Collection support should obey monadplus laws'() {
        expect: testPlusCategory(MCollectionCategory, [1, 2, 3] as Set)
    }

    def 'built-in List support should obey monadplus laws'() {
        expect: testPlusCategory(MListCategory, 1..3)
    }

    def 'built-in Map support should obey monadplus laws'() {
        expect: testPlusCategory(MMapCategory, [ aa:11, bb:22 ])
    }

    def 'Option should obey monadplus laws'() {
        expect:
        testSum(option)
        testProduct(option)

        where:
        option << [ Option.some(3), Option.some('foo'), Option.none() ]
    }

    private void testPlusCategory(MPlusCategory, ma) {
        use(MPlusCategory) {
            testSum(ma)
            testProduct(ma)
        }
    }

    private void testSum(ma) {
        assert ma.mplus(ma.mzero()) == ma
        assert ma.mzero().mplus(ma) == ma
    }

    private void testProduct(ma) {
        assert ma.bind({ ma.mzero() }) == ma.mzero()
        assert ma.mzero().bind({ ma }) == ma.mzero()
    }
}
