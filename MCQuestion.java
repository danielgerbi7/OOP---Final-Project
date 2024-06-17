package daniel_strasser_daniel_jerbi;

import java.io.Serializable;

@SuppressWarnings("serial")
public class MCQuestion extends Question implements Serializable {

	private Answer[] aArray;
	private boolean[] values;
	private int numOfAnswers;

	public MCQuestion(String text, eDifficulty dif) {
		super(text, dif);
		this.aArray = new Answer[MAX_ANSWERS];
		this.values = new boolean[MAX_ANSWERS];
	}

	public String getAnswersText() {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < numOfAnswers; i++) {
			res.append("Answer #" + (i + 1) + aArray[i].toString() + "\n");
		}
		return res.toString();
	}

	public Answer getAnswer(int aNum) {
		return aArray[aNum];
	}

	public String getAnswerText(int aNum) {
		return aArray[aNum].toString();
	}

	public void removeAnswer(int aNum) {
		aArray[aNum - 1] = null;
		numOfAnswers--;
		for (int i = aNum - 1; i < numOfAnswers; i++) {
			aArray[i] = aArray[i + 1];
		}

	}

	public void removeAllAnswers() {
		for (int i = 0; i < numOfAnswers; i++) {
			aArray[i] = null;
		}
		numOfAnswers = 0;
	}

	public boolean getAnswerValue(int aNum) {
		return values[aNum];
	}

	public int getNumOfAnswers() {
		return numOfAnswers;
	}

	public boolean canAddAnswer() {
		return (!(numOfAnswers == aArray.length));
	}

	public boolean isAnswer(Answer answer) {
		for (int i = 0; i < numOfAnswers; i++) {
			if (aArray[i] == answer) {
				return true;
			}
		}
		return false;
	}

	public boolean isAnswerFromText(String text) {
		for (int i = 0; i < numOfAnswers; i++) {
			if (text.equals(aArray[i].toString())) {
				return true;
			}
		}
		return false;
	}

	public void addAnswer(Answer a, boolean v) {
		aArray[numOfAnswers] = a;
		values[numOfAnswers++] = v;
	}

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append(super.toString());
		res.append("aArray: " + aArray.toString() + "\n");
		res.append("values: " + values.toString() + "\n");
		res.append("numOfAnswers: " + numOfAnswers);
		return res.toString();
	}

	@Override
	public boolean equals(Object other) {

		if (!(other instanceof MCQuestion)) {
			return false;
		}
		if (!super.equals(other)) {
			return false;
		}
		MCQuestion m = (MCQuestion) other;
		return m.aArray.equals(this.aArray) && m.values.equals(this.values) && m.numOfAnswers == this.numOfAnswers;
	}

}