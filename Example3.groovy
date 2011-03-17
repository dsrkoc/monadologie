import hr.helix.monadologie.*
import static hr.helix.monadologie.MonadComprehension.foreach

def c(o) { o.getClass().name }

def l1 = [1, 2, 3, 4],
    l2 = [2, 3, 4, 5]

def res = foreach {
    a = takeFrom { l1 }
    b = takeFrom { l2 }
    guard { a < 3 }
    guard { b > 3 }
    yield { a + b }
}
println "result1 = $res; class=${c(res)}"
println "-----"

res = foreach {
    a = takeFrom { l1 }
    b = takeFrom { 1..a }
    yield { a * b }
}
println "result2 = $res"
println "-----"

res = foreach {
    a = takeFrom { [1, 2] as Set }
    yield { a * 2 }
}
println "result3 = $res; class=${c(res)}"
println "-----"

res = foreach {
    a = takeFrom{ [1,2] as Set }
    b = takeFrom{ [3,4] }
    yield { a * b }
}
println "result4 = $res; class=${c(res)}"
println "-----"

res = foreach {
    a = takeFrom { [1,2,3,4] }
    b = takeFrom { [ a:5, b:6 ]}
    yield { a + b.value }
}
println "result5 = $res; class=${c(res)}"
