package encoding;

import java.util.ArrayDeque;
import java.util.Iterator;

/**
 * Iterator over the bytes encoded by a sequence of pairs of the form
 * (x, y), where the sequence of bytes encoded by (x, y) is 
 * the singleton [y] if x==0 or the y elements from the output sequence 
 * starting at x elements back.  
 * @author kant
 */
public class PairSequence implements Iterator<byte[]> {

	public static class InvalidLookback extends Exception {
		private static final long serialVersionUID = -8058495951305723752L;
		
		public InvalidLookback() {
			super();
		}
		
		public InvalidLookback(String message) {
			super(message);
		}
	}
	
	public static class History {
		final int capacity;
		ArrayDeque<Byte> sequence;
		
		/**
		 * Creates a history for a stream of bytes with a bounded capacity.
		 * When more bytes are read, the oldest ones are lost. 
		 * @param capacity the capacity of the history.
		 */
		public History(int capacity) {
			this.capacity = capacity;
			this.sequence = new ArrayDeque<Byte>();
		}
		
		/**
		 * Adds an array of bytes to the history and removes as many
		 * of the oldest elements from the history as needed to prevent
		 * exceeding the capacity.
		 * FIXME: No smart things are done when the lenght of bytes
		 * exceeds the capacity of the history.
		 * @param bytes The bytes that are added to the history
		 */
		public void add(byte[] bytes) {
			for (int i=0; i < bytes.length; i++) {
				if (sequence.size() == capacity) {
					sequence.pollLast();
				}
				sequence.push(bytes[i]);
			}
		}
		
		public byte[] lookback(short offset, short number) throws InvalidLookback {
			if (offset > sequence.size()) {
				throw new InvalidLookback(String.format("Invalid pair: (%d, %d)", offset, number));
			}
			byte[] result = new byte[number];
			int start = sequence.size() - offset;
			int i = 0;
			int j = 0;
			/* Iterate over a bytes in the sequence, as ArrayDeque has no random access
			 * get method. Which one is faster depends on the use; if writes (additions)
			 * are frequent, this is probably faster than using, e.g., an ArrayList. 
			 */
			Iterator<Byte> it = sequence.descendingIterator();
			while(it.hasNext()) {
				byte b = it.next();
				if (i >= start) {
					result[j] = b;
					j++;
				}
				i++;
			}
			return result;
		}
	}
	
	public static interface Element {
		public byte[] decode(History history) throws InvalidLookback;
		public byte[] bytes();
	}
	
	public static class Character implements Element {
		byte character;
		
		public Character(byte character) {
			this.character = character;
		}
		
		public byte[] decode(History history) {
			return new byte[] {character};
		}
		
		public byte[] bytes() {
			return new byte[] {0, character};
		}
	}

	public static class Lookback implements Element {
		short offset;  
		short number;
		
		/**
		 * A lookback element. 
		 * @param offset the number of bytes to go back in history.
		 * @param number the number of bytes to read forward from there.
		 * @throws InvalidLookback if offset < number.
		 */
		public Lookback(byte offset, byte number) throws InvalidLookback {
			// interpret signed bytes as unsigned numbers
			this.offset = (short) (offset & 0xFF);
			this.number = (short) (number & 0xFF);
			if (this.offset < this.number) {
				throw new InvalidLookback(String.format("Invalid pair: (%d [%d], %d [%d])", 
						this.offset, offset, this.number, number));
			}
		}

		public byte[] decode(History history) throws InvalidLookback {
			return history.lookback(offset, number);
		}
		
		public byte[] bytes() {
			return new byte[] {(byte)offset, (byte)number};
		}
	}

	History history;
	ElementReader reader;
	
	public PairSequence(ElementReader reader) {
		int capacity = 1 << Byte.SIZE; // 2^8
		this.history = new History(capacity);
		this.reader = reader;
	}

	public boolean hasNext() {
		return (reader.hasNext());
	}

	public byte[] next() {
		Element e = reader.next();
		if (e == null) {
			return new byte[] {0x3f};
		}
		byte[] result;
		try {
			result = e.decode(history);
		} catch (InvalidLookback e1) {
			//System.out.printf("Error in input, invalid pair: %s.\n", e1.getMessage());
			//return null;
			return new byte[] {0x3f};
		}
		history.add(result);
		return result;
	}

}
