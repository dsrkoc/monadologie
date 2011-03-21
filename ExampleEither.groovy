import hr.helix.monadologie.monads.*
import static hr.helix.monadologie.MonadComprehension.foreach

def safeDiv = { a, b ->
    try {
        Either.right(a / b)
    } catch (e) {
        Either.left(e.message)
    }
}

def ress = []
ress << safeDiv(2, 3)
ress << safeDiv(4, 2)
ress << safeDiv(3, 0)

println "lefties: ${Either.lefts(ress)}"
println "righties: ${Either.rights(ress)}"
println "reduce0: ${Either.reduce(ress[0])}"
println "reduce2: ${Either.reduce(ress[2])}"

print "either0: "; ress[0].either({ println "left: $it" }, { println "right: $it"  })
print "either2: "; ress[2].either({ println "left: $it" }, { println "right: $it"  })

def r = foreach {
    a = takeFrom { Option.some(2) }
    b = takeFrom { Option.some(3) }
    yield { [a, b] }
}
println "res1: r=$r"

def s1 = safeDiv(3, 0)
println "1: ${s1.left().bind { Either.left(it) }}"
println "2: ${s1.left().bind { Either.right(it) }}"
println "3: ${s1.right().bind { Either.left(it) }}"
println "4: ${s1.right().bind { Either.right(it) }}"

println "----------\nforeach example\n----------"

def r2 = foreach {
    a = takeFrom { safeDiv(2, 3).right() }
    b = takeFrom { safeDiv(3, 0).left() }
    c = takeFrom { safeDiv(2, 2).right() }
    yield { [a, b, c] }
}

println r2
