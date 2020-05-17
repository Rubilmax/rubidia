package me.pmilon.RubidiaCore.votes;

public abstract class Vote implements Runnable {
	
	private Long time;
	private String question;
	private String type;
	public Vote(long time, String question, String type){
		this.time = time;
		this.question = question;
		this.type = type;
	}
	
	public Long getTime() {
		return time;
	}
	
	public void setTime(Long time) {
		this.time = time;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
