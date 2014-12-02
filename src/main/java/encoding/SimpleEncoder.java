package encoding;

import java.util.ArrayDeque;
import java.util.Queue;

import encoding.PairSequence.Element;

public class SimpleEncoder implements Encoder {

	Queue<Element> elements;

	public SimpleEncoder() {
		elements = new ArrayDeque<Element>();
	}
	
	public void add(byte[] bytes) {
		for (byte b: bytes) {
			elements.add(new PairSequence.Character(b));
		}
	}

	public boolean hasNext() {
		return !elements.isEmpty();
	}

	public Element next() {
		return elements.poll();
	}
	
	
}
