package hr.helix.monadologie.monads

abstract class Either<L, R> implements Monad<Either> {

    static <A, B> Either<A, B> left(final A l)  { new Left<A, B>(l) }
    static <A, B> Either<A, B> right(final B r) { new Right<A, B>(r) }

    boolean isLeft()  { false }
    boolean isRight() { false }

    abstract Object get()

    L getLeftValue()  { throw new NoSuchElementException('Cannot resolve left value') }
    R getRightValue() { throw new NoSuchElementException('Cannot resolve right value') }

    def either(final Closure leftAction, final Closure rightAction) {
        left ? leftAction(leftValue) : rightAction(rightValue)
    }

    static <A> A reduce(final Either<A, A> e) {
        e.left ? e.leftValue : e.rightValue
    }

    private static final class Left<L, R> extends Either<L, R> {
        private final L value

        Left(final L l) { value = l }

        @Override boolean isLeft() { true }
        @Override Object get() { value }
        @Override L getLeftValue()  { value }

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

        @Override boolean isRight() { true }
        @Override Object get() { value }
        @Override R getRightValue() { value }

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
        right ? f(get()) : this
    }

    Option filter(Closure f) {
        right ?
            (f(get()) ? Option.some(this) : Option.none()) :
            Option.none()
    }

    // --- ---

    static <A, B> List<A> lefts(List<Either<A, B>> es) {
        es.sum { Either e -> e.left ? [ e.leftValue ] : [] }
    }

    static <A, B> List<B> rights(List<Either<A, B>> es) {
        es.sum { Either e -> e.right ? [ e.rightValue ] : [] }
    }
}
