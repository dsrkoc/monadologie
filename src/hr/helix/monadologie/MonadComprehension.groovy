package hr.helix.monadologie

import hr.helix.monadologie.monads.Monad

class MonadComprehension {

    // ----- monad wrappers -----

    private static class FunctorCategory<M> {
        static M fmap(M m, Closure f) { m.bind { a -> m.unit(f(a)) }}
    }

    private static class CollectionCategory extends FunctorCategory<Collection> {
        static Collection unit(Collection coll, elem) { coll.getClass().newInstance() << elem }
        static Collection bind(Collection coll, Closure f) {
            coll.inject(coll.getClass().newInstance()) { r, e -> r + f(e) }
        }
        static Collection filter(Collection coll, Closure f) { coll.findAll(f) }
    }

    private static class MapCategory extends FunctorCategory<Map> {
        static Map unit(Map map, Map elem) { elem.clone() }
        static Map unit(Map map, Map.Entry elem) { [:] << elem }
        static Map unit(Map map, key, value) { [(key):value] }
        static Map unit(Map map, value) { [(value):value] }

        static Map bind(Map map, Closure f) { map.inject([:]) { r, e -> r + f(e) }}
        static Map filter(Map map, Closure f) { map.findAll(f) }

        static List fmap(Map map, Closure f) { map.collect { a -> f(a) }}
    }

    /* List is easily processed under Collection, but Range isn't.
       Range, however, can be processed as List. */
    private static class ListCategory extends FunctorCategory<List> {
        static List unit(List list, elem) { [elem] }
        static List bind(List list, Closure f) { list.inject([]) { r, e -> r + f(e) }}
        static List filter(List list, Closure f) { list.findAll(f) }
    }

    private Class category(Collection c) { CollectionCategory }
    private Class category(Range c) { ListCategory }
    private Class category(Map c) { MapCategory }
    private Class category(Monad m) { FunctorCategory }
    private Class category(Object o) {
        throw new RuntimeException("unsupported monad category: ${o.getClass().name}")
    }

    // ----- container storage -----

    private Map<String, Object> propVals = [:]
    private List<String> propNames = []
    private List<Closure> guards   = []
    private Map<String, Object> currProp = [:]

    // ----- comprehension interface -----

    private takeFrom(Closure vals) { vals }
    
    private guard(Closure g) { guards << g }
    
    private yield(Closure yieldStep) { processOuter(propNames, yieldStep) }

    // ----- comprehension implementation -----

    private inContext(String name, value, Closure action) {
        currProp[name] = value
        ctx(this, action)
    }

    private Closure ctx(delegate, Closure action) {
        action.delegate = delegate
        action.resolveStrategy = Closure.DELEGATE_ONLY
        action
    }

    private processOuter(List<String> names, Closure yieldAction) {
        def (curr, rest) = [names.head(), names.tail()]
        
        def currMonad = ctx(this, propVals[curr])()

        use(category(currMonad)) {
            if (rest)
                currMonad.bind { elem ->
                    currProp[curr] = elem
                    processOuter(rest, yieldAction)
                }
            else {
                def container = guards ? // filter if at least one guard is given
                    currMonad.filter { elem ->
                        guards.every { inContext(curr, elem, it)() }
                    } : currMonad

                container.fmap { elem ->
                    inContext(curr, elem, yieldAction)()
                }
            }
        }
    }

    // ----- dynamic properties -----

    def propertyMissing(String name) {
        currProp[name]
    }
    
    def propertyMissing(String name, val) {
        propVals[name] = val
        propNames << name
    }

    // ----- runner -----

    static foreach(Closure comprehension) {
        comprehension.delegate = new MonadComprehension()
        comprehension.resolveStrategy = Closure.DELEGATE_ONLY
        comprehension()
    }
}
