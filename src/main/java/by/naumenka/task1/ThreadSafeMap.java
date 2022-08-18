package main.java.by.naumenka.task1;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

public class ThreadSafeMap<K, V> implements Map<K, V> {

    private final ReentrantLock lock = new ReentrantLock();
    private final Set<Map.Entry<K, V>> entrySet = new HashSet<>();

    @Override
    public int size() {
        lock.lock();
        int size = entrySet.size();
        lock.unlock();

        return size;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return false;
    }

    @Override
    public boolean containsValue(Object value) {
        return false;
    }

    @Override
    public V get(Object key) {
        lock.lock();
        for (Map.Entry<K, V> entry : entrySet){
            if (entry.getKey().equals(key)){
                return entry.getValue();
            }
        }
        lock.unlock();
        return null;
    }

    @Override
    public V put(K key, V value) {
        lock.lock();
        entrySet.add(new Entry<>(key, value));
        lock.unlock();
        return value;
    }

    @Override
    public V remove(Object key) {
        return null;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Set<K> keySet() {
        return new HashSet<>();
    }

    @Override
    public Collection<V> values() {
        lock.lock();
        Set<V> vSet = entrySet.stream()
                .map(Map.Entry::getValue)
                .collect(Collectors.toSet());
        lock.unlock();
        return vSet;
    }

    @Override
    public Set<Map.Entry<K, V>> entrySet() {
        return entrySet;
    }

    public static class Entry<K, V> implements Map.Entry<K, V> {

        private K key;
        private V value;

        public Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public void setKey(K key) {
            this.key = key;
        }

        public V getValue() {
            return value;
        }

        public V setValue(V value) {
            this.value = value;
            return value;
        }
    }
}