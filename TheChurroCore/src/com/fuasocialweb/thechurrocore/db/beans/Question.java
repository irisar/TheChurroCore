package com.fuasocialweb.thechurrocore.db.beans;

import java.io.Serializable;

public class Question implements Serializable {

	private static final long serialVersionUID = 1L;
	private int id;
    private String question;
    private String answer1;
    private String answer2;
    private String answer3;
    private String answer4;
    private int correctAnswer;
    private int level;
    private String multimedia;
    private int type;
    private int answered;
 
    public Question(){}

    public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getAnswer1() {
		return answer1;
	}

	public void setAnswer1(String answer1) {
		this.answer1 = answer1;
	}

	public String getAnswer2() {
		return answer2;
	}

	public void setAnswer2(String answer2) {
		this.answer2 = answer2;
	}

	public String getAnswer3() {
		return answer3;
	}

	public void setAnswer3(String answer3) {
		this.answer3 = answer3;
	}

	public String getAnswer4() {
		return answer4;
	}

	public void setAnswer4(String answer4) {
		this.answer4 = answer4;
	}

	public int getCorrectAnswer() {
		return correctAnswer;
	}
	
	public String getCorrectAnswerString() {
		String answer = "";
		switch (correctAnswer) {
		case 1:
			answer = answer1;
			break;
		case 2:
			answer = answer2;
			break;
		case 3:
			answer = answer3;
			break;
		case 4:
			answer = answer4;
			break;
		default:
			break;
		}
		return answer;
	}

	public void setCorrectAnswer(int correctAnswer) {
		this.correctAnswer = correctAnswer;
	}

	public int getLevel() {
		return level;
	}

	public void setLevel(int level) {
		this.level = level;
	}

	public String getMultimedia() {
		return multimedia;
	}

	public void setMultimedia(String multimedia) {
		this.multimedia = multimedia;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getAnswered() {
		return answered;
	}

	public void setAnswered(int answered) {
		this.answered = answered;
	}
	
}
