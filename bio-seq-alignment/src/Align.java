/**
 * Align.java
 * 
 * The main Root program.
 * 
 * @author Gopi
 */
public class Align {
	public static void main(String args[]) {
		boolean useSubMatrix = true;
		String matName = "PAM250";
		new SubstitutionMatrix(matName);

		Sequences seqs = new Sequences();
		seqs.readSequences("sequences2.txt");
		// seqs.printSequences();
		Matrix mat = new Matrix(seqs.sequences.get(0).length() + 1,
				seqs.sequences.get(1).length() + 1, 1, -1, -1, useSubMatrix);
		mat.allignGlobally(seqs.sequences);
		mat.allignLocally(seqs.sequences);
	}
}
