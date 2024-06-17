package daniel_strasser_daniel_jerbi;

public class TooManyQuestionsDeclared extends Exception {
	
	public TooManyQuestionsDeclared() {
		super("Maximum number of allowed questions is 10.");
	}
}