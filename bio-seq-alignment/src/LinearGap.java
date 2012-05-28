import java.util.ArrayList;

public class LinearGap {
	Cell[][] matrix;
	
	int m, n, match, mismatch, gap;
	int d, e;
	boolean useSubstitutionMatrix = false;

	int maxI = 0, maxJ = 0, maxValue = 0;

	public LinearGap(int m, int n, int mch, int mmch, int gp) {
		matrix = new Cell[m][n];
	
		this.m = m;
		this.n = n;
		this.match = mch;
		this.mismatch = mmch;
		this.gap = gp;

		matrix[0][0] = new Cell(0, null);		
	}

	public LinearGap(int m, int n, int md, int me, boolean useSubMat) {
		matrix = new Cell[m][n];
	
		this.m = m;
		this.n = n;
		this.d = md;
		this.e = me;
		
		this.useSubstitutionMatrix = useSubMat;
		matrix[0][0] = new Cell(0, null);		
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
