package encoding;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;

import encoding.PairSequence.Element;

public class SmartEncoder implements Encoder {

	static final int MAX_LOOKBACK = 1 << Byte.SIZE;
	ArrayDeque<Byte> sequence;
	Queue<Element> elements;
	
	public SmartEncoder() {
		sequence = new ArrayDeque<Byte>();
		elements = new ArrayDeque<Element>();
	}
	
	/** 
	 * Iterate over at most MAX_LOOKBACK bytes and encode them
	 * FIXME: for efficiency, we should store prefixes in some map
	 */
	protected void generateElements() {
		ArrayList<Byte> bytes = new ArrayList<Byte>();
		int n = 0;
		while (n < MAX_LOOKBACK && !sequence.isEmpty()) {
			byte b = sequence.poll();
			// Still using the trivial approach
			Element e = new PairSequence.Character(b);
			elements.add(e);
		}
		// FIXME: The smart approach is not finished yet.
		// sketch of a more advanced one:
		Iterator<Byte> it1 = bytes.iterator();
		Iterator<Byte> it2 = bytes.iterator();
		while (it1.hasNext()) {
			byte b1 = it1.next();
			// while b1 is ahead of b2
			// and their values match:
			//   try to increment both
			// when there is no match anymore, we have a recipe for a lookback element...
		}
		
	}
	
	public boolean hasNext() {
		if (elements.isEmpty()) {
			generateElements();
		}
		return (!elements.isEmpty());
	}

	public Element next() {
		if (elements.isEmpty()) {
			generateElements();
		}
		return elements.poll();
	}

	
	public void add(byte[] bytes) {
		for (int i=0; i < bytes.length; i++) {
			if (sequence.size() == MAX_LOOKBACK) {
				generateElements();
			}
			sequence.push(bytes[i]);
		}
	}

}
