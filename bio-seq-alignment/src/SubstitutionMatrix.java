import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class SubstitutionMatrix {

	static int[][] subsMatrix = new int[24][24];

	public SubstitutionMatrix(String matrix) {
		try {
			readSubstMatrixFile("./matrices/" + matrix);
		} catch (IOException e) {
			System.out
					.println("The Substitution Matrix source file not found.");
		}
	}

	static int getIndex(String aa) {

		char aacid[] = aa.toCharArray();

		switch (aacid[0]) {
		case 'A':
			return 1;
		case 'R':
			return 2;
		case 'N':
			return 3;
		case 'D':
			return 4;
		case 'C':
			return 5;
		case 'Q':
			return 6;
		case 'E':
			return 7;
		case 'G':
			return 8;
		case 'H':
			return 9;
		case 'I':
			return 10;
		case 'L':
			return 11;
		case 'K':
			return 12;
		case 'M':
			return 13;
		case 'F':
			return 14;
		case 'P':
			return 15;
		case 'S':
			return 16;
		case 'T':
			return 17;
		case 'W':
			return 18;
		case 'Y':
			return 19;
		case 'V':
			return 20;
		case 'B':
			return 21;
		case 'Z':
			return 22;
		case 'X':
			return 23;
		case '*':
			return 24;
		}
		return 0;
	}

	public static int getScoreValue(String a1, String a2) {
		int i1 = getIndex(a1);
		int i2 = getIndex(a2);
		// System.out.println("**" + i1 + "--" + i2 + ": " +
		// subsMatrix[i1][i2]);
		return subsMatrix[i1][i2];
	}

	void readSubstMatrixFile(String filename) throws IOException {
		BufferedReader br = new BufferedReader(new FileReader(filename));
		String line = "";
		int rowIndex = 0;
		while ((line = br.readLine()) != null) {
			line = line.trim();
			if (line.startsWith("#") || line.trim().endsWith("*"))
				continue;
			line = line.replaceAll("  ", " ");
			String[] rows = line.split(" ");
			for (int colIndex = 1; colIndex < rows.length; colIndex++) {
				subsMatrix[rowIndex][colIndex - 1] = Integer
						.parseInt(rows[colIndex]);
			}
			rowIndex++;
		}
	}

	void print() {
		for (int i = 0; i < 24; i++) {
			for (int j = 0; j < 24; j++) {
				System.out.print(subsMatrix[i][j] + " ");
			}
			System.out.println("\n");
		}
	}
}
