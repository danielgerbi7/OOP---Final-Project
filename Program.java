package daniel_strasser_daniel_jerbi;

import java.io.FileNotFoundException;
import java.util.Scanner;

import daniel_strasser_daniel_jerbi.Question.eDifficulty;

import java.io.IOException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.ObjectInputStream;

public class Program {

	public static void printOpening() {
		System.out.println(
				"Welcome to the test management system. Please choose of the following (or type 0 for exit) :\n");

		System.out.println("1) Show classes");
		System.out.println("2) Add new class");
		System.out.println("3) Remove class");
	}

	public static int printClasses(Sys sys, Scanner s) throws FileNotFoundException, IOException {
		System.out.println("Please choose subject: (or 0 to exit):\n ");
		System.out.println(sys.classesToString());
		int classnum = s.nextInt();
		while (!sys.validClassNum(classnum) && classnum != 0) {
			System.out.println(
					"Invalid number. Please enter a number between 1 and " + sys.getNumOfReps() + " (or 0 for menu): ");
			classnum = s.nextInt();
		}
		if (classnum == 0) {
			exit(sys);
		}
		return classnum;
	}

	public static void printMenu() {
		System.out.println("Choose option:");

		System.out.println("1)  Show all questions and corresponding answers");
		System.out.println("2)  Add new answer to an existing question");
		System.out.println("3)  Add new question");
		System.out.println("4)  Remove answer to a question");
		System.out.println("5)  Remove question and its answers");
		System.out.println("6)  Create new test");
		System.out.println("7)  Back to class menu");
		System.out.println("0)  Exit");
	}

	public static void addNewClass(Sys sys, Scanner s) {
		System.out.println("Enter class name, or 0 for menu:");
		s.nextLine();
		String name = s.nextLine();
		while (sys.classExists(name) && !(name.equals("0"))) {
			System.out.println("Class already exists. Please try again: ");
			name = s.nextLine();
		}
		if (name.equals("0")) {
			return;
		}
		sys.addRep(new Repository(1, name));
		System.out.println("Class added successfully.");
	}

	public static void removeClass(Sys sys, Scanner s) {
		System.out.println("Choose class to remove (or 0 for menu): ");
		System.out.println(sys.classesToString());
		int classnum = s.nextInt();
		while (classnum < 0 || classnum > sys.getNumOfReps()) {
			System.out.println(
					"Invalid number: please choose beteween 1 and " + sys.getNumOfReps() + " , or 0 for menu:");
			classnum = s.nextInt();
		}
		if (classnum == 0) {
			return;
		}
		sys.removeRep(sys.getRepByNum(classnum));
		System.out.println("Class removed successfully.");
	}

	public static void showQuestionsAndAnswers(Repository rep) {
		if (rep.noQuestions()) {
			System.out.println("No questions yet. Add some and try again.");
		} else {
			System.out.println(rep.toString());
		}
	}

