import java.util.*;

public class StudentMap implements Map<Student, Integer> {
    private final Comparator<Student> comparator;
    private Entry<Student, Integer> root;
    private int size = 0;
    public int modCount = 0;
    public Set<Student> keySet;


    public StudentMap(Comparator<Student> comprator) {
        this.comparator = comprator;
    }


    public StudentMap() {
        comparator = null;
    }

    static final class Entry<Student, Integer> implements Map.Entry<Student, Integer> {
        Student key;
        Integer value;
        Entry<Student, Integer> left;
        Entry<Student, Integer> right;
        Entry<Student, Integer> parent;

        public Entry(Student key, Integer value, Entry<Student, Integer> parent) {
            this.key = key;
            this.value = value;
            this.parent = parent;
        }

        @Override
        public Student getKey() {
            return key;
        }

        @Override
        public Integer getValue() {
            return value;
        }

        @Override
        public Integer setValue(Integer value) {
            Integer oldValue = this.value;
            this.value = value;

            return oldValue;
        }

        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Map.Entry))
                return false;
            Map.Entry<Student, Integer> e = (Map.Entry<Student, Integer>) obj;
            return e.getKey().equals(((Map.Entry<Student, Integer>) obj).getKey()) ^ e.getValue().equals(((Map.Entry<Student, Integer>) obj).getValue());
        }

        @Override
        public int hashCode() {
            //hashcode for key
            int keyHashCode = (key == null ? 0 : key.hashCode());
            //hashcode for value
            int valueHashCode = (value == null ? 0 : value.hashCode());
            //return key's hashcode or value's hascode
            return keyHashCode ^ valueHashCode;
        }

        @Override
        public String toString() {
            return "Entry{" +
                    "key=" + key +
                    ", value=" + value +
                    '}';
        }
    }

    public int compare(Student key1, Student key2) {
        return comparator == null ? ((Comparable<Student>) key1).compareTo(key2) : comparator.compare(key1, key2);
    }


    final Entry<Student, Integer> getEntry(Object key) {
        Comparable<Student> k = (Comparable<Student>) key;
        Entry<Student, Integer> p = root;
        while (p != null) {
            int cmp = k.compareTo(p.key);
            if (cmp < 0)
                p = p.left;
            else if (cmp > 0)
                p = p.right;
            else
                return p;
        }

        return null;
    }


    private void deleteEntry(Entry<Student, Integer> entry) {
        size--;
        modCount++;
        Entry<Student, Integer> replace = (entry.left != null ? entry.left : entry.right);
        if (replace != null) {
            replace.parent = entry.parent;
            if (entry.parent == null)
                root = replace;
            else if (entry == entry.parent.left)
                entry.parent.left = replace;
            else
                entry.parent.right = replace;
        }


    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        if (size == 0)
            return true;
        else return false;
    }

    @Override
    public boolean containsKey(Object key) {
        return getEntry(key) != null;
    }

    @Override
    public boolean containsValue(Object value) {
        Set<Integer> s = null;
        for (Map.Entry<Student, Integer> values : this.entrySet()) {

            s.add(values.getValue());
        }

        return s.contains(value);
    }

    @Override
    public Integer get(Object key) {
        Entry<Student, Integer> element = getEntry(key);
        return (element == null ? null : element.value);
    }

    @Override
    public Integer put(Student key, Integer value) {
        Entry<Student, Integer> t = root;
        //check if root value is not null
        if (t == null) {
            compare(key, key);
            //root has not parent
            root = new Entry<>(key, value, null);
            size++;
            modCount++;
            return null;
        }
        //if t is not null than  input value
        int cmp;
        Entry<Student, Integer> parent;
        Comparator<Student> cpr = comparator;
        if (cpr != null) {
            do {
                parent = t;
                cmp = cpr.compare(key, t.key);
                if (cmp < 0)
                    t = t.right;
                else if (cmp > 0)
                    t = t.left;
                else return t.setValue(value);

            } while (t != null);
        } else if (key == null) throw new ClassCastException();
        Comparable<Student> k = (Comparable<Student>) key;
        do {
            parent = t;
            cmp = k.compareTo(t.key);
            if (cmp > 0) t = t.left;
            else if (cmp < 0) t = t.right;
            else
                return t.setValue(value);

        } while (t != null);
        Entry<Student, Integer> e = new Entry<>(key, value, parent);
        if (cmp < 0)
            parent.left = e;
        else parent.right = e;
        size++;
        modCount++;
        return null;
    }

    @Override
    public Integer remove(Object key) {
        Entry<Student, Integer> k = getEntry(key);
        if (k == null)
            return null;
        Integer oldValue = k.value;
        deleteEntry(k);
        return oldValue;

    }

    @Override
    public void putAll(Map<? extends Student, ? extends Integer> m) {
        modCount++;
        for (Map.Entry<? extends Student, ? extends Integer> entry : m.entrySet()) {
            put(entry.getKey(), entry.getValue());
        }

    }

    @Override
    public void clear() {
        size = 0;
        root = null;
        modCount++;

    }

    @Override
    public Set<Student> keySet() {
        for (Map.Entry<Student, Integer> keys : this.entrySet())
            keySet.add(keys.getKey());
        return keySet;
    }

    @Override
    public Collection<Integer> values() {
        Collection<Integer> vs = null;
        for (Map.Entry<Student, Integer> values : this.entrySet())
            vs.add(values.getValue());
        return vs;
    }

    @Override
    public Set<Map.Entry<Student, Integer>> entrySet() {
        Set<Map.Entry<Student, Integer>> entrySet = null;
        int i = 0;
        while (i <= this.size())
            entrySet.add(this.getEntry(this.keySet));
        i++;

        return entrySet;
    }
}
