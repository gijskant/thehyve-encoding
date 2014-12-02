package encoding;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;

import encoding.PairSequence.Character;
import encoding.PairSequence.Element;
import encoding.PairSequence.InvalidLookback;
import encoding.PairSequence.Lookback;

public class ElementReader implements Iterator<Element> {

	InputStream input;
	byte[] buffer;
	boolean error;
	
	public ElementReader(InputStream input) {
		this.input = input;
		buffer = new byte[2];
		error = false;
	}
	
	protected Element read() throws IOException, InvalidLookback {
		input.read(buffer, 0, 2);
		if (buffer[0] == 0) {
			return new Character(buffer[1]);
		} else {
			return new Lookback(buffer[0], buffer[1]);
		}
	}
	
	public boolean hasNext() {
		if (error) {
			return false;
		}
		try {
			return input.available() > 0;
		} catch (IOException e) {
			error = true;
			System.out.println("Error reading from stream.");
			return false;
		}
	}

	public Element next() {
		try {
			return read();
		} catch (IOException e) {
			error = true;
			System.out.println("Error reading from stream.");
			return null;
		} catch (InvalidLookback e) {
			error = true;
			System.out.printf("Error in input: %s.\n", e.getMessage());
			return null;
		}
	}
	
}
