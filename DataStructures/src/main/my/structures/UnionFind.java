package my.structures;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import my.structures.graph.Node;



/**
 * 
 * 
 * @author mone
 *
 * @param <T>
 */
public class UnionFind<T extends Node>{
	
	Map<Integer, Element> elementsMap = new HashMap<Integer, Element>();
	
	public UnionFind(Collection<? extends T> collection) {
		
		for(T item : collection) {
			elementsMap.put(item.getNum(), new Element(item));
		}
	
	}
	
	public UnionFind() {
	}
	
	public void add(T item) {
		elementsMap.put(item.getNum(), new Element(item));
	}
	
	public void clear() {
		elementsMap.clear();
	}
	
	public int size() {
		return elementsMap.size();
	}
	
	public boolean union(T item1, T item2) {
		Element e1parent = elementsMap.get(item1.getNum());
		Element e2parent = elementsMap.get(item2.getNum());
		Element e1 = e1parent;
		Element e2 = e2parent;
		
		while(e1parent.parent() != e1parent)
			e1parent = e1parent.parent();
		while(e2parent.parent() != e2parent)
			e2parent = e2parent.parent();
		
		e1.setParent(e1parent);
		e2.setParent(e2parent);
		
		if(e1parent == e2parent)
			return false;
		
		if(e1parent.getSize() > e2parent.getSize()) {
			e1parent.setSize(e1parent.getSize() + e2parent.getSize());
			e2parent.setSize(0);
			e2parent.setParent(e1parent);
		} else {
			e2parent.setSize(e1parent.getSize() + e2parent.getSize());
			e1parent.setSize(0);
			e1parent.setParent(e2parent);
		}
		return true;
	}
	
	public T find(T item) {
		Element e = elementsMap.get(item.getNum());
		
		while(e != e.parent())
			e = e.parent();
		
		return e.data();
	}
	
	
	class Element {
		private T data;
		private Element parent;
		private int size = 1;
		
		public Element(T data) {
			this.data = data;
			parent = this;
		}
		public Element parent() {
			return parent;
		}
		public void setParent(Element parent) {
			this.parent = parent;
		}
		public T data() {
			return data;
		}
		public int getSize() {
			return size;
		}
		public void setSize(int size) {
			this.size = size;
		}
		@Override
		public String toString() {
			return "data=" + data + " ";
		}
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + getOuterType().hashCode();
			result = prime * result + ((data == null) ? 0 : data.hashCode());
			result = prime * result + size;
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			@SuppressWarnings("unchecked")
			Element other = (Element) obj;
			if (!getOuterType().equals(other.getOuterType()))
				return false;
			if (data == null) {
				if (other.data != null)
					return false;
			} else if (!data.equals(other.data))
				return false;
			if (size != other.size)
				return false;
			return true;
		}
		@SuppressWarnings("rawtypes")
		private UnionFind getOuterType() {
			return UnionFind.this;
		}
		
		
	}

	@Override
	public String toString() {
		return "UnionFind [" + elementsMap + "]";
	}

}
