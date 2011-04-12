package hr.helix.monadologie

import hr.helix.monadologie.monads.Monad
import hr.helix.monadologie.mcategories.FunctorCategory
import hr.helix.monadologie.mcategories.MReaderCategory

class MonadComprehension {

    // ----- monad makers -----

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
        static List unit(Map map, List list) { list.clone() } // idea: if list.size == 2 then [ list[0]:list[1] ]
        static List unit(Map map, value) { [value] }

        static def bind(Map map, Closure f) {
            map.inject([:]) { r, e ->
                def fRes = f(e)
                if (r in Map) {
                    if (fRes in Map || fRes in Map.Entry)
                        r << fRes
                    else // if one unmappable result found, transform whole result
                        r.collect { it } + fRes // into list
                }
                else
                    r + fRes // r is Map or List
            }
        }

        static List bind(List map, Closure f) {
            map.inject([]) { r, e -> r + f(e) }
        }

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

    // choosing the categories to be used on given monads

    private Class category(Collection c) { CollectionCategory }

    private Class category(Range r) { ListCategory }

    private Class category(Map c) { MapCategory }

    private Class category(Closure m) { MReaderCategory }

    private Class category(Monad m) { FunctorCategory }
    
    private Class category(Object o) {
        throw new IllegalArgumentException("Unsupported monad category: ${o.getClass().name}")
    }

    // ----- container storage -----

    private Map<String, Closure> propVals = [:]
    private List<String> propNames = []
    private List<Closure> guards   = []
    private Map<String, Object> currProp = [:]

    private freeFunctions = []

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
        applyOptionalFn(currMonad)

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

    /**
     * Queries for potential calls to unbound functions that may (or may not)
     * belong to the current monad. If such monad function is found, it is immediately
     * applied to its arguments.
     *
     * @param monad the currently used monad
     */
    private applyOptionalFn(monad) {
        if (freeFunctions) {
            def fnInfo = freeFunctions.head()
            /*try {
             ... to catch the possible exception or not to catch: that is the question */
                monad."${fnInfo.fn}"(*fnInfo.args)
//                monad.metaClass.invokeMethod(monad, fnInfo.fn, fnInfo.args)
                freeFunctions.remove(0) // the function is used up, time for the next one
            /*}
            catch (MissingMethodException ignored) {
                // the method apparently doesn't belong to this monad, maybe next time
            }*/
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

    def methodMissing(String name, args) {
        freeFunctions << [ fn:name, args:args ]
    }

    // ----- runner -----

    static foreach(Closure comprehension) {
        comprehension.delegate = new MonadComprehension()
        comprehension.resolveStrategy = Closure.DELEGATE_ONLY
        comprehension()
    }
}
