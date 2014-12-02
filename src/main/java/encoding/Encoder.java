package encoding;

import java.util.Iterator;

import encoding.PairSequence.Element;

/**
 * Encodes a byte stream into a sequence of encoding pairs 
 * @author kant
 *
 */
public interface Encoder extends Iterator<Element>{

	public void add(byte[] bytes);
	
}
