package com.android.memeinn.learn;

/**
 * Class that models a question in quiz/match.
 */
public class Question {
    public String question;
    public String[] options;
    public final int CORRECT_OPTION;

    public Question(String q, String[] options, int correctOption) {
        question = q;
        this.options = new String[options.length];
        System.arraycopy(options, 0, this.options, 0, options.length);
        CORRECT_OPTION = correctOption;
    }
}
