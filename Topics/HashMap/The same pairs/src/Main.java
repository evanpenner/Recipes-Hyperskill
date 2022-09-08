import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;


class MapFunctions {

    public static void calcTheSamePairs(Map<String, String> map1, Map<String, String> map2) {
        // write your code here
        AtomicInteger count = new AtomicInteger();
        map1.forEach((s, s2) -> {
            if (map2.containsKey(s) && map2.get(s).equals(s2))
                count.getAndIncrement();
        });
        System.out.print(count.get());
    }
}