	public static void addAnswerToQuestion(Repository rep, Scanner s) {

		if (rep.noQuestions()) {
			System.out.println("No Questions yet. Add some and try again.");
		} else {
			System.out.println("Choose question, or type 0 to return to menu:");

			System.out.println(rep.questionsToString());
			int qNum = s.nextInt();
			while (qNum != 0) {
				if (qNum < 1 || qNum > rep.getNumOfQuestions()) {
					System.out.println("Invalid question number. Please try again or type 0 to return to menu.");
					System.out.println(rep.questionsToString());
					qNum = s.nextInt();
				} else if (!rep.canAddAnswerToQNum(qNum)) {
					if (rep.isOpenQuestion(qNum)) {
						System.out.println(
								"This open question already has an answer. To change it, remove this question with menu option 3 and re-enter it again and add an answer.");
					} else {
						System.out.println("Question already has " + rep.getMaxAnswers()
								+ " answers. Choose another or type 0 to return to menu.");
					}
					System.out.println(rep.questionsToString());
					qNum = s.nextInt();
				} else {

					System.out.println(
							"Choose whether to add from repository (type 1) or enter text (type 2). To return to menu type 0.");
					int choice = s.nextInt();
					while (choice > 2 || choice < 0) {
						System.out.println(
								"Invalid option. Choose whether to add from repository (type 1) or enter text (type 2). To return to menu type 0");
						choice = s.nextInt();
					}
					if (choice == 0) {
						return;
					}
					if (choice == 1) {
						System.out.println("Choose from the following answers by number: (or 0 for menu)");
						int aChoice = -1;
						System.out.println(rep.allAnswersToString());
						aChoice = s.nextInt();
						while (aChoice < 1 || aChoice > rep.getNumOfAnswers()) {
							if (aChoice == 0) {
								return;
							} else {
								System.out.println("Invalid option. Try again (or 0 for menu)");
								rep.allAnswersToString();
								aChoice = s.nextInt();
							}
						}

						if (rep.isOpenQuestion(qNum)) {
							rep.addAnswerToOpenQuestionFromRep(qNum, aChoice);
							System.out.println("Answer added succesfully.");
							return;
						} else {
							while (rep.qNumHasAnswerFromRep(qNum, aChoice)) {
								System.out
										.println("Answer already picked for this question. Try again (or 0 for menu)");
								rep.allAnswersToString();
								aChoice = s.nextInt();
							}
							String value = "";
							System.out.println("Enter value: (true/false) or 0 for menu");
							s.nextLine();
							value = s.nextLine();
							while (!value.equals("0") && !value.equals("true") && !value.equals("false")
									&& !value.equals("True") && !value.equals("False")) {
								System.out.println("Invalid value. Enter true / false or 0 for menu");
								value = s.nextLine();

								if (value.equals("0")) {
									return;
								}
							}
							boolean v;
							if (value.equals("true") || value.equals("True")) {
								v = true;
							} else {
								v = false;
							}
							rep.addAnswerToMCQuestionFromRep(aChoice, qNum, v);
							System.out.println("Answer added successfully.");
							return;
						}
					} else if (choice == 2) {
						System.out.println("Enter answer text: (or type the word 'exit' for menu)");
						s.nextLine();
						String aText = s.nextLine();
						if (aText.equals("exit)")) {
							return;
						} else {
							if (rep.isOpenQuestion(qNum)) {
								rep.addAnswerToOpenFromText(qNum, aText);
								System.out.println("Answer added successfully.");
								return;
							} else {
								if (rep.qNumHasAnswerByText(qNum, aText)) {
									System.out.println("Answer already exists for this question.");
								} else {
									String value = "";
									while (!value.equals("0") && !value.equals("true") && !value.equals("false")
											&& !value.equals("True") && !value.equals("False")) {
										System.out.println("Enter value: (true/false) or 0 for menu");
										value = s.nextLine();
										if (value.equals("0")) {
											return;
										}
									}
									boolean v;
									if (value.equals("true") || value.equals("True")) {
										v = true;
									} else {
										v = false;
									}
									rep.addAnswerToMCFromText(qNum, aText, v);
									System.out.println("Answer added successfully.");
									return;
								}
							}
						}
					}
				}
			}
		}
	}

