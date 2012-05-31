import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Align.java
 * 
 * The main Root program.
 * 
 * @author Gopi
 */
public class Align {
	static String input = "";
	static String matName = "PAM250";
	static double match = 5, mismatch = -3, gap = -4, open = -12, extend = -4;
	static boolean useSubsMat = false, useAffine = false;
	static String alignType = "";

	public static void main(String args[]) {

		new SubstitutionMatrix(matName);

		Sequences seqs = new Sequences();

		try {
			readConfig();
		} catch (FileNotFoundException e) {
			System.out
					.println("Cound not find the configuratin file: 'run.config'");
		} catch (IOException e) {
			System.out.println("Cound read the config file: 'run.config'");
		}

		seqs.readSequences(input);
		// seqs.printSequences();

		if (!useAffine) {
			ScoreMatrix mat = new ScoreMatrix(
					seqs.sequences.get(0).length() + 1, seqs.sequences.get(1)
							.length() + 1, match, mismatch, gap, useSubsMat);

			if (alignType.equals("global")) {
				NeedlemanWunschLinear nw0 = new NeedlemanWunschLinear(mat);
				nw0.allignGlobally(seqs.sequences);
			} else {
				SmithWatermanLinear sw0 = new SmithWatermanLinear(mat);
				sw0.allignLocally(seqs.sequences);
			}
		} else {
			ScoreMatrix mat1 = new ScoreMatrix(
					seqs.sequences.get(0).length() + 1, seqs.sequences.get(1)
							.length() + 1, match, mismatch, open, extend,
					useSubsMat);

			if (alignType.equals("global")) {
				NeedlemanWunschAffine nw = new NeedlemanWunschAffine(mat1);
				nw.allignGlobally(seqs.sequences);
			} else {
				SmithWatermanAffine sw = new SmithWatermanAffine(mat1);
				sw.allignLocally(seqs.sequences);
			}
		}
		System.out.println("\n\n--End of Run--");
	}

	private static void readConfig() throws IOException {
		BufferedReader br = new BufferedReader(new FileReader("run.config"));
		String line = "";

		while ((line = br.readLine()) != null) {

			if (line.startsWith("#"))
				continue;
			if (line.startsWith("#Comments"))
				break;

			if (line.startsWith("input-sequence-file")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					input = opts1[1].trim();
				}
			} else if (line.startsWith("alignment-type")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					alignType = opts1[1].trim();
				}
			} else if (line.startsWith("substitution-matrix")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					matName = opts1[1].trim();
				}
			} else if (line.startsWith("use-substitution-matrix")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					if (opts1[1].trim().equals("yes"))
						useSubsMat = true;
					else
						useSubsMat = false;
				}
			} else if (line.startsWith("use-affine-gap-penalty")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					if (opts1[1].trim().equals("yes"))
						useAffine = true;
					else
						useAffine = false;
				}
			} else if (line.startsWith("match")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					match = Double.parseDouble(opts1[1].trim());
				}
			} else if (line.startsWith("mismatch")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					mismatch = Double.parseDouble(opts1[1].trim());
				}
			} else if (line.startsWith("gap")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					gap = Double.parseDouble(opts1[1].trim());
				}
			} else if (line.startsWith("open")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					open = Double.parseDouble(opts1[1].trim());
				}
			} else if (line.startsWith("extend")) {
				String[] opts1 = line.split(":");
				if (opts1.length == 2) {
					extend = Double.parseDouble(opts1[1].trim());
				}
			}
		}
	}

	public static void main2(String args[]) {
		String matName = "PAM250";
		double match = 5, mismatch = -3, gap = -4, open = -12, extend = -4;
		boolean useSubsMat = false;

		System.out.println("Input file: " + args[0]);
		new SubstitutionMatrix(matName);

		Sequences seqs = new Sequences();
		seqs.readSequences(args[0]);
		// seqs.printSequences();
		ScoreMatrix mat = new ScoreMatrix(seqs.sequences.get(0).length() + 1,
				seqs.sequences.get(1).length() + 1, match, mismatch, gap,
				useSubsMat);

		NeedlemanWunschLinear nw0 = new NeedlemanWunschLinear(mat);
		nw0.allignGlobally(seqs.sequences);

		SmithWatermanLinear sw0 = new SmithWatermanLinear(mat);
		sw0.allignLocally(seqs.sequences);

		ScoreMatrix mat1 = new ScoreMatrix(seqs.sequences.get(0).length() + 1,
				seqs.sequences.get(1).length() + 1, match, mismatch, open,
				extend, useSubsMat);

		NeedlemanWunschAffine nw = new NeedlemanWunschAffine(mat1);
		nw.allignGlobally(seqs.sequences);

		SmithWatermanAffine sw = new SmithWatermanAffine(mat1);
		sw.allignLocally(seqs.sequences);

		System.out.println("\n\n--End of Run--");
	}
}
