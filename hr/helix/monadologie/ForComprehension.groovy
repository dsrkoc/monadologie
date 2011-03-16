package hr.helix.monadologie

class ForComprehension {
    /*
    private static class Each {
        private Map<String, List> propVals = [:]
        private List<String> propNames = []
        private List<Closure> guards   = []
        private Map<String, Object> currProp = [:]
        
        private each(List vals) { vals }
        
        private guard(Closure g) { guards << g }
        
        private yield(Closure yieldStep) {
            yieldStep.delegate = this
            yieldStep.resolveStrategy = Closure.DELEGATE_ONLY
            processOuter(propNames, yieldStep)
        }
        
        private processOuter(names, Closure action) {
            def (curr, rest) = [names.head(), names.tail()]
            
            def currList = propVals[curr]
            
            if (rest)
                flatMap(currList, curr) {
                    processOuter(rest, action)
                }
            else
                currList.findAll { elem ->
                    currProp[curr] = elem
                    guards.every { grd -> grd() }
                }.collect { elem ->
                    currProp[curr] = elem
                    action()
                }
        }
        
        private flatMap(List list, String name, Closure action) {
            list.inject([]) { r, e ->
                currProp[name] = e
                r + action()
            }
        }
        
        def propertyMissing(String name) {
            println "property getter: name=$name"
            currProp[name]
        }
        
        def propertyMissing(String name, val) {
            println "property setter: name=$name, val=$val"
            propVals[name] = val
            propNames << name
        }

        def getProperty(String name) {
            println "DEBUG: getter $name"
            currProp[name]
        }

        void setProperty(String name, val) {
            println "DEBUG: getter $name=$val"
            propVals[name] = val
            propNames << name
        }

    }
    */

    // ---- runner ----

    static foreach(Closure comprehension) {
        comprehension.delegate = new Each()
        comprehension.resolveStrategy = Closure.DELEGATE_ONLY
        comprehension()
    }
}

