/**
 * Align.java
 * 
 * The main Root program.
 * @author Gopi
 */
public class Align {
	public static void main(String args[]) {
		Sequences seqs = new Sequences();
		seqs.readSequences("sequences2.txt");		
		//seqs.printSequences();
		Matrix mat = new Matrix(seqs.sequences.get(0).length()+1, seqs.sequences
				.get(1).length()+1, 8, -3, -4);		
		mat.allignGlobally(seqs.sequences);
		mat.allignLocally(seqs.sequences);
	}
}
