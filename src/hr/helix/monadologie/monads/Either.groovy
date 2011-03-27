package hr.helix.monadologie.monads

abstract class Either<L, R> {

    LeftProjection<L, R>  left() { new LeftProjection<L, R>(this) }
    RightProjection<L, R> right() { new RightProjection<L, R>(this) }

    static <A, B> Either<A, B> left(final A l)  { new Left<A, B>(l) }
    static <A, B> Either<A, B> right(final B r) { new Right<A, B>(r) }

    abstract Boolean isLeft()
    abstract Boolean isRight()

    def either(final Closure leftAction, final Closure rightAction) {
        isLeft() ?
            leftAction(left().get()) :
            rightAction(right().get())
    }

    static <A> A reduce(final Either<A, A> e) {
        e.isLeft() ? e.left().get() : e.right().get()
    }

    private static final class Left<L, R> extends Either<L, R> {
        private final L value

        Left(final L l) { value = l }

        Boolean isLeft()  { true }
        Boolean isRight() { false }

        String toString() { "Left($value)" }

        boolean equals(o) {
            if (this.is(o)) return true;
            if (getClass() != o.class) return false;

            Left left = (Left) o;

            if (value != left.value) return false;

            return true;
        }

        int hashCode() {
            return (value != null ? value.hashCode() : 0);
        }

    }

    private static final class Right<L, R> extends Either<L, R> {
        private final R value

        Right(final R r) { value = r }

        Boolean isLeft()  { false }
        Boolean isRight() { true }

        String toString() { "Right($value)" }

        boolean equals(o) {
            if (this.is(o)) return true;
            if (getClass() != o.class) return false;

            Right right = (Right) o;

            if (value != right.value) return false;

            return true;
        }

        int hashCode() {
            return (value != null ? value.hashCode() : 0);
        }
    }

    final class LeftProjection<A, B> implements Monad<Either> {
        private final Either<A, B> e

        private LeftProjection(final Either<A, B> either) {
            e = either
        }

        A get() {
            if (e.isLeft())
                e.value
            else
                throw new RuntimeException('left.value on Right')
        }

        /**
         * The Either value underlying this projection.
         * @return Either object encapsulated by the projection
         */
        Either<A, B> either() { e }

        // --- Monad interface implementation ---

        Either unit(A a) { Either.left(a) }

        Either bind(Closure f) {
            def res = isLeft() ? f(get()) : e
            res
        }

        Option filter(Closure f) {
            isLeft() && f(get()) ?
                Option.some(e) :
                Option.none()
        }
    }

    final class RightProjection<A, B> implements Monad<Either> {
        private final Either<A, B> e

        private RightProjection(final Either<A, B> either) {
            e = either
        }

        B get() {
            if (e.isRight())
                e.value
            else
                throw new RuntimeException('right.value on Left')
        }

        /**
         * The Either value underlying this projection.
         * @return Either object encapsulated by the projection
         */
        Either<A, B> either() { e }

        // --- Monad interface implementation ---

        Either unit(B b) { Either.right(b) }

        Either bind(Closure f) {
            isRight() ? f(get()) : e
        }

        Option filter(Closure f) {
            isRight() && f(get()) ?
                Option.some(e) :
                Option.none()
        }
    }

    static <A, B> List<A> lefts(List<Either<A, B>> es) {
        es.inject([]) { res, e ->
            e.isLeft() ? (res << e.left().value) : res
        }
    }

    static <A, B> List<B> rights(List<Either<A, B>> es) {
        es.inject([]) { res, e ->
            e.isRight() ? (res << e.right().value) : res
        }
    }
}
