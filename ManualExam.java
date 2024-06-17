package daniel_strasser_daniel_jerbi;

import java.io.File;

import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.io.IOException;

public class ManualExam implements Examable {

	private int MAX_ANSWERS;
	private int[] qArray;
	private boolean[][] aArray;
	private int numOfQuestions;
	private int TestQNum;
	private int numOfValid;

	public ManualExam(Repository rep) {

		Scanner s = new Scanner(System.in);
		numOfValid = rep.getValidQuestionsForExam().length;
		if (rep.repConditions()) { // there are questions and answers
			System.out.println("Type number of desired questions, 0 for menu");
			boolean isValidInput = false;
			while (!isValidInput) {
				try {
					int num = s.nextInt();
					setTestQNum(num, rep);
					isValidInput = true;
				} catch (TooManyQuestionsDeclared e) {
					System.out.print(e.getMessage());
					System.out.println(" Choose a number between 1 and " + numOfValid);
				} catch (TestQNumNotInRange e) {
					System.out.print(e.getMessage());
					System.out.println(" Choose a number between 1 and " + numOfValid);

				}
			}
			if (TestQNum == 0) {
				return;
			}

			this.MAX_ANSWERS = rep.getMaxAnswers();
			this.qArray = new int[TestQNum];
			this.aArray = new boolean[TestQNum][this.MAX_ANSWERS];

		} else {
			System.out.println("Repository doesn't contain enough data. Add questions or answers and try again.");
		}
	}

	public boolean isFull() {
		return (numOfQuestions == qArray.length);
	}

	public boolean noQuestions() {
		return (numOfQuestions == 0);
	}

	public boolean lessThanTestQNum() {
		return (numOfQuestions < qArray.length);
	}

	public int getNumOfQuestions() {
		return numOfQuestions;
	}

	public boolean questionPicked(int qNum) {
		for (int i = 0; i < numOfQuestions; i++) {
			if (qNum == qArray[i]) {
				return true;
			}
		}
		return false;
	}

	public boolean questionHasAnswers(int qNum) {
		for (int i = 0; i < MAX_ANSWERS; i++) {
			if (aArray[rowFromQNum(qNum)][i] == true) {
				return true;
			}
		}
		return false;
	}

	public void removeQuestion(int qNum) {
		for (int i = 0; i < numOfQuestions; i++) {
			if (qNum == qArray[i]) {
				qArray[i] = 0;
				numOfQuestions--;
				for (int j = i; j < numOfQuestions; j++) {
					qArray[j] = qArray[j + 1];
				}
				removeAnswersToQuestion(i);
				return;
			}
		}
	}

	public void removeAnswersToQuestion(int i) {
		for (int j = 0; j < MAX_ANSWERS; j++) {
			aArray[i][j] = false;
		}
	}

	public void addQuestion(int qNum) {
		qArray[numOfQuestions++] = qNum;
	}

	public void addAnswerToQuestion(int qNum, int aNum) {
		aArray[rowFromQNum(qNum)][aNum - 1] = true;
	}

	public int rowFromQNum(int qNum) {
		for (int i = 0; i < numOfQuestions; i++) {
			if (qArray[i] == qNum) {
				return i;
			}
		}
		return -1;
	}

	public boolean answerPicked(int qNum, int aNum) {
		if (aArray[rowFromQNum(qNum)][aNum - 1] == true) {
			return true;
		}
		return false;
	}

	public void removeAnswerFromMCQuestion(int qNum, int aNum) {
		aArray[rowFromQNum(qNum)][aNum - 1] = false;
		return;
	}

