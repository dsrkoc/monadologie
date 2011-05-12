Monadologie
-----------

### What is Monadologie?

Monadologie is [Groovy][1] library for monad comprehension.

Monad comprehensions are generalization for list comprehensions which allow us
to express certain types of computations in a more concise way.

Let's get down to examples...

#### Examples

Here, you can find two examples, using the list monads.

In the first example we simply sum each element of the first list with
each element of the second list.

    import static hr.helix.monadologie.MonadComprehension.foreach

    def res = foreach {
        a = takeFrom { [1, 2, 3] }
        b = takeFrom { [4, 5] }

        yield { a + b }
    }

    assert res == [5, 6, 6, 7, 7, 8]

Now, for a slightly more elaborate example. Say we have a chess board and one knight piece on it.
We want to find out if the knight can reach certain position in three moves (example taken from
the book [Learn You a Haskell for Great Good][5]).

    import static hr.helix.monadologie.MonadComprehension.foreach

    // returns all possible knight moves given column and row
    def moveKnight = { c, r ->
        foreach {
            cr1 = takeFrom {
                [[c+2, r-1], [c+2, r+1], [c-2, r-1], [c-2, r+1],
                 [c+1, r-2], [c+1, r+2], [c-1, r-2], [c-1, r+2]]
            }

            // guard prevents knight from escaping the chess board
            guard {
                def (c1, r1) = cr1
                c1 in 1..8 && r1 in 1..8
            }

            yield { cr1 }
        }
    }

    // returns all possible knight positions after three moves
    def threeMoves = { start ->
        foreach {
            first  = takeFrom { moveKnight start }
            second = takeFrom { moveKnight first }
            third  = takeFrom { moveKnight second }

            yield { third }
        }
    }

    // returns true if end position can be reached from start position
    // in three moves, otherwise returns false
    def canReachIn3 = { start, end ->
        end in threeMoves(start)
    }

    assert canReachIn3([6, 2], [6, 3]) == true
    assert canReachIn3([6, 2], [7, 3]) == false

### What's in a Name?

In his famous work *La Monadologie*, 1714, German philosopher and mathematician Gottfried Wilhelm Leibniz described monads
as "substantial forms of being" with the following properties: they are eternal, indecomposable, individual, subject to their
own laws, un-interacting, and each reflecting the entire universe in a pre-established harmony. They are irreducibly simple,
but possess no material or spatial character. (source: [Wikipedia][6])

His monads, however, are *not* related to our monads in any way. Monads used here have their origin in mathematical
[Category theory][8] from the 1960's. This library is named *monadologie* as an homage to this [first computer scientist][7].

### Documentation

The documentation for Monadologie can be found on the [wiki][10] pages.

### Inspirations

This implementation is in large part inspired by [Scala][3]'s `for` comprehension.
One notable difference is that in Scala `yield` is optional, thus making `for` behave in
two different ways. In Monadologie `yield` is used always and there's no such duality.

Of course, if working with monads, one cannot escape [Haskell][4], the "premiere monad language".

### How to Build

#### Requirements

* JDK 1.6
* Groovy 1.7+
* [Gradle][2] 1.0

#### The Process

1. get the source (download or git clone/hg clone)
2. run `gradle build`, this will assemble the jars and run the tests
3. profit

### What I have so far

The following monads are currently implemented:

* Collection (aka List, Sequence), Map
* Option (aka Maybe)
* Either
* Reader
* Writer
* State

### License

Copyright 2011 Dinko Srko&#0269;. Released under the [Apache Public License, v2.0][9]


[1]: http://groovy.codehaus.org                                     "Groovy Programming Language"
[2]: http://www.gradle.org                                          "Gradle Build Tool"
[3]: http://www.scala.org                                           "Scala Programming Language"
[4]: http://haskell.org/haskellwiki/Haskell                         "Haskell Programming Language"
[5]: http://learnyouahaskell.com/a-fistful-of-monads#the-list-monad "Learn You a Haskell - A Fistful of Monads"
[6]: http://en.wikipedia.org/wiki/Gottfried_Leibniz#The_monads      "Leibniz: about monads"
[7]: http://en.wikipedia.org/wiki/Gottfried_Leibniz#Computation     "Leibniz: the computer scientist"
[8]: http://en.wikipedia.org/wiki/Category_theory                   "Category theory"
[9]: http://www.apache.org/licenses/LICENSE-2.0                     "Apache Public License, v2.0"
[10]: https://github.com/dsrkoc/monadologie/wiki                    "Monadologie Documentation"
