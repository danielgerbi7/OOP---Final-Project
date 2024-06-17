package daniel_strasser_daniel_jerbi;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Question implements Serializable {

	
	private static int qSerialCounter;
	protected int MAX_ANSWERS = 10;
	protected String text;
	protected int qSerialNum;
	protected eDifficulty difficulty;
	
	public enum eDifficulty {
		easy, medium, hard
	};
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("MAX_ANSWERS: " + MAX_ANSWERS + "\n");
		res.append("text: " + text + "\n");
		res.append("qSerialNum: " + qSerialNum);
		res.append("eDifficulty: " + difficulty.name());
		return res.toString();
	}
	
	@Override
	public boolean equals(Object other) {
		if (!(other instanceof Question)) {
			return false;
		}
		Question q = (Question)other;
		return q.MAX_ANSWERS == this.MAX_ANSWERS && q.text.equals(this.text) && q.qSerialNum == this.qSerialNum && q.difficulty == this.difficulty;
	}
	
	public Question(String text) {
		this.text = text;
	}
	
	public Question(String text, eDifficulty eDif) {
		this(text);
		this.difficulty = eDif;
		qSerialNum = ++qSerialCounter;
	}
	
	public Question(String text, String difText) {
		this(text, eDifficulty.valueOf(difText));
	}

	public int getQSerialNum() {
		return qSerialNum;
	}
	
	public String getQuestionText() {
		return text;
	}
	
	public eDifficulty getEDif() {
		return difficulty;
	}
	public void setEDifficulty(String text) {
		this.difficulty = eDifficulty.valueOf(text);
	}
	
}
