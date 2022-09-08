class Predicate {
    public static final TernaryIntPredicate ALL_DIFFERENT = (a, b, c) -> {
        return a != b && b != c && a != c;
    };// Write a lambda expression here

    @FunctionalInterface
    public interface TernaryIntPredicate {
        // Write a method here
        boolean test(int a, int b, int c);
    }
}