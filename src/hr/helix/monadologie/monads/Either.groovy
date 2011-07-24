package hr.helix.monadologie.monads

abstract class Either<L, R> implements Monad<Either> {

    static <A, B> Either<A, B> left(final A l)  { new Left<A, B>(l) }
    static <A, B> Either<A, B> right(final B r) { new Right<A, B>(r) }

    Boolean isLeft()  { false }
    Boolean isRight() { false }

    abstract Object get()

    L getLeft()  { throw new NoSuchElementException('Cannot resolve left value') }
    R getRight() { throw new NoSuchElementException('Cannot resolve right value') }

    def either(final Closure leftAction, final Closure rightAction) {
        isLeft() ? leftAction(left) : rightAction(right)
    }

    static <A> A reduce(final Either<A, A> e) {
        e.isLeft() ? e.left : e.right
    }

    private static final class Left<L, R> extends Either<L, R> {
        private final L value

        Left(final L l) { value = l }

        @Override Boolean isLeft() { true }
        @Override Object get() { value }
        @Override L getLeft()  { value }

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

        @Override Boolean isRight() { true }
        @Override Object get() { value }
        @Override R getRight() { value }

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

    // --- Monad interface implementation ---

    @Override Either unit(Object a) { Either.right(a) }

    @Override Either bind(Closure f) {
        isRight() ? f(get()) : this
    }

    Option filter(Closure f) {
        isRight() ?
            (f(get()) ? Option.some(this) : Option.none()) :
            Option.none()
    }

    // --- ---

    static <A, B> List<A> lefts(List<Either<A, B>> es) {
        es.sum { Either e -> e.isLeft() ? [ e.left ] : [] }
    }

    static <A, B> List<B> rights(List<Either<A, B>> es) {
        es.sum { Either e -> e.isRight() ? [ e.right ] : [] }
    }
}
