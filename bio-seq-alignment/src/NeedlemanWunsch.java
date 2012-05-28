import java.util.ArrayList;

public class NeedlemanWunsch {
	Cell[][] matrix;
	Cell[][] x;
	Cell[][] y;

	int m, n, match, mismatch, gap;
	int d, e;
	boolean useSubstitutionMatrix = false;

	int maxI = 0, maxJ = 0, maxValue = 0;

	public NeedlemanWunsch(int m, int n, int mch, int mmch, int gp, boolean useSubMat) {
		matrix = new Cell[m][n];
		x = new Cell[m][n];
		y = new Cell[m][n];

		this.m = m;
		this.n = n;
		this.match = mch;
		this.mismatch = mmch;
		this.gap = gp;

		this.useSubstitutionMatrix = useSubMat;
		matrix[0][0] = new Cell(0, null);
		x[0][0] = new Cell(0, null);
		y[0][0] = new Cell(0, null);
	}

	public NeedlemanWunsch(LinearGap mat) {
		matrix = mat.matrix;
		this.m = mat.m;
		this.n = mat.n;
		this.match = mat.match;
		this.mismatch = mat.match;
		this.gap = mat.gap;

		this.useSubstitutionMatrix = mat.useSubstitutionMatrix;
		matrix[0][0] = new Cell(0, null);
	}

	public void allignGlobally(ArrayList<String> sequences) {
		initGlobal();
		populateMatrix(sequences);
		// printMatrix();
		printSequences(backtrack(sequences));
	}

	private void printSequences(String seqs) {
		String[] alignedSeqs = seqs.split(";");		
		System.out.println("\n" + alignedSeqs[0] + "\n" + alignedSeqs[1]);
	}

	private String backtrack(ArrayList<String> sequences) {
		char[] seq0 = sequences.get(0).toUpperCase().toCharArray();
		char[] seq1 = sequences.get(1).toUpperCase().toCharArray();

		int i = this.m - 1;
		int j = this.n - 1;

		String s0 = "", s1 = "";

		// System.out.println("\nBacktrack sequence:");
		while (true) {
			if (matrix[i][j].pointer == null)
				break;

			// System.out.print("(" + matrix[i][j].value + ")");
			if (matrix[i][j].pointer.equals("\\")) {
				s0 = seq0[i - 1] + s0;
				s1 = seq1[j - 1] + s1;
				i--;
				j--;
			} else if (matrix[i][j].pointer.equals("^")) {
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
		for (int i = 1; i < this.m; i++) {
			matrix[i][0] = new Cell(i * this.gap, "^");
		}
		for (int j = 1; j < this.n; j++) {
			matrix[0][j] = new Cell(j * this.gap, "<");
		}
	}
	
	void populateMatrix(ArrayList<String> sequences) {
		char[] seq0 = sequences.get(0).toUpperCase().toCharArray();
		char[] seq1 = sequences.get(1).toUpperCase().toCharArray();

		for (int i = 1; i < this.m; i++) {
			for (int j = 1; j < this.n; j++) {
				matrix[i][j] = populateCell(matrix[i][j - 1],
						matrix[i - 1][j - 1], matrix[i - 1][j], seq0[i - 1]
								+ "", seq1[j - 1] + "");				
			}
		}
	}

	private Cell populateCell(Cell cellLeft, Cell cellDiag, Cell cellUp,
			String s0, String s1) {

		int dScore, lScore, uScore;

		if (useSubstitutionMatrix)
			dScore = cellDiag.value + SubstitutionMatrix.getScoreValue(s0, s1);
		else {
			if (s0.equals(s1)) {
				dScore = cellDiag.value + this.match;
			} else
				dScore = cellDiag.value + this.mismatch;
		}
		lScore = cellLeft.value + this.gap;
		uScore = cellUp.value + this.gap;

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
		for (int i = 0; i < m; i++) {
			for (int j = 0; j < n; j++) {
				System.out.print(matrix[i][j].value + "\t");
			}
			System.out.print("\n");
		}
	}
}