	public static void addQuestion(Repository rep, Scanner s) {

		String text;
		System.out.println("Enter question text: (or 0 for menu) ");
		s.nextLine();
		text = s.nextLine();
		while (rep.isQuestionByText(text) && !text.equals("0")) {
			System.out.println("Question already exists. Type another (or 0 for menu):");
			text = s.nextLine();
		}
		if (text.equals("0")) {
			return;
		} else

			System.out.println("Please enter difficulty: easy, medium, or hard (or 0 for menu): ");
		String difText = s.nextLine();
		while (!rep.isEnumVal(difText) && !difText.equals("0")) {
			System.out.println("Invalid difficulty type. Please enter easy, medium or hard, (or 0 for menu):");
			difText = s.nextLine();
		}
		if (difText.equals("0")) {
			return;
		} else {
			System.out.println("Choose question type: 1 for open, 2 for multiple choice, 0 for menu:");
			int qType = s.nextInt();
			while (qType != 0 && qType != 1 && qType != 2) {
				System.out.println("Invalid input. Please try again: ");
				qType = s.nextInt();
			}
			if (qType == 1) {
				System.out.println(
						"Choose whether to add the answer from the repository (1), enter a new answer (2), or cancel and return to menu (0)");
				int aChoiceNum = s.nextInt();
				while (aChoiceNum != 0 && aChoiceNum != 1 && aChoiceNum != 2) {
					System.out.println("Invalid option. Please try again:");
					aChoiceNum = s.nextInt();
				}
				if (aChoiceNum == 0) {
					return;
				} else if (aChoiceNum == 1) {
					System.out.println("Choose from the following answers by number: (or 0 for menu)");
					int aChoice = -1;
					System.out.println(rep.allAnswersToString());
					aChoice = s.nextInt();
					while (aChoice < 1 || aChoice > rep.getNumOfAnswers()) {
						if (aChoice == 0) {
							return;
						} else {
							System.out.println("Invalid option. Try again (or 0 for menu)");
							rep.allAnswersToString();
							aChoice = s.nextInt();
						}
					}
					rep.addQuestion(text, aChoice - 1, eDifficulty.valueOf(difText));
				} else {
					System.out.println("Enter answer text, or type \"exit\" for menu: ");
					s.nextLine();
					String aText = s.nextLine();
					if (aText.equals("exit")) {
						return;
					}
					rep.addQuestion(text, aText, eDifficulty.valueOf(difText));
				}
			}

			else {
				rep.addQuestion(text, eDifficulty.valueOf(difText));
			}

			System.out.println("Question added successfully.");
		}
	}

	public static void removeAnswerFromQuestion(Repository rep, Scanner s) {

		if (rep.noQuestions()) {
			System.out.println("No questions yet. Add some and try again.");
		} else {
			System.out.println("Choose question, or type 0 to return to menu:");
			System.out.println(rep.questionsToString());
			int qNum = s.nextInt();
			while (qNum < 1 || qNum > rep.getNumOfQuestions() || rep.isEmptyQuestion(qNum)) {
				if (qNum == 0) {
					return;
				}

				else if (qNum < 1 || qNum > rep.getNumOfQuestions()) {
					System.out.println("Invalid question number. Please try again or type 0 to return to menu.");
					System.out.println(rep.questionsToString());
					qNum = s.nextInt();
				} else {
					System.out.println("Question doesn't have any answers. Choose another or 0 for menu.");
					System.out.println(rep.questionsToString());
					qNum = s.nextInt();
				}
			}

			if (!rep.isOpenQuestion(qNum)) {
				System.out.println("Choose answer, or type 0 for menu.");
				System.out.println(rep.questionAnswersToString(qNum));
				int aChoice = s.nextInt();
				while (aChoice < 1 || aChoice > rep.getNumOfAnswersByQNum(qNum)) {
					if (aChoice == 0) {
						return;
					} else {
						System.out.println("Invalid option. Try again (or 0 for menu)");
						rep.questionAnswersToString(qNum);
						aChoice = s.nextInt();
					}
				}
				rep.removeAnswerFromMCQuestion(qNum, aChoice);
				System.out.println("Answer removed successfully.");
			} else {
				System.out.println(
						"This question is open. To substitute its answer, remove it by typing 1 and re-enter question with menu option 3, or choose 0 to return to menu:");
				System.out.println(rep.questionsToString());
				int aChoiceNum = s.nextInt();
				while (aChoiceNum != 0 && aChoiceNum != 1) {
					System.out.println("Invalid option. Please try again: ");
					aChoiceNum = s.nextInt();
				}
				if (aChoiceNum == 0) {
					return;

				} else {
					rep.removeQuestion(qNum);
					System.out.println("Question removed successfully.");
					return;
				}

//				rep.removeAnswerFromOpenQuestion(qNum);
//				System.out.println("Answer removed successfully.");
			}
		}
	}

