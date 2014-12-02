package encoding;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import encoding.PairSequence.Element;

public class EncodingTest {

	@Test
	public void testSimpleStream() {
		System.out.println("testSimpleStream");
		byte[] buffer = new byte[] {0, 1, 0, 2, 0, 3, 0, 4, 0, 5};
		ByteArrayInputStream input = new ByteArrayInputStream(buffer);
		ElementReader reader = new ElementReader(input);
		PairSequence sequence = new PairSequence(reader);
		byte i = 0;
		while (sequence.hasNext()) {
			byte[] bytes = sequence.next();
			for (byte b: bytes) {
				i++;
				System.out.printf("byte: %d\n", b);
				assertSame(i, b);
			}
		}
	}

	@Test
	public void testLookbackStream() {
		System.out.println("testLookbackStream");
		byte[] buffer = new byte[] {0, 1, 0, 2, 0, 3, 3, 3};
		ByteArrayInputStream input = new ByteArrayInputStream(buffer);
		ElementReader reader = new ElementReader(input);
		PairSequence sequence = new PairSequence(reader);
		byte i = 0;
		while (sequence.hasNext()) {
			byte[] bytes = sequence.next();
			for (byte b: bytes) {
				i++;
				System.out.printf("byte: %d\n", b);
				assertSame(i, b);
				if (i == 3) {
					i = 0;
				}
			}
		}
	}
	
	@Test
	public void testSimpleEncoding() {
		System.out.println("testSimpleEncoding");
		byte[] bytes = new byte[] {1, 2, 3, 4};
		Encoder encoder = new SimpleEncoder();
		encoder.add(bytes);
		byte i = 1;
		while (encoder.hasNext()) {
			Element e = encoder.next();
			byte[] output = e.bytes();
			assertSame(2, output.length);
			if (output.length == 2) {
				System.out.printf("Output: (%d, %d)\n", output[0], output[1]);
				assertSame((byte)0, output[0]);
				assertSame(i, output[1]);
			}
			i++;
		}
	}

	
	
}
