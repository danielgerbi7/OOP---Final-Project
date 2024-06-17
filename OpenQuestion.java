package daniel_strasser_daniel_jerbi;

@SuppressWarnings("serial")
public class OpenQuestion extends Question {

	private Answer answer;

	
	public OpenQuestion(String qText, String aText, eDifficulty eDif) {
		super(qText, eDif);
		this.answer = new Answer(aText);
	}
	
	public OpenQuestion(String qText, Answer a, eDifficulty eDif) {
		super(qText, eDif);
		this.answer = a;
	}

	public String getAnswerText() {
		return answer.toString();
	}

	public Answer getAnswer() {
		return answer;
	}

	public void removeAnswer() {
		answer = null;
	}

//	public boolean canAddAnswer() {
//		return (answer == null);
//	}

	public void addAnswer(Answer a) {
		answer = a;
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append(super.toString());
		res.append("Answer: " + answer);
		return res.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof OpenQuestion)) {
			return false;
		}
		if (!super.equals(other)) {
			return false;
		}
		OpenQuestion o = (OpenQuestion)other;
		return o.answer.equals(this.answer);
	}
	
}
