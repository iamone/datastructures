package my.structures;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsNot.not;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.hamcrest.core.IsNull.nullValue;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import my.structures.HashMap;

import org.junit.Test;

public class HashMapTest {
	
	private static final int TEST_INTEGER1 = 7639;
	private static final int TEST_INTEGER2 = 10;
	
	
	@Test
	public void shouldSetDefaultValuesAndCreateDefaultSizeTableOnCreation() {
		HashMap<String, String> map = new HashMap<String, String>();
		
		assertThat(map.capacity, equalTo(HashMap.DEFAULT_INITIAL_CAPACITY));
		assertThat(map.threshold, equalTo(HashMap.DEFAULT_INITIAL_THRESHOLD));
		assertThat(map.loadFactor, equalTo(HashMap.DEFAULT_INITIAL_LOAD_FACTOR));
		assertThat(map.size, equalTo(0));
		assertThat(map.table.length, equalTo(map.capacity));
	}
	
	@Test
	public void shouldEvaluateHashCodeForKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		Integer i = new Integer(TEST_INTEGER1);
		
		int hash = i.hashCode();
		int evaluatedHash = map.hash(i);
		
		assertThat(hash, equalTo(evaluatedHash));
	}
	
	@Test
	public void shouldEvaluateIndexBasedOnHash() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		
		int hash = map.hash(i);
		
		int index = hash % map.capacity;
		
		int evaluatedIndex = map.evaluateIndex(hash);
		
		assertThat(index, equalTo(evaluatedIndex));
	}
	
	@Test
	public void shouldPlaceElementInMapTableBasingOnEvaluatedIndex() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		
		int hash = map.hash(i);
		
		int index = map.evaluateIndex(hash);
		
		assertThat(map.table[index], nullValue());
		
		map.put(i, i);
		
		assertThat(map.table[index], notNullValue());
	}
	
	@Test
	public void shouldPutElementToTable() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		map.put(i, i);
		
		int hash = map.hash(i);
		
		int index = map.evaluateIndex(hash);
		
		assertThat(map.table[index].key, sameInstance(i));
		assertThat(map.table[index].value, sameInstance(i));
	}
	
	@Test
	public void shouldPutOnlyOneValueToTable() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		map.put(i, i);
		
		int count = 0;
		for(HashMap.Entry<Integer, Integer> item : map.table) {
			while(item != null) {
				item = item.next;
				count++;
			}
		}
		
		assertThat(count, equalTo(1));
	}
	
	@Test
	public void shouldReturnOldValueIfPutValueForExistingKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(TEST_INTEGER1);
		map.put(i1, i1);
		
		Integer i2 = new Integer(TEST_INTEGER2);
		
		Integer retValue = map.put(i1, i2);
		
		assertThat(retValue, sameInstance(i1));
	}
	
	@Test
	public void shouldReturnNullWhenPutItemWithKeyNotInMapYet() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(TEST_INTEGER1);
		Integer retValue = map.put(i1, i1);
		
		assertThat(retValue, nullValue());
	}
	
	@Test
	public void shouldReplaceValueWithNewSpecifiedIfPutForExistingKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(TEST_INTEGER1);
		map.put(i1, i1);
		
		Integer i2 = new Integer(TEST_INTEGER2);
		map.put(i1, i2);
		
		int hash = map.hash(i1);
		int index = map.evaluateIndex(hash);
		
		assertThat(map.table[index].value, sameInstance(i2));
	}
	
	@Test
	public void shouldScanThroughBucketWhenHashCodeMatchesToLookForEqalKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		int int2 = TEST_INTEGER1 + 1;
		
		int int1Pos = map.hash(TEST_INTEGER1) % map.capacity;
		while(int1Pos != map.hash(new Integer(int2)) % map.capacity)
			int2++;
		
		Integer i1 = new Integer(TEST_INTEGER1);
		map.put(i1, i1);
		
		Integer i2 = new Integer(int2);
		map.put(i2, i2);
		
		Integer i3 = new Integer(TEST_INTEGER1);
		Integer retVal = map.put(i3, i3);
		
		assertThat(retVal, sameInstance(i1));
	}
	
	@Test
	public void shouldReturnActualSizeWhenEmptyAddingAndUpdatingElements() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		assertThat(map.size(), equalTo(0));
		
		map.put(0, 0);
		
		assertThat(map.size(), equalTo(1));
		
		map.put(0, 1);
		
		assertThat(map.size(), equalTo(1));
	}
	
	@Test
	public void shouldReturnNullIfKeyNotInMap() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer retValue = map.get(0);
		
		assertThat(retValue, nullValue());
	}
	
	@Test
	public void shouldReturnValueIfElementWithIdenticKeyIsFirstInTheBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer key = new Integer(TEST_INTEGER1);
		Integer value = new Integer(TEST_INTEGER2);
		map.put(key, value);
		
		Integer retValue = map.get(key);
		
		assertThat(retValue, sameInstance(value));
	}
	
	@Test
	public void shouldReturnValueIfElementWithEqualKeyPresentInMap() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer key = new Integer(TEST_INTEGER1);
		Integer value = new Integer(TEST_INTEGER2);
		map.put(key, value);
		
		Integer key2 = new Integer(TEST_INTEGER1);
		
		Integer retValue = map.get(key2);
		
		assertThat(retValue, sameInstance(value));
	}
	
	@Test
	public void shouldReturnValueIfElementIsInTheBucketChain() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		int int2 = TEST_INTEGER1 + 1;
		
		int int1Pos = map.hash(TEST_INTEGER1) % map.capacity;
		while(int1Pos != map.hash(new Integer(int2)) % map.capacity)
			int2++;
		
		Integer i1 = new Integer(TEST_INTEGER1);
		map.put(i1, i1);
		
		Integer i2 = new Integer(int2);
		map.put(i2, i2);
		
		Integer retValue = map.get(i1);
		
		assertThat(retValue, sameInstance(i1));
	}
	
	@Test
	public void shouldReturnFalseWhenHashMapDoesntContainKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		boolean retValue = map.containsKey(0);
		
		assertFalse(retValue);
	}
	
	@Test
	public void shouldReturnTrueWhenHashMapContainsKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		map.put(i, TEST_INTEGER2);
		
		boolean retValue = map.containsKey(i);
		
		assertTrue(retValue);
	}
	
	@Test
	public void shouldPutValueForNullKeyToFirstElementInZeroBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		
		Integer retValue = map.put(null, i);
		
		assertThat(retValue, nullValue());
		assertThat(map.table[0], notNullValue());
		assertThat(map.table[0].value, sameInstance(i));
	}
	
	@Test
	public void shouldUpdateValueForNullKeyIfNullKeyElementIsPresent() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(TEST_INTEGER1);
		Integer i2 = new Integer(TEST_INTEGER2);
		
		map.put(null, i1);
		
		Integer retValue = map.put(null, i2);
		
		assertThat(retValue, sameInstance(i1));
		assertThat(map.table[0].value, sameInstance(i2));
	}
	
	@Test
	public void shouldPutValueForNullKeyWhenElementIsPresentInZeroBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(0);
		int hash = map.hash(i1);
		int index = map.evaluateIndex(hash);
		
		while(index != 0) {
			i1++;
			hash = map.hash(i1);
			index = map.evaluateIndex(hash);
		}
		
		map.put(i1, i1);
		
		Integer i2 = new Integer(TEST_INTEGER1);
		map.put(null, i2);
		
		HashMap.Entry<Integer, Integer> entry = map.table[0];
		while(entry != null && entry.key != null)
			entry = entry.next;
		
		assertThat(entry.key, nullValue());
		assertThat(entry.value, sameInstance(i2));
	}
	
	@Test
	public void shouldDoubleCapacityAndCreateNewExtendedTableWhenSizeReachesThresholdWhenPut() {
		HashMap<Integer, Integer> map = new HashMap<>();

		for(int i = 0; i < map.threshold; i++) {
			map.put(i, i);
		}
		
		assertThat(map.capacity, equalTo(HashMap.DEFAULT_INITIAL_CAPACITY));
		assertThat(map.threshold, equalTo(HashMap.DEFAULT_INITIAL_THRESHOLD));
		
		HashMap.Entry<Integer, Integer>[] oldTable = map.table;
		int borderValue = map.threshold;
		
		map.put(borderValue, borderValue);
		
		int newCapacity = HashMap.DEFAULT_INITIAL_CAPACITY << 1;
		int newThreshold = (int)Math.floor(newCapacity * map.loadFactor);
		
		assertThat(map.capacity, equalTo(newCapacity));
		assertThat(map.threshold, equalTo(newThreshold));
		
		assertThat(map.table.length, equalTo(newCapacity));
		assertThat(map.table, not(sameInstance(oldTable)));
	}
	
	@Test
	public void shouldTransferAllValuesToNewTableWhenSizeReachesCapacityWhenPut() {
		HashMap<Integer, Integer> map = new HashMap<>();
		List<Integer> items = prepareItemsListForTableExtensionTest(map);
		
		for(int i = 0; i < map.threshold; i++) {
			Integer item = items.get(i);
			map.put(item, item);
		}
		
		for(int i = 0; i < items.size() - 1; i++){
			assertThat(map.table[i].value, sameInstance(items.get(i)));
		}
		assertThat(map.table.length, equalTo(HashMap.DEFAULT_INITIAL_CAPACITY));
		assertThat(map.capacity, equalTo(HashMap.DEFAULT_INITIAL_CAPACITY));
		
		//Put one more value expecting map to be resized and elements rearranged
		Integer itemOverThreshold = items.get(items.size() - 1);
		map.put(itemOverThreshold, itemOverThreshold);
		
		for(int i = 0; i < items.size(); i++){
			assertThat(map.table[i].value, sameInstance(items.get(i)));
		}
	}

	/**
	 * Preparing items list to be put to map and layed out in a table in predictable way -
	 * consequently from 0 index up to threshold value before and after table extension
	 * 
	 * @param map
	 * @return
	 */
	private List<Integer> prepareItemsListForTableExtensionTest(HashMap<Integer, Integer> map) {
		List<Integer> items = new ArrayList<Integer>();
		
		for(int i = 0; i <= map.threshold; i++) {
			Integer item = new Integer(i);
			int hash = map.hash(item);
			int index = map.evaluateIndex(hash);
			int expectedIndex = hash % (HashMap.DEFAULT_INITIAL_CAPACITY << 1);
			while(index != i || expectedIndex != i) {
				item++;
				hash = map.hash(item);
				index = map.evaluateIndex(hash);
				expectedIndex = hash % (HashMap.DEFAULT_INITIAL_CAPACITY << 1);
			}
			items.add(item);
		}
		
		return items;
	}
	
	@Test
	public void shouldRemoveElementForKeyFromTableIfItIsFirstInBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		map.put(i, i);
		
		int hash = map.hash(i);
		int index = map.evaluateIndex(hash);
		
		map.remove(i);
		
		assertThat(map.table[index], nullValue());
	}
	
	@Test
	public void shouldRemoveElementForKeyIfItIsInTheBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(TEST_INTEGER1);
		int hash1 = map.hash(i1);
		int index1 = map.evaluateIndex(hash1);
		
		Integer i2 = new Integer(TEST_INTEGER1 + 1);
		int hash2 = map.hash(i2);
		int index2 = map.evaluateIndex(hash2);
		while(index1 != index2) {
			i2++;
			hash2 = map.hash(i2);
			index2 = map.evaluateIndex(hash2);
		}
		
		Integer i3 = new Integer(i2 + 1);
		int hash3 = map.hash(i3);
		int index3 = map.evaluateIndex(hash3);
		while(index1 != index3) {
			i3++;
			hash3 = map.hash(i3);
			index3 = map.evaluateIndex(hash3);
		}
		
		map.put(i1, i1);
		map.put(i2, i2);
		map.put(i3, i3);
		
		map.remove(i1);
		
		assertThat(map.table[index1].value, sameInstance(i3));
		assertThat(map.table[index1].next.value, sameInstance(i2));
		assertThat(map.table[index1].next.next, nullValue());
	}
	
	@Test
	public void shouldReturnValueWhenRemovingForKey() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i = new Integer(TEST_INTEGER1);
		map.put(i, TEST_INTEGER2);
		Integer retValue = map.remove(i);
		
		assertThat(retValue, sameInstance(TEST_INTEGER2));
	}
	
	@Test
	public void shouldReturnNullWhenRemovingForNonPresentKeyOnTopOfBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer retValue = map.remove(0);
		
		assertThat(retValue, nullValue());
	}
	
	@Test
	public void shouldReturnNullWhenRemovingForNonPresentKeyScanningThroughBucket() {
		HashMap<Integer, Integer> map = new HashMap<>();
		
		Integer i1 = new Integer(TEST_INTEGER1);
		int hash1 = map.hash(i1);
		int index1 = map.evaluateIndex(hash1);
		
		Integer i2 = new Integer(TEST_INTEGER1 + 1);
		int hash2 = map.hash(i2);
		int index2 = map.evaluateIndex(hash2);
		while(index1 != index2) {
			i2++;
			hash2 = map.hash(i2);
			index2 = map.evaluateIndex(hash2);
		}
		
		Integer i3 = new Integer(i2 + 1);
		int hash3 = map.hash(i3);
		int index3 = map.evaluateIndex(hash3);
		while(index1 != index3) {
			i3++;
			hash3 = map.hash(i3);
			index3 = map.evaluateIndex(hash3);
		}
		
		map.put(i1, i1);
		map.put(i2, i2);
		
		Integer retValue = map.remove(i3);
		
		assertThat(retValue, nullValue());
		
	}
	
	
}