	public static void removeQuestion(Repository rep, Scanner s) {
		if (rep.noQuestions()) {
			System.out.println("No questions yet. Add some and try again");
			return;
		} else {
			System.out.println("Choose question to remove, or 0 for menu:");
			System.out.println(rep.questionsToString());
			int qNum = s.nextInt();
			while (qNum < 0 || qNum > rep.getNumOfQuestions()) {
				System.out.println("Invalid option. Try again, or choose 0 for menu:");
				System.out.println(rep.questionsToString());
				qNum = s.nextInt();
			}
			if (qNum == 0) {
				return;
			} else {
				rep.removeQuestion(qNum);
				System.out.println("Question removed successfully.");
			}
		}
	}

	public static void createTest(Repository rep, Scanner s) throws FileNotFoundException, IOException {
		System.out.println("Choose 1 for manual exam, 2 for automatic exam, or 0 to return to menu:");
		int res = s.nextInt();
		while (res != 0 && res != 1 && res != 2) {
			System.out.println("Wrong option - please try again: ");
			res = s.nextInt();
		}
		if (res == 0) {
			return;
		} else if (res == 1) {
			Examable exam = new ManualExam(rep);
			exam.createExam(rep);
		}

		else {
			Question[] questionsForAutomaticExam = rep.getValidQuestionsForExam();
			int numOfValid = questionsForAutomaticExam.length;
			System.out.println("Type number of desired questions, 0 for menu");
			int TestQNum = s.nextInt();
			while (TestQNum < 0 || TestQNum > numOfValid) {
				System.out.println("Invalid number. Please choose between 1 and " + numOfValid + ", or 0 for menu:");
				TestQNum = s.nextInt();
			}
			if (TestQNum == 0) {
				return;
			}
			Examable exam = new AutomaticExam(questionsForAutomaticExam, TestQNum);
			exam.createExam(rep);
		}
	}

//	public static void createTest(Repository rep, Scanner s) throws FileNotFoundException, IOException {
//
//		if (rep.repConditions()) { // there are questions and answers
//			System.out.println("Type number of desired questions, 0 for menu");
//			int TestQNum = s.nextInt();
//			while (TestQNum < 0 || TestQNum > rep.getNumOfQuestions()) {
//				System.out.println(
//						"Invalid number. Please choose between 1 and " + rep.getNumOfQuestions() + ", or 0 for menu:");
//				TestQNum = s.nextInt();
//			}
//			if (TestQNum == 0) {
//				return;
//			}
//			TestCreator test = new TestCreator(TestQNum, rep.getMaxAnswers());
//
//			int aChoice = -1, qNum = -2;
//			while (qNum != -1 && qNum != 0) {
//				System.out.println(
//						"Choose question. To finalize type -1, to return to menu type 0. To remove a picked question, choose it again.");
//				System.out.println(rep.questionsToString());
//				qNum = s.nextInt();
//				while (!rep.isValidQuestionNum(qNum)) {
//					System.out.println("Invalid question number. Please try again or type 0 for menu");
//					qNum = s.nextInt();
//				}
//				if (qNum == 0) {
//					return;
//				}
//
//				aChoice = -2;
//				if (qNum != -1) {
//
//					if (test.questionPicked(qNum)) {
//						test.removeQuestion(qNum);
//						System.out.println("Question removed successfully.");
//					} else {
//						if (test.isFull()) {
//							System.out.println("Question number exceeded. Please start over with more questions.");
//							return;
//						} else {
//							if (!rep.isEmptyQuestion(qNum)) {
//							test.addQuestion(qNum);
//						}
//							else {	
//								System.out.println("Question is empty");
//
//						}}
//						if (rep.isEmptyQuestion(qNum)) {
//													} else {
//							if (rep.isOpenQuestion(qNum)) {
//								test.addAnswerToQuestion(qNum, 1);
//								System.out.println("Question added successfully.");
//							} else {
//								while (aChoice != 0 && aChoice != -1) {
//									System.out.println(
//											"Choose answer(s). To remove a picked answer, choose it again. To remove question, type -1. To return to questions type 0.");
//									System.out.println(rep.questionAnswersToString(qNum));
//									aChoice = s.nextInt();
//
//									while (!rep.isValidAnswerToQuestion(qNum, aChoice)) {
//										System.out.println(
//												"Invalid Answer number. Try again or type 0 to return to questions.");
//										aChoice = s.nextInt();
//									}
//									if (aChoice != 0) {
//										if (aChoice == -1) {
//											test.removeQuestion(qNum);
//											System.out.println("Question removed successfully.");
//										} else if (test.answerPicked(qNum, aChoice)) {
//											test.removeAnswerFromMCQuestion(qNum, aChoice);
//											System.out.println("Answer removed successfully.");
//										} else {
//											test.addAnswerToQuestion(qNum, aChoice);
//											System.out.println("Answer added successfully.");
//										}
//									} else {
//										if (!test.questionHasAnswers(qNum)) {
//											test.removeQuestion(qNum);
//										}
//									}
//								}
//							}
//						}
//					}
//				} else {
//					if (test.noQuestions()) {
//						System.out.println("No questions chosen. returning to menu.");
//						return;
//					} else if (test.lessThanTestQNum()) {
//						System.out.println("Only " + test.getNumOfQuestions()
//								+ " question chosen. To finalize choose -1, 0 to return to questions");
//						qNum = s.nextInt();
//						while (qNum != 0 && qNum != -1) {
//							System.out.println("Invalid answer. Try again:");
//						}
//
//						if (qNum == 0) {
//							qNum = -2;
//						} else {
//							System.out.println("Finalizing:");
//							test.finalizeTest(rep);
//							System.out.println("Done");
//						}
//					} else {
//						System.out.println("Finalizing:");
//						test.finalizeTest(rep);
//						System.out.println("Done");
//					}
//				}
//			}
//		} else {
//
//			System.out.println("Repository doesn't contain enough data. Add questions or answers and try again.");
//		}
//	}

