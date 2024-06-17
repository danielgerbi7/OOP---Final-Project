// realize getNumOfQuestions using Question method and remove 
// Repository's private int numOfQuestions

package daniel_strasser_daniel_jerbi;

import java.io.Serializable;
import java.util.Arrays;

import daniel_strasser_daniel_jerbi.Question.eDifficulty;

@SuppressWarnings("serial")
public class Repository implements Serializable {

	private final int MAX_ANSWERS = 10;
	private String className;
	private Question[] qArray;
	private int numOfQuestions;
	private Answer[] aArray;
	private int numOfAnswers;

	
	public Repository(int qArraySize, String className) {
		qArray = new Question[qArraySize];
		aArray = new Answer[qArraySize * MAX_ANSWERS];
		this.className = className;
	}


	public Answer getAnswer(int aNum) {
		return aArray[aNum - 1];
	}

	public Question getQuestion(int qNum) {
		return qArray[qNum - 1];
	}

	public boolean QuestionExists(String text) {
		for (int i = 0; i < numOfQuestions; i++) {
			if (text.equals(qArray[i].getQuestionText())) {
				return true;
			}
		}
		return false;
	}

	public boolean qArrayFull() {
		return (numOfQuestions == qArray.length);
	}

	public void extendArrays() {
		qArray = Arrays.copyOf(qArray, qArray.length + 1);
		aArray = Arrays.copyOf(aArray, aArray.length + MAX_ANSWERS);
	}

	public MCQuestion addQuestion(String text, Question.eDifficulty eDif) {
		if (QuestionExists(text)) {
			return null;
		}
		if (qArrayFull()) {
			extendArrays();
		}
		qArray[numOfQuestions] = new MCQuestion(text, eDif);
		return (MCQuestion) qArray[numOfQuestions++];
	}

	public OpenQuestion addQuestion(String qText, String aText, eDifficulty eDif) {
		if (QuestionExists(qText)) {
			return null;
		}
		if (qArrayFull()) {
			extendArrays();
		}
		Answer a = isAnswer(aText);
		if (a != null) {
			qArray[numOfQuestions] = new OpenQuestion(qText, a, eDif);
		} else {
			aArray[numOfAnswers++] = new Answer(aText);
			qArray[numOfQuestions] = new OpenQuestion(qText, aText, eDif);
		}
		return (OpenQuestion) qArray[numOfQuestions++];
	}

	public OpenQuestion addQuestion(String qText, int aChoice, eDifficulty eDif) {
		return addQuestion(qText, aArray[aChoice].toString(), eDif);
	}
	public boolean isEnumVal(String text) {
		for (int i = 0; i < Question.eDifficulty.values().length; i++) {
			if (text.equals(Question.eDifficulty.values()[i].name())) {
				return true;
			}
		}
		return false;
	}

	public boolean canAddAnswerToQNum(int qNum) {
		if (getQuestion(qNum) instanceof OpenQuestion) {
			return false;
		} else {
			return ((MCQuestion) qArray[qNum - 1]).canAddAnswer();
		}
	}

	public String WhyCannotAdd(int qNum) {
		if (qArray[qNum - 1] instanceof MCQuestion) {
			return "Question already has " + MAX_ANSWERS + " answers. Choose another or type 0 to return to menu.";
		}
		return "This open question already has an answer. Remove it and try again.";
	}

	public Answer isAnswer(String aText) {
		for (int i = 0; i < numOfAnswers; i++) {
			if (aText.equals(aArray[i].toString())) {
				return aArray[i];
			}
		}
		return null;
	}

	public void addAnswerToOpenQuestionFromRep(int qNum, int aNum) {
		((OpenQuestion) qArray[qNum - 1]).addAnswer(getAnswer(aNum));
	}

	public void addAnswerToOpenFromText(int qNum, String aText) {
		Question q = qArray[qNum - 1];
		Answer a = isAnswer(aText);
		if (a == null) {
			a = addAnswer(aText);
		}
		((OpenQuestion) q).addAnswer(a);
	}

