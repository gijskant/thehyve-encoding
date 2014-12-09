package encoding;

import encoding.PairSequence.Element;

/**
 * Idea: build a tree with all the prefixes of the current history frame.
 * add should append all suffixes of its input to all the leaves of the tree
 * and remove the paths in the tree that have a begin point earlier than 
 * the the maximum lookback before the current point.
 * For removing the paths that are to long, we have to store the begin point
 * and length (or end point) of paths in the leave nodes. 
 * To iterate over the leave nodes, it is good to have
 * a separate collection (possibly an ordered list, ordered by begin point) of leave nodes.
 * To remove the entire path (and not only the leave nodes), we need to
 * also keep reverse edges in the tree, from a node to its predecessor.
 * 
 * Encoding a sequence of bytes is then kind of easy: find the longest path
 * that encodes the current sequence of bytes. There may be multiple (many?) paths
 * that match, though.  
 * @author kant
 */
public class TreeEncoder implements Encoder {

	@Override
	public boolean hasNext() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Element next() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void add(byte[] bytes) {
		// TODO Auto-generated method stub

	}

}