	public static void exit(Sys sys) throws FileNotFoundException, IOException {
		ObjectOutputStream outFile = new ObjectOutputStream(new FileOutputStream("sys.dat"));
		outFile.writeObject(sys);
		outFile.close();
	}

//	public static Sys startup() throws FileNotFoundException, IOException, ClassNotFoundException {
//		ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("sys.dat"));
//		inFile.close();
//		return (Sys) inFile.readObject();
//	}

	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException {

		Scanner s = new Scanner(System.in);
		ObjectInputStream inFile = new ObjectInputStream(new FileInputStream("sys.dat"));
		Sys sys = (Sys) inFile.readObject();
		inFile.close();

//		Hard coded data:

//		String[] classes = { "Math", "English" };		
//		Sys sys = new Sys(classes);
//		Repository rep1 = sys.getRepByNum(1);
//		Repository rep2 = sys.getRepByNum(2);
//		
//		rep1.addQuestion("q1", eDifficulty.easy);
//		rep1.addQuestion("q2", eDifficulty.medium);
//		rep1.addQuestion("q3", "a1", eDifficulty.medium);
//		rep1.addQuestion("q4", eDifficulty.hard);
//		rep2.addQuestion("q5", eDifficulty.easy);
//		rep2.addQuestion("q6", eDifficulty.medium);
//		rep2.addQuestion("q7", eDifficulty.hard);
//		rep2.addQuestion("q8", eDifficulty.easy);
//		
//		rep1.addAnswerToMCFromText(1, "a1", false);
//		rep1.addAnswerToMCFromText(1, "a2", false);
//		rep1.addAnswerToMCFromText(1, "a3", false);
//		rep1.addAnswerToMCFromText(1, "a4", true);
//		rep1.addAnswerToMCFromText(1, "a5", true);
//
//		rep1.addAnswerToMCFromText(2, "a1", false);
//		rep1.addAnswerToMCFromText(2, "a6", false);
//
//		rep1.addAnswerToMCFromText(4, "a7", true);
//		rep1.addAnswerToMCFromText(4, "a8", true);
//		rep1.addAnswerToMCFromText(4, "a9", true);
//		rep1.addAnswerToMCFromText(4, "a10", true);
//		rep1.addAnswerToMCFromText(4, "a11", true);
//		rep1.addAnswerToMCFromText(4, "a12", true);
//		rep1.addAnswerToMCFromText(4, "a13", true);
//		rep1.addAnswerToMCFromText(4, "a14", true);
//		rep1.addAnswerToMCFromText(4, "a15", true);
//		rep1.addAnswerToMCFromText(4, "a16", true);
//		
//		rep2.addAnswerToMCFromText(1, "a17", false);
//		rep2.addAnswerToMCFromText(1, "a18", false);
//		rep2.addAnswerToMCFromText(1, "a19", false);
//		rep2.addAnswerToMCFromText(1, "a20", false);
//		rep2.addAnswerToMCFromText(1, "a21", false);
//
//		rep2.addAnswerToMCFromText(2, "a22", false);
//		rep2.addAnswerToMCFromText(2, "a23", true);
//		rep2.addAnswerToMCFromText(2, "a24", false);
//		rep2.addAnswerToMCFromText(2, "a25", false);
//		rep2.addAnswerToMCFromText(2, "a26", false);
//		
//		rep2.addAnswerToMCFromText(3, "a27", false);
//		rep2.addAnswerToMCFromText(3, "a28", true);
//		rep2.addAnswerToMCFromText(3, "a29", false);
//		rep2.addAnswerToMCFromText(3, "a30", false);
//		
//		rep2.addAnswerToMCFromText(4, "a31", false);
//		rep2.addAnswerToMCFromText(4, "a32", true);
//		rep2.addAnswerToMCFromText(4, "a33", false);
//		rep2.addAnswerToMCFromText(4, "a34", false);

		int oChoice = -1;
		int cChoice = -1;
		do {
			printOpening();
			oChoice = s.nextInt();
			switch (oChoice) {
			case 0:
				exit(sys);
				break;
			case 1:
				cChoice = printClasses(sys, s);
				break;
			case 2:
				addNewClass(sys, s);
				break;
			case 3:
				removeClass(sys, s);
				break;
			default:
				System.out.println("Wrong option. Please try again:\n");
			}

			if (oChoice == 1 && cChoice != 0) {

				Repository rep = sys.getRepByNum(cChoice);
				int choice = -1;
				do {
					printMenu();
					choice = s.nextInt();
					switch (choice) {

					case 1:
						showQuestionsAndAnswers(rep);
						break;
					case 2:
						addAnswerToQuestion(rep, s);
						break;
					case 3:
						addQuestion(rep, s);
						break;
					case 4:
						removeAnswerFromQuestion(rep, s);
						break;
					case 5:
						removeQuestion(rep, s);
						break;
					case 6:
						createTest(rep, s);
					case 7:
						choice = 0;
						oChoice = -1;
						break;
					case 0:
						oChoice = 0;
						exit(sys);
						break;
					default:
						System.out.println("Wrong option. Please try again:\n");
					}
				} while (choice != 0);
			}
		} while (oChoice != 0);
	}
}