	public void addAnswerToMCFromText(int qNum, String aText, boolean v) {
		Question q = qArray[qNum - 1];
		Answer a = isAnswer(aText);
		if (a == null) {
			a = addAnswer(aText);
		}
		((MCQuestion) q).addAnswer(a, v);
	}

	public void addAnswerToMCQuestionFromRep(int aNum, int qNum, boolean value) {

		MCQuestion q = (MCQuestion) getQuestion(qNum);
		Answer a = getAnswer(aNum);
		q.addAnswer(a, value);
	}
	
	public boolean qNumHasAnswerByText(int qNum, String text) {
		MCQuestion mCQ = (MCQuestion) qArray[qNum - 1];
		for (int i = 0; i < mCQ.getNumOfAnswers(); i++) {
			if (mCQ.isAnswerFromText(text)) {
				return true;
			}
		}
		return false;
	}

	public boolean qNumHasAnswerFromRep(int qNum, int a) {
		MCQuestion mCQ = (MCQuestion) qArray[qNum - 1];
		for (int i = 0; i < mCQ.getNumOfAnswers(); i++) {
			if (mCQ.isAnswer(aArray[a - 1])) {
				return true;
			}
		}
		return false;
	}

	public boolean isEmptyQuestion(int qNum) {
		if (qNum < 1 || qNum > numOfQuestions) {
			return false;
		}
		Question q = qArray[qNum - 1];
		if (q instanceof OpenQuestion) {
			return (((OpenQuestion) q).getAnswer() == null);
		}
		return (((MCQuestion) qArray[qNum - 1]).getNumOfAnswers() == 0);
	}

	public boolean isOpenQuestion(int qNum) {
		if (qArray[qNum - 1] instanceof OpenQuestion) {
			return true;
		}
		return false;
	}

	public Answer addAnswer(String aText) {
		Answer a = new Answer(aText);
		aArray[numOfAnswers++] = a;
		return a;
	}


	public boolean isQuestionByText(String text) {
		for (int i = 0; i < numOfQuestions; i++) {
			if (text.equals(qArray[i].getQuestionText())) {
				return true;
			}
		}
		return false;
	}

	public String MCQuestionsToString() {
		StringBuffer res = new StringBuffer();
		int MCCounter = 0;
		for (int i = 0; i < numOfQuestions; i++) {
			if (qArray[i] instanceof OpenQuestion) {
				continue;
			} else {
				MCCounter++;
				res.append("Question #" + MCCounter + ": " + qArray[i].getQuestionText() + "\n");
			}
		}
		return res.toString();
	}

