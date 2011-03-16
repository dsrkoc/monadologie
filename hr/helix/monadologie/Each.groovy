package hr.helix.monadologie

class Each {
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
        currProp[name]
    }
    
    def propertyMissing(String name, val) {
        propVals[name] = val
        propNames << name
    }
}
