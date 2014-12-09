package encoding;

import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Queue;

import encoding.PairSequence.Element;
import encoding.PairSequence.InvalidLookback;
import encoding.PairSequence.Lookback;

public class SmartEncoder implements Encoder {

	static final int MAX_LOOKBACK = 1 << Byte.SIZE;
	ArrayDeque<Byte> history;
	ArrayDeque<Byte> todo;
	Queue<Element> elements;
	
	public SmartEncoder() {
		history = new ArrayDeque<Byte>();
		todo = new ArrayDeque<Byte>();
		elements = new ArrayDeque<Element>();
	}
	
	/** 
	 * Move number elements from todo to history.
	 * @param number
	 */
	protected void commit(int number) {
		for (int i=0; i < number; i++) {
			if (history.size() == MAX_LOOKBACK) {
				history.pollLast();
			}
			history.push(todo.pollLast());
		}
	}
	
	/** 
	 * Iterate over at most MAX_LOOKBACK bytes and encode them
	 * FIXME: for efficiency, we should store prefixes in some map
	 */
	protected void generateElements() {
		ArrayDeque<Byte> bytes = new ArrayDeque<Byte>();
		int n = 0;
		Iterator<Byte> todo_it = todo.descendingIterator();
		while (n < MAX_LOOKBACK && todo_it.hasNext()) {
			byte b = todo_it.next();
			bytes.add(b);
			// Still using the trivial approach
			//Element e = new PairSequence.Character(b);
			//elements.add(e);
			
			n++;
		}
		// Looking for the most recent common pattern
		while (!bytes.isEmpty()) {
			byte b1 = bytes.pop();
			//System.out.printf("Looking for a match for %d\n", b1);
			ArrayDeque<Byte> subsequence = new ArrayDeque<Byte>();
			int offset = 1;
			Iterator<Byte> it = history.iterator();
			boolean lookupSuccess = false;
			while (!lookupSuccess && it.hasNext()) {
				byte b2 = it.next();
				//System.out.printf("(Considering %d (offset: %d)\n", b2, offset);
				if (b1 != b2) {
					subsequence.push(b2);
					offset++;
				} else {
					//System.out.printf("MATCH FOUND: %d (offset: %d)\n", b2, offset);
					// At this point, the current character b1 is equal to
					// the element offset positions back in history.
					// We have recorded the history from there in subsequence.
					// Start a forward iteration.
					int number = 1;
					while (!subsequence.isEmpty() && bytes.peek() == subsequence.peek()) {
						bytes.pop();
						subsequence.pop();
						number++;
					}
					if (number > 1) {
						try {
							elements.add(new Lookback((byte)offset, (byte)number));
							commit(number);
							lookupSuccess = true;
						} catch (InvalidLookback e1) {
							System.out.println("Error in encoding.");
						}
					}
				}
			}
			if (!lookupSuccess) {
				elements.add(new PairSequence.Character(b1));
				commit(1);
			}
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
			if (todo.size() == MAX_LOOKBACK) {
				generateElements();
			}
			todo.push(bytes[i]);
		}
	}

}