	public String questionsToString() {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < numOfQuestions; i++) {
			res.append("Question #" + (i + 1) + ": " + qArray[i].getQuestionText() + "\n");
		}
		return res.toString();
	}

	// public String MCQuestionsToString() {

	// }

	public String allAnswersToString() {
		StringBuffer res = new StringBuffer();
		for (int i = 0; i < numOfAnswers; i++) {
			res.append("Answer #" + (i + 1) + ": " + aArray[i].toString() + "\n");
		}
		return res.toString();
	}

	public String questionAnswersToString(int n) {
		StringBuffer res = new StringBuffer();
		MCQuestion mCQ = (MCQuestion) qArray[n - 1];
		for (int i = 0; i < mCQ.getNumOfAnswers(); i++) {
			res.append((i + 1) + ". " + mCQ.getAnswerText(i) + " - " + mCQ.getAnswerValue(i) + "\n");
		}
		return res.toString();

	}

	public boolean isValidQuestionNum(int qNum) {
		return (qNum >= -1 && qNum <= numOfQuestions);
	}

	public boolean isValidAnswerToQuestion(int qNum, int aNum) {
		return (aNum >= -1 && aNum <= getNumOfAnswersByQNum(qNum));
	}

	public int getNumOfQuestions() {
		return numOfQuestions;
	}

	public int getNumOfAnswers() {
		return numOfAnswers;
	}

	public int getNumOfAnswersByQNum(int qNum) {
		return ((MCQuestion) qArray[qNum - 1]).getNumOfAnswers();
	}

	public String getQuestionText(int q) {
		return qArray[q].getQuestionText();
	}

	public String getMCAnswerText(int q, int a) {
		return ((MCQuestion) qArray[q]).getAnswerText(a);
	}

	public String getOpenQuestionAnswerText(int q) {
		return ((OpenQuestion) qArray[q]).getAnswerText();
	}

	public boolean getMCAnswerValue(int q, int a) {
		return ((MCQuestion) qArray[q]).getAnswerValue(a);
	}

	public int getMaxAnswers() {
		return MAX_ANSWERS;
	}

	public Question[] getValidQuestionsForExam() {

		Question[] arr = new Question[0];
		int res = 0;
		for (int i = 0; i < qArray.length; i++) {
			if (qArray[i] instanceof OpenQuestion) {
				arr = Arrays.copyOf(arr, arr.length + 1);
				arr[res++] = qArray[i];
			} else {
				MCQuestion mcq = (MCQuestion) qArray[i];
				if (mcq.getNumOfAnswers() < 4) {
					continue;
				}
				int falseCounts = 0;
				for (int j = 0; j < mcq.getNumOfAnswers(); j++) {
					if (mcq.getAnswerValue(j) == false) {
						falseCounts++;
					}

				}
				if (falseCounts >= 3) {

					arr = Arrays.copyOf(arr, arr.length + 1);
					arr[res++] = qArray[i];
				}
			}
		}
		return arr;
	}

	public void removeAnswerFromMCQuestion(int qNum, int aNum) {
		((MCQuestion) qArray[qNum - 1]).removeAnswer(aNum);
	}

	public void removeAnswerFromOpenQuestion(int qNum) {
		((OpenQuestion) qArray[qNum - 1]).removeAnswer();
	}

	public void removeQuestion(int qNum) {
		if (qArray[qNum - 1] instanceof MCQuestion) {
			((MCQuestion) qArray[qNum - 1]).removeAllAnswers();
		} else if (qArray[qNum - 1] instanceof OpenQuestion) {
			((OpenQuestion) qArray[qNum - 1]).removeAnswer();
		}
		qArray[qNum - 1] = null;
		numOfQuestions--;
		for (int i = qNum - 1; i < numOfQuestions; i++) {
			qArray[i] = qArray[i + 1];
		}

	}

	public boolean repConditions() {
		if (numOfQuestions == 0) {
			return false;
		} else {
			for (int i = 0; i < numOfQuestions; i++) {
				if (qArray[i] instanceof OpenQuestion) {
					continue;
				} else {
					if (((MCQuestion) qArray[i]).getNumOfAnswers() == 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	public boolean noQuestions() {
		return (numOfQuestions == 0);
	}
	
	public String getClassName() {
		return className;
	}
	
	@Override 
	public boolean equals(Object other) {
		if (!(other instanceof Repository)) {
			return false;
		}
		Repository r = (Repository)other;
		return r.className == this.className && r.qArray.equals(this.qArray) && r.numOfAnswers == this.numOfQuestions && r.aArray.equals(this.aArray) && r.numOfAnswers == this.numOfAnswers; 
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("Class name: " + className + "\n\n");
		for (int i = 0; i < numOfQuestions; i++) {
			res.append("Question #" + (i + 1) + ": " + qArray[i].getQuestionText() + " [difficulty: "
					+ qArray[i].getEDif() + ", serial# " + qArray[i].getQSerialNum() + "]\n");
			if (qArray[i] instanceof OpenQuestion) {
				if (!isEmptyQuestion(i + 1)) {
					res.append("Answer: " + ((OpenQuestion) qArray[i]).getAnswerText() + "\n\n");
				} else {
					res.append("\n");
				}
			} else {
				MCQuestion mCQ = (MCQuestion) qArray[i];
				for (int j = 0; j < mCQ.getNumOfAnswers(); j++) {
					res.append("Answer #" + (j + 1) + ": " + mCQ.getAnswer(j).toString() + " - " + mCQ.getAnswerValue(j)
							+ "\n");
				}
				res.append("\n");
			}
		}
		return res.toString();
	}


	
//	public void removeData() {
//		for (int i=0; i<numOfQuestions; i++) {
//			for (int j=0; j < qArray[i].get)
//		}
	}
