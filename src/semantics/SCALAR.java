package semantics;

public class SCALAR extends TYPE {
  private final int min;
  private final int max;

  public SCALAR(int min, int max) {
	this.min = min;
	this.max = max;
  }
}