	public boolean empty() {
		for (int i = 0; i < aArray.length; i++) {
			for (int j = 0; j < aArray[i].length; j++) {
				if (aArray[i][j] == true) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean morethan1(int i, Repository rep) {
		boolean found = false;
		for (int j = 0; j < MAX_ANSWERS; j++) {
			if (aArray[i][j] == true) {
				if (rep.getMCAnswerValue(qArray[i] - 1, j) == true) {
					if (found == true) {
						return true;
					}
					found = true;
				}
			}
		}
		return false;
	}

	public boolean none(Repository rep, int n) {
		for (int i = 0; i < MAX_ANSWERS; i++) {
			if (aArray[n][i] == true) {
				if (rep.getMCAnswerValue(qArray[n] - 1, i) == true) {
					return false;
				}
			}
		}
		return true;
	}

	public boolean finalizeTest(Repository rep) throws FileNotFoundException, IOException {
		if (empty()) {
			return false;
		}
		String testString = "", solutionString = "", tmp = "";
		for (int i = 0; i < numOfQuestions; i++) {
			tmp = "Question #" + (i + 1) + ": " + rep.getQuestionText(qArray[i] - 1) + "\n";
			solutionString += tmp;
			testString += tmp;
			int aCount = 1;

			if (rep.isOpenQuestion(qArray[i])) {
				// testString += "Answer: " + rep.getOpenQuestionAnswerText(qArray[i] - 1) +
				// "\n\n";
				testString += "\n";
				solutionString += "Answer: " + rep.getOpenQuestionAnswerText(qArray[i] - 1) + "\n\n";
			}

			else {
				boolean moreThan1 = morethan1(i, rep);
				for (int j = 0; j < MAX_ANSWERS; j++) {
					if (aArray[i][j] == true) {
						tmp = "Answer #" + aCount + ": " + rep.getMCAnswerText(qArray[i] - 1, j);
						solutionString += tmp;
						testString += tmp;
						solutionString += " - " + (moreThan1 ? "false" : rep.getMCAnswerValue(qArray[i] - 1, j)) + "\n";
						testString += "\n";
						aCount++;
					}

				}

				tmp = "Answer #" + aCount++ + ": " + "More than one answer is correct";
				solutionString += tmp;
				testString += tmp;
				solutionString += " - " + moreThan1;
				tmp = "\nAnswer #" + aCount++ + ": " + "None of the above";
				solutionString += tmp;
				testString += tmp + "\n\n";
				solutionString += " - " + none(rep, i) + "\n\n";
			}
		}
		LocalDateTime today = LocalDateTime.now();
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy_MM_dd_hh_mm");
		File testFile = new File("exam_" + today.format(dtf) + ".txt");
		File solutionFile = new File("solution_" + today.format(dtf) + ".txt");
		PrintWriter pwt = new PrintWriter(testFile);
		PrintWriter pws = new PrintWriter(solutionFile);
		pwt.print(testString);
		pws.print(solutionString);
		pwt.close();
		pws.close();
		return true;
	}

	public void createExam(Repository rep) throws FileNotFoundException, IOException {

		Scanner s = new Scanner(System.in);

		int aChoice = -1, qNum = -2;
		while (qNum != -1 && qNum != 0) {

			System.out.println(
					"Choose question. To finalize type -1, to return to menu type 0. To remove a picked question, choose it again.");
			System.out.println(rep.questionsToString());
			boolean isValidInput = false;
			while (!isValidInput) {
				try {
					qNum = s.nextInt();
					checkQNum(qNum, rep);
					isValidInput = true;
				} catch (LessThan4Answers e) {
					System.out.print(e.getMessage());
				} catch (InvalidQNumException e) {
					System.out.println(e.getMessage());
				}
			}

			if (qNum == 0) {
				return;
			}

			aChoice = -2;
			if (qNum != -1) {

				if (this.questionPicked(qNum)) {
					this.removeQuestion(qNum);
					System.out.println("Question removed successfully.");
				} else {
					if (this.isFull()) {
						System.out.println("Question number exceeded. Please start over with more questions.");
						return;
					} else {
						if (!rep.isEmptyQuestion(qNum)) {
							this.addQuestion(qNum);
						} else {
							System.out.println("Question is empty");

						}
					}
					if (rep.isEmptyQuestion(qNum)) {
					} else {
						if (rep.isOpenQuestion(qNum)) {
							this.addAnswerToQuestion(qNum, 1);
							System.out.println("Question added successfully.");
						} else {
							while (aChoice != 0 && aChoice != -1) {
								System.out.println(
										"Choose answer(s). To remove a picked answer, choose it again. To remove question, type -1. To return to questions type 0.");
								System.out.println(rep.questionAnswersToString(qNum));
								aChoice = s.nextInt();

								while (!rep.isValidAnswerToQuestion(qNum, aChoice)) {
									System.out.println(
											"Invalid Answer number. Try again or type 0 to return to questions.");
									aChoice = s.nextInt();
								}
								if (aChoice != 0) {
									if (aChoice == -1) {
										this.removeQuestion(qNum);
										System.out.println("Question removed successfully.");
									} else if (this.answerPicked(qNum, aChoice)) {
										this.removeAnswerFromMCQuestion(qNum, aChoice);
										System.out.println("Answer removed successfully.");
									} else {
										this.addAnswerToQuestion(qNum, aChoice);
										System.out.println("Answer added successfully.");
									}
								} else {
									if (!this.questionHasAnswers(qNum)) {
										this.removeQuestion(qNum);
									}
								}
							}
						}
					}
				}
			} else {
				if (this.noQuestions()) {
					System.out.println("No questions chosen. returning to menu.");
					return;
				} else if (this.lessThanTestQNum()) {
					System.out.println("Only " + this.getNumOfQuestions()
							+ " question chosen. To finalize choose -1, 0 to return to questions");
					qNum = s.nextInt();
					while (qNum != 0 && qNum != -1) {
						System.out.println("Invalid answer. Try again:");
					}

					if (qNum == 0) {
						qNum = -2;
					} else {
						System.out.println("Finalizing:");
						this.finalizeTest(rep);
						System.out.println("Done");
					}
				} else {
					System.out.println("Finalizing:");
					this.finalizeTest(rep);
					System.out.println("Done");
				}
			}
		}
	} // else {
//
//			System.out.println("Repository doesn't contain enough data. Add questions or answers and try again.");
//		}

	public void setTestQNum(int num, Repository rep) throws TooManyQuestionsDeclared, TestQNumNotInRange {
		if (num > 10) {
			throw new TooManyQuestionsDeclared();
		} else if (num < 0 || num > numOfValid) {
			throw new TestQNumNotInRange();
		} else {
			TestQNum = num;
		}
	}

	public boolean checkQNum(int num, Repository rep) throws LessThan4Answers, InvalidQNumException {
		if (num == 0 || num == -1) {
			return true;
		}
		Question q = rep.getQuestion(num);
		if (q instanceof MCQuestion) {
			if (((MCQuestion) q).getNumOfAnswers() < 4) {
				throw new LessThan4Answers();
			}
		} else if (!rep.isValidQuestionNum(num)) {
			throw new InvalidQNumException();
		}
		return true;
	}

	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("MAX ANSWERS: " + MAX_ANSWERS + "\n");
		res.append("qArray: " + qArray.toString() + "\n");
		res.append("aArray: " + aArray.toString() + "\n");
		res.append("numOfQuestions: " + numOfQuestions + "\n");
		res.append("TestQNum: " + TestQNum);
		res.append("numOfValid: " + numOfValid);
		return res.toString();
	}

	@Override
	public boolean equals(Object other) {
		if (!(other instanceof ManualExam)) {
			return false;
		}
		ManualExam m = (ManualExam) other;
		return m.MAX_ANSWERS == this.MAX_ANSWERS && m.qArray.equals(this.qArray) && m.aArray.equals(this.aArray)
				&& m.numOfQuestions == this.numOfQuestions && m.TestQNum == this.TestQNum
				&& m.numOfValid == this.numOfValid;
	}
}
