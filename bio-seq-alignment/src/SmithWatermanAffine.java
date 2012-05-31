import java.util.ArrayList;

/**
 * Local alignment with Affine gap penalty.
 * @author gopi
 *
 */
public class SmithWatermanAffine {
	ScoreMatrix scoreMat;
	Cell maxCell = new Cell(0, null);

	public SmithWatermanAffine(ScoreMatrix mat) {
		this.scoreMat = mat;
	}

	/**
	 * Initialize the scoring matrices, Ix, Iy equivalents.
	 * @param sequences
	 */
	public void allignLocally(ArrayList<String> sequences) {
		initLocal();
		populateMatrix(sequences);
		// printMatrix();
		printSequences(backtrack(sequences));
	}

	/**
	 * Print sequences after populating.
	 * @param seqs
	 */
	private void printSequences(String seqs) {
		String[] alignedSeqs = seqs.split(";");		
		String mToken = "";
		for (int i = 0; i < alignedSeqs[0].length(); i++)
			if (alignedSeqs[0].substring(i, i+1).equals(alignedSeqs[1].substring(i, i+1)))
				mToken += "|";
			else
				mToken += " ";
		System.out.println("\n\nLocally aligned sequences (affine):");
		System.out.println("\n" + alignedSeqs[0] + "\n" + mToken + "\n" + alignedSeqs[1]);
	}

	/**
	 * Backtrack the matrices after populating.
	 * @param sequences
	 * @return
	 */
	private String backtrack(ArrayList<String> sequences) {
		char[] seq0 = sequences.get(0).toUpperCase().toCharArray();
		char[] seq1 = sequences.get(1).toUpperCase().toCharArray();

		int i = this.scoreMat.maxI;
		int j = this.scoreMat.maxJ;

		String s0 = "", s1 = "";

		while (true) {
			if (this.maxCell.pointer == null || this.maxCell.value == 0)
				break;

			if (this.maxCell.pointer.contains("\\")) {
				s0 = seq0[i - 1] + s0;
				s1 = seq1[j - 1] + s1;
				i--;
				j--;
			} else if (this.maxCell.pointer.contains("^")) {
				s0 = seq0[i - 1] + s0;
				s1 = "-" + s1;
				i--;
			} else {
				s0 = "-" + s0;
				s1 = seq1[j - 1] + s1;
				j--;
			}
			if (this.maxCell.pointer.contains("m"))
				this.maxCell = this.scoreMat.matrix[i][j];
			else if (this.maxCell.pointer.contains("i"))
				this.maxCell = this.scoreMat.I[i][j];
			else if (this.maxCell.pointer.contains("d"))
				this.maxCell = this.scoreMat.D[i][j];
		}
		return s0 + ";" + s1;
	}

	/**
	 * Initialize the matrices.
	 */
	void initLocal() {
		for (int i = 1; i < this.scoreMat.m; i++) {
			for (int j = 1; j < this.scoreMat.n; j++) {
				this.scoreMat.matrix[i][j] = new Cell(0, null);
			}
		}
		for (int i = 1; i < this.scoreMat.m; i++) {
			this.scoreMat.matrix[i][0] = new Cell(0, null);
			this.scoreMat.I[i][0] = new Cell((this.scoreMat.d + this.scoreMat.e
					* (i)), null);
			this.scoreMat.D[i][0] = new Cell(Double.NEGATIVE_INFINITY, null);
		}
		for (int j = 1; j < this.scoreMat.n; j++) {
			this.scoreMat.matrix[0][j] = new Cell(0, null);
			this.scoreMat.I[0][j] = new Cell(Double.NEGATIVE_INFINITY, null);
			this.scoreMat.D[0][j] = new Cell((this.scoreMat.d + this.scoreMat.e
					* (j)), null);
		}
	}

	/**
	 * Populate the matrices.
	 * @param sequences
	 */
	void populateMatrix(ArrayList<String> sequences) {
		char[] seq0 = sequences.get(0).toUpperCase().toCharArray();
		char[] seq1 = sequences.get(1).toUpperCase().toCharArray();

		for (int i = 1; i < this.scoreMat.m; i++) {
			for (int j = 1; j < this.scoreMat.n; j++) {
				this.scoreMat.I[i][j] = populateCellI(
						this.scoreMat.matrix[i - 1][j],
						this.scoreMat.I[i - 1][j]);

				this.scoreMat.D[i][j] = populateCellD(
						this.scoreMat.matrix[i][j - 1],
						this.scoreMat.I[i][j - 1]);

				this.scoreMat.matrix[i][j] = populateCell(
						this.scoreMat.D[i - 1][j - 1],
						this.scoreMat.matrix[i - 1][j - 1],
						this.scoreMat.I[i - 1][j - 1], seq0[i - 1] + "",
						seq1[j - 1] + "");

				if (this.scoreMat.I[i][j].value > this.scoreMat.D[i][j].value
						&& this.scoreMat.I[i][j].value > this.scoreMat.matrix[i][j].value) {
					this.maxCell = this.scoreMat.I[i][j];
					this.scoreMat.maxI = i;
					this.scoreMat.maxJ = j;
				} else if (this.scoreMat.D[i][j].value > this.scoreMat.matrix[i][j].value) {
					this.maxCell = this.scoreMat.D[i][j];
					this.scoreMat.maxI = i;
					this.scoreMat.maxJ = j;
				} else {
					this.maxCell = this.scoreMat.matrix[i][j];
					this.scoreMat.maxI = i;
					this.scoreMat.maxJ = j;
				}
			}
		}
	}

	private Cell populateCellD(Cell M, Cell D) {
		double m = M.value - this.scoreMat.d;
		double d = D.value - this.scoreMat.e;

		if (m > d)
			return new Cell(m, "m<");
		return new Cell(d, "d<");

	}

	private Cell populateCellI(Cell M, Cell I) {
		double m = M.value - this.scoreMat.d;
		double i = I.value - this.scoreMat.e;

		if (m > i)
			return new Cell(m, "m^");
		return new Cell(i, "i^");
	}

	private Cell populateCell(Cell D, Cell M, Cell I, String s0, String s1) {

		double dScore, lScore, uScore;
		double s, d, m, i;

		if (this.scoreMat.useSubstitutionMatrix)
			s = SubstitutionMatrix.getScoreValue(s0, s1);
		else {
			if (s0.equals(s1)) {
				s = this.scoreMat.match;
			} else
				s = this.scoreMat.mismatch;
		}

		uScore = D.value + s;
		dScore = M.value + s;
		lScore = I.value + s;

		if (dScore <= 0 && lScore <= 0 && uScore <= 0)
			return new Cell(0, null);

		if (dScore >= uScore) {
			if (dScore >= lScore)
				return new Cell(dScore, "m\\");
			else
				return new Cell(lScore, "i\\");
		} else {
			if (uScore >= lScore)
				return new Cell(uScore, "d\\");
			else
				return new Cell(lScore, "i\\");
		}
	}

	void printMatrix() {
		System.out.println("\nMatrix:");
		for (int i = 0; i < this.scoreMat.m; i++) {
			for (int j = 0; j < this.scoreMat.n; j++) {
				System.out.print(this.scoreMat.I[i][j].pointer + "\t");
			}
			System.out.print("\n");
		}
	}
}
