import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class Sequences {
	ArrayList<String> sequences = new ArrayList<String>();

	void readSequences(String filename) {
		BufferedReader seqReader = null;
		try {
			seqReader = new BufferedReader(new FileReader(filename));
		} catch (FileNotFoundException e) {
			System.out.println("The sequences source file: " + filename
					+ ", was not found!");
		}
		String line = "";
		String seq = "";

		try {
			while ((line = seqReader.readLine()) != null) {
				if (line.startsWith(">")) {
					seq = "";
					continue;
				}

				if (line.equals("")) {
					sequences.add(seq.trim());
					seq = "";
				}

				seq += line.trim();
			}
			sequences.add(seq);
		} catch (IOException e) {
			System.out.println("Could'nt read the sequence source file: "
					+ filename);
		}
	}

	void printSequences() {
		System.out.println("Original Sequences:");
		for (int index = 0; index < sequences.size(); index++) {
			System.out.println(sequences.get(index));
		}
	}
}
