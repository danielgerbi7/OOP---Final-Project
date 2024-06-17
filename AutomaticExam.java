package daniel_strasser_daniel_jerbi;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AutomaticExam implements Examable {

	private Question[] possibleQArray;
	private int TestQNum;

	public AutomaticExam(Question[] possibleQArray, int TestQNum) {
		this.possibleQArray = possibleQArray;
		this.TestQNum = TestQNum;
	}

	public void createExam(Repository rep) throws FileNotFoundException, IOException {

		Question[] qArray = new Question[TestQNum];
		String testString = "", solutionString = "", tmp = "";

		for (int i = 0; i < TestQNum; i++) {
			int randQ;
			boolean randQExists;
			do {
				randQExists = false;
				randQ = (int) (Math.random() * ((qArray.length)));
				for (int j = 0; j < qArray.length; j++) {
					if (possibleQArray[randQ] == qArray[j]) {
						randQExists = true;
					}
				}
			} while (randQExists == true);
			qArray[i] = possibleQArray[randQ];
			tmp = "Question #" + (i + 1) + ": " + qArray[i].getQuestionText() + "\n\n";
			solutionString += tmp;
			testString += tmp;

			System.out.println(qArray[i].getQuestionText());

			if (qArray[i] instanceof OpenQuestion) {
				OpenQuestion oq = (OpenQuestion) qArray[i];
				// testString += "Answer: " + rep.getOpenQuestionAnswerText(qArray[i] - 1) +
				// "\n\n";
				testString += "\n";
				solutionString += "Answer: " + oq.getAnswerText() + "\n\n";
			} else {
				MCQuestion mcq = (MCQuestion) qArray[i];
				int[] Answers = { -1, -1, -1, -1 };
				for (int l = 0; l < 4; l++) {
					int randA;
					boolean doubleTrue = false;
					boolean trueFlag = false;
					boolean randAExists;
					do {
						doubleTrue = false;
						randAExists = false;
						randA = (int) (Math.random() * ((mcq.getNumOfAnswers())));
						for (int m = 0; m < l; m++) {
							if (randA == Answers[m]) {
								randAExists = true;

							}

						}
						if (mcq.getAnswerValue(randA) == true && trueFlag == true) {
							doubleTrue = true;
							continue;
						}
						if (mcq.getAnswerValue(randA) == true) {
							trueFlag = true;
						}

					} while (randAExists == true || doubleTrue == true);
					Answers[l] = randA;
					tmp = "Answer #" + (l+1) + ": " + mcq.getAnswerText(Answers[l]);
					solutionString += tmp + " - " + mcq.getAnswerValue(Answers[l])  + "\n";
					testString += tmp + "\n";
					//System.out.println(Answers[l]);
				}
				String none = "true";
				for (int n = 0; n < 4; n++) {
					if (mcq.getAnswerValue(Answers[n]) == true) {
					none = "false";	
					}
				}
				solutionString += "Answer #5: None of the above - " + none + "\n";
				tmp = "\n";
				solutionString += tmp;
				testString += tmp;
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
	}
	
	@Override
	public String toString() {
		StringBuffer res = new StringBuffer();
		res.append("possibleQArray: " + possibleQArray.toString() + "\n");
		res.append("TestQNum: " + TestQNum + "\n");
		return res.toString();
	}
	
	@Override 
	public boolean equals(Object other) {
		if (!(other instanceof AutomaticExam)) {
			return false;
		}
		AutomaticExam a = (AutomaticExam)other;
		return a.possibleQArray == this.possibleQArray && a.TestQNum == this.TestQNum;
	}
}