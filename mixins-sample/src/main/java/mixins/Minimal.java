package mixins;

import java.util.WeakHashMap;

interface Mixin {
    static WeakHashMap<Mixin, String> stateMap = new WeakHashMap<>();

    default String getValue() {
        stateMap.putIfAbsent(this, "");
        return stateMap.get(this);
    }

    default void setValue(String to) {
        stateMap.put(this, to);
    }
}

class Foo implements Mixin {}

public class Minimal {
    public static void main(String[] args) {
        var foo = new Foo();

        System.out.printf("foo.getValue(): %s\n", foo.getValue());

        foo.setValue("Hello World!");
        System.out.printf("foo.getValue(): %s\n", foo.getValue());
    }
}