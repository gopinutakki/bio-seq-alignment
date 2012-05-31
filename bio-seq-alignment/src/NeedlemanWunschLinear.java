import java.util.ArrayList;

public class NeedlemanWunschLinear {
	ScoreMatrix scoreMat;

	public NeedlemanWunschLinear(ScoreMatrix mat) {
		this.scoreMat = mat;
	}

	public void allignGlobally(ArrayList<String> sequences) {
		initGlobal();
		populateMatrix(sequences);
		// printMatrix();
		printSequences(backtrack(sequences));
	}

	private void printSequences(String seqs) {
		String[] alignedSeqs = seqs.split(";");
		String mToken = "";
		for (int i = 0; i < alignedSeqs[0].length(); i++)
			if (alignedSeqs[0].substring(i, i+1).equals(alignedSeqs[1].substring(i, i+1)))
				mToken += "|";
			else
				mToken += " ";
		
		System.out.println("\n\nGlobally aligned sequences (linear):");
		System.out.println("\n" + alignedSeqs[0] + "\n" + mToken + "\n" + alignedSeqs[1]);
	}

	private String backtrack(ArrayList<String> sequences) {
		char[] seq0 = sequences.get(0).toUpperCase().toCharArray();
		char[] seq1 = sequences.get(1).toUpperCase().toCharArray();

		int i = this.scoreMat.m - 1;
		int j = this.scoreMat.n - 1;

		String s0 = "", s1 = "";

		while (true) {
			if (scoreMat.matrix[i][j].pointer == null)
				break;

			if (scoreMat.matrix[i][j].pointer.equals("\\")) {
				s0 = seq0[i - 1] + s0;
				s1 = seq1[j - 1] + s1;
				i--;
				j--;
			} else if (scoreMat.matrix[i][j].pointer.equals("^")) {
				s0 = seq0[i - 1] + s0;
				s1 = "-" + s1;
				i--;
			} else {
				s0 = "-" + s0;
				s1 = seq1[j - 1] + s1;
				j--;
			}
		}
		return s0 + ";" + s1;
	}

	void initGlobal() {
		for (int i = 1; i < this.scoreMat.m; i++) {
			scoreMat.matrix[i][0] = new Cell(i * this.scoreMat.gap, "^");
		}
		for (int j = 1; j < this.scoreMat.n; j++) {
			scoreMat.matrix[0][j] = new Cell(j * this.scoreMat.gap, "<");
		}
	}

	void populateMatrix(ArrayList<String> sequences) {
		char[] seq0 = sequences.get(0).toUpperCase().toCharArray();
		char[] seq1 = sequences.get(1).toUpperCase().toCharArray();

		for (int i = 1; i < this.scoreMat.m; i++) {
			for (int j = 1; j < this.scoreMat.n; j++) {
				scoreMat.matrix[i][j] = populateCell(scoreMat.matrix[i][j - 1],
						scoreMat.matrix[i - 1][j - 1],
						scoreMat.matrix[i - 1][j], seq0[i - 1] + "",
						seq1[j - 1] + "");
			}
		}
	}

	private Cell populateCell(Cell cellLeft, Cell cellDiag, Cell cellUp,
			String s0, String s1) {

		double dScore, lScore, uScore;

		if (scoreMat.useSubstitutionMatrix) {
			dScore = cellDiag.value + SubstitutionMatrix.getScoreValue(s0, s1);
		} else {
			if (s0.equals(s1)) {
				dScore = cellDiag.value + this.scoreMat.match;
			} else {
				dScore = cellDiag.value + this.scoreMat.mismatch;
			}
		}
		lScore = cellLeft.value + this.scoreMat.gap;
		uScore = cellUp.value + this.scoreMat.gap;

		if (dScore >= uScore) {
			if (dScore >= lScore)
				return new Cell(dScore, "\\");
			else
				return new Cell(lScore, "<");
		} else {
			if (uScore >= lScore)
				return new Cell(uScore, "^");
			else
				return new Cell(lScore, "<");
		}
	}

	void printMatrix() {
		System.out.println("\nMatrix:");
		for (int i = 0; i < scoreMat.m; i++) {
			for (int j = 0; j < scoreMat.n; j++) {
				System.out.print(scoreMat.matrix[i][j].value + "\t");
			}
			System.out.print("\n");
		}
	}
}
