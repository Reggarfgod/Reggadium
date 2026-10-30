package com.reggarf.mods.reggadium.config;

public interface ConfigValue<T> {
    T get();
    void set(T value);

    static <T> ConfigValue<T> of(T initial) {
        return new ConfigValue<T>() {
            private T val = initial;
            @Override
            public T get() { return val; }
            @Override
            public void set(T value) { this.val = value; }
        };
    }
}
