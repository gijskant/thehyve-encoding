package encoding;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import encoding.PairSequence.Element;

public class Read {

	public static void main(String[] args) {
		InputStream input = System.in;
		ElementReader reader = new ElementReader(input);
		PairSequence sequence = new PairSequence(reader);
		boolean useTrivial = false;
		Map<String, String> env = System.getenv();
        if (env.containsKey("USE_TRIVIAL_IMPLEMENTATION")) {
        	String value = env.get("USE_TRIVIAL_IMPLEMENTATION");
        	try {
	        	int intValue = Integer.parseInt(value);
	        	if (intValue == 1) {
	        		useTrivial = true;
	        	}
        	} catch (Exception e) {
        		// skip
        	}
        }
        Encoder encoder;
        if (useTrivial) {
        	encoder = new SimpleEncoder();
        } else {
        	encoder = new SmartEncoder();
        }
		while (sequence.hasNext()) {
			byte[] bytes = sequence.next();
			try {
				System.out.write(bytes);
			} catch (IOException e) {
				System.out.println("Error writing to stdout.");
			}
			encoder.add(bytes);
			while (encoder.hasNext()) {
				Element e = encoder.next();
				if (e == null) {
					System.out.println("Error.");
					break;
				}
				try {
					System.err.write(e.bytes());
				} catch (IOException e1) {
					System.out.println("Error writing to stderr.");
				}
			}
		}
	}

}
