import hr.helix.monadologie.*
import static hr.helix.monadologie.Foreach.foreach


def result = foreach {
    a = each([1, 2, 3, 4])
    b = each([2, 3, 4, 5])
    guard { a < 3 }
    guard { b > 3 }
    yield { a + b }
}

println "-----> $result"
