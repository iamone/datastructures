package my.structures;

public class HashMap<K,V> {
	
	Entry<K,V>[] table;
	
	int size = 0;
	int threshold;
	float loadFactor;
	int capacity;
	
	static final int DEFAULT_INITIAL_CAPACITY = 16;
	static final int DEFAULT_INITIAL_THRESHOLD = 8;
	static final float DEFAULT_INITIAL_LOAD_FACTOR = 0.75f;
	
	
	@SuppressWarnings("unchecked")
	public HashMap() {
		capacity = DEFAULT_INITIAL_CAPACITY;
		threshold = DEFAULT_INITIAL_THRESHOLD;
		table = new Entry[capacity];
		loadFactor = DEFAULT_INITIAL_LOAD_FACTOR;
	}
	
	public V put(K key, V value) {
		ensureCapacity();
		if(key == null) {
			V oldValue = putForNullKey(value);
			return oldValue;
		}
		
		int hash = hash(key);
		int index = evaluateIndex(hash);
		
		V oldValue = putForKey(key, value, index, hash);
		
		return oldValue;
	}

	public V get(K key) {
		int hash = hash(key);
		int index = evaluateIndex(hash);
		
		V value = getForKey(key, index, hash);
		
		return value;
	}
	
	public boolean containsKey(K key) {
		int hash = hash(key);
		int index = evaluateIndex(hash);
		
		V value = getForKey(key, index, hash);
		
		return value != null;
	}
	
	public V remove(K key) {
		return removeForKey(key);
	}
	
	private V putForNullKey(V value) {
		Entry<K, V> entry = table[0];
		while(entry != null) {
			if(entry.key == null) {
				V oldValue = entry.value;
				entry.value = value;
				return oldValue;
			}
			entry = entry.next;
		}
		addEntry(null, value, 0, 0);
		
		return null;
	}
	
	private V removeForKey(K key) {
		int hash = hash(key);
		int index = evaluateIndex(hash);
		
		Entry<K, V> entry = table[index];
		if(entry == null)
			return null;
		
		Entry<K, V> nextEntry = entry.next;
		if(hash == entry.hash && (entry.key == key || entry.key.equals(key))) {
			table[index] = nextEntry;
			return entry.value;
		}
		
		while(nextEntry != null) {
			if(hash == nextEntry.hash && (nextEntry.key == key || nextEntry.key.equals(key))) {
				entry.next = nextEntry.next;
				return nextEntry.value;
			}
			entry = nextEntry;
			nextEntry = nextEntry.next;
		}
		
		return null;
	}
	
	private V getForKey(K key, int index, int hash) {
		Entry<K, V> entry = table[index];
		while(entry != null) {
			if(hash == entry.hash && (entry.key == key || entry.key.equals(key)))
				return entry.value;
			entry = entry.next;
		}
		
		return null;
	}
	
	private V putForKey(K key, V value, int index, int hash) {
		Entry<K, V> entry = table[index];
		while(entry != null) {
			if(hash == entry.hash && (entry.key == key || entry.key.equals(key))) {
				V oldValue = entry.value;
				entry.value = value;
				return oldValue;
			}
			entry = entry.next;
		}
		
		addEntry(key, value, index, hash);
		
		return null;
	}

	private void addEntry(K key, V value, int index, int hash) {
		Entry<K, V> newEntry = new Entry<>(key, value, hash, table[index]);
		table[index] = newEntry;
		size++;
	}
	
	
	int evaluateIndex(int hash) {
		return hash % capacity;
	}
	
	int hash(K key) {
		return key.hashCode();
	}
	
	private void ensureCapacity() {
		if(size == threshold) {
			capacity <<= 1;
			threshold = (int) Math.floor(capacity * loadFactor);
			Entry<K,V>[] oldTable = this.table;
			@SuppressWarnings("unchecked")
			Entry<K,V>[] newTable = new Entry[capacity];
			
			this.table = newTable;
			transfer(oldTable);
		}
			
	}
	
	private void transfer(Entry<K,V>[] fromTable) {
		for(Entry<K,V> entry : fromTable) {
			while(entry != null) {
				Entry<K,V> nextEntry = entry.next;
				
				int index = evaluateIndex(entry.hash);
				
				entry.next = table[index];
				table[index] = entry;
				
				entry = nextEntry;
			}
		}
		
	}
	
	
	public int size() {
		return size;
	}
	
	
	static class Entry<K,V> {
		K key;
		V value;
		int hash;
		Entry<K,V> next;
		
		public Entry(K key, V value, int hash, Entry<K,V> next) {
			this.key = key;
			this.value = value;
			this.hash = hash;
			this.next = next;
		}
	}
	
	

}
