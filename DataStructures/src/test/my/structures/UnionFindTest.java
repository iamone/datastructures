package my.structures;

import static org.hamcrest.core.IsEqual.equalTo;
import static org.hamcrest.core.IsSame.sameInstance;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.Map;

import my.structures.graph.Node;

import org.junit.Before;
import org.junit.Test;

public class UnionFindTest {
	
	
	Map<Integer, Node> nodes = new java.util.HashMap<Integer, Node>();
	
	
	@Before
	public void prepareTestData() {
		nodes.put(1, new Node(1));
		nodes.put(2, new Node(2));
		nodes.put(3, new Node(3));
		nodes.put(4, new Node(4));
		
	}
	
	@Test
	public void shouldCreateUnionFindWithElementsSetUpViaConstructor() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		assertEquals(uf.elementsMap.size(), nodes.size());
		
		for(Map.Entry<Integer, Node> e : nodes.entrySet())
			assertEquals(e.getValue(), uf.elementsMap.get(e.getKey()).data());
	}
	
	@Test
	public void shouldReturnFalseAndNotUnionIfSameNodePassedTwice() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		boolean result = uf.union(nodes.get(1), nodes.get(1));
		
		assertFalse(result);
	}
	
	@Test
	public void shouldAddElementToMapUsingNodeNumberAsKey() {
		UnionFind<Node> uf = new UnionFind<>();
		
		uf.add(nodes.get(1));
		
		assertThat(nodes.get(1), sameInstance(uf.elementsMap.get(nodes.get(1).getNum()).data()));
	}
	
	@Test
	public void shouldUnionTwoDistinctNodes() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		Node node1 = nodes.get(1);
		Node node2 = nodes.get(2);
		
		boolean result = uf.union(node1, node2);
		
		UnionFind<Node>.Element firstParent = uf.elementsMap.get(node1.getNum()).parent();
		UnionFind<Node>.Element secondParent = uf.elementsMap.get(node2.getNum()).parent();
		
		assertThat(firstParent, sameInstance(secondParent));

		assertTrue(result);
	}
	
	@Test
	public void shouldUnionTwoElementsIntoFirstIfItHasBiggerUnionSize() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		Node node1 = nodes.get(1);
		Node node2 = nodes.get(2);
		
		UnionFind<Node>.Element firstElement = uf.elementsMap.get(node1.getNum());
		UnionFind<Node>.Element secondElement = uf.elementsMap.get(node2.getNum());
		
		firstElement.setSize(secondElement.getSize() + 1);
		
		uf.union(node1, node2);
		
		assertThat(firstElement.parent(), sameInstance(firstElement));
		assertThat(secondElement.parent(), sameInstance(firstElement));
	}
	
	@Test
	public void shouldUnionTwoElementsIntoSecondIfFirstUnionSizeIsNotGreaterThanSecond() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		Node node1 = nodes.get(1);
		Node node2 = nodes.get(2);
		
		UnionFind<Node>.Element firstElement = uf.elementsMap.get(node1.getNum());
		UnionFind<Node>.Element secondElement = uf.elementsMap.get(node2.getNum());
		
		firstElement.setSize(2);
		secondElement.setSize(firstElement.getSize());
		
		uf.union(node1, node2);
		
		assertThat(firstElement.parent(), sameInstance(secondElement));
		assertThat(secondElement.parent(), sameInstance(secondElement));
		
	}
	
	@Test
	public void shouldSetFirstElementSizeToSumAndSecondElementSizeToZeroOnUnionIfFirstElementUnionIsBigger() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		Node node1 = nodes.get(1);
		Node node2 = nodes.get(2);
		
		UnionFind<Node>.Element firstElement = uf.elementsMap.get(node1.getNum());
		UnionFind<Node>.Element secondElement = uf.elementsMap.get(node2.getNum());
		
		firstElement.setSize(secondElement.getSize() + 1);
		
		uf.union(node1, node2);

		assertThat(secondElement.getSize(), equalTo(0));
		assertThat(firstElement.getSize(), equalTo(firstElement.getSize() + secondElement.getSize()));
	}
	
	@Test
	public void shouldSetSecondElementSizeToSumAndFirstElementSizeToZeroOnUnionIfFirstElementUnionIsNotBigger() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		Node node1 = nodes.get(1);
		Node node2 = nodes.get(2);
		
		UnionFind<Node>.Element firstElement = uf.elementsMap.get(node1.getNum());
		UnionFind<Node>.Element secondElement = uf.elementsMap.get(node2.getNum());
		
		secondElement.setSize(firstElement.getSize() + 1);
		
		uf.union(node1, node2);

		assertThat(firstElement.getSize(), equalTo(0));
		assertThat(secondElement.getSize(), equalTo(firstElement.getSize() + secondElement.getSize()));
	}
	
	@Test
	public void shouldFindElementParent() {
		UnionFind<Node> uf = new UnionFind<>(nodes.values());
		
		Node node1 = nodes.get(1);
		Node node2 = nodes.get(2);
		
		uf.union(node1, node2);
		
		Node parent1 = uf.find(node1);
		Node parent2 = uf.find(node2);
		
		assertThat(parent1, sameInstance(parent2));
	}
	

}
