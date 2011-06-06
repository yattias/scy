package eu.scy.agents.util;

public class Pair<K, V> {

	private K key;
	private V value;

	public Pair() {
		this(null, null);
	}

	public Pair(K key, V value) {
		this.key = key;
		this.value = value;
	}

	public void setKey(K key) {
		this.key = key;
	}

	public void setValue(V value) {
		this.value = value;
	}

	public K getKey() {
		return key;
	}

	public V getValue() {
		return value;
	}

	@SuppressWarnings("unchecked")
	@Override
	public boolean equals(Object obj) {
		if (obj == null) {
			return false;
		}
		if (obj == this) {
			return true;
		}
		if (obj.getClass() != getClass())
			return false;

		Pair<K, V> other = (Pair<K, V>) obj;
		return (other.key.equals(key)) && (other.value.equals(value));
	}

	@Override
	public int hashCode() {
		return key.hashCode() * value.hashCode() + value.hashCode();
	}

	@Override
	public String toString() {
		return "(" + key + "," + value + ")";
	}

}
