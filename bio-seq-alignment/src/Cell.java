/**
 * Class to hold the matrices cell information.
 * 
 * @author gopi
 *
 */
public class Cell {
	// Value to store the cell values, pointer to the backtrack.
	double value = 0;
	// Possible values for pointer are: \, ^, < and (m, i, d in the front of \, ^, < for affine)
	String pointer = "";

	public Cell(double val, String ptr) {
		this.value = val;
		this.pointer = ptr;
	}
}
