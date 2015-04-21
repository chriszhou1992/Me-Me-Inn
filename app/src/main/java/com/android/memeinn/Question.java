package com.android.memeinn;

import java.util.ArrayList;
import java.util.HashMap;

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

    public Question(HashMap data) {
        CORRECT_OPTION = (int)data.get("CORRECT_OPTION");
        question = (String)data.get("question");
        ArrayList optionList = (ArrayList)data.get("options");
        options = new String[optionList.size()];
        for (int i = 0; i < options.length; i++)
            options[i] = (String)optionList.get(i);
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder(question);
        b.append("\n");
        for (int i = 0; i < options.length; i++) {
            String s = options[i];
            if (CORRECT_OPTION == i)
                b.append("*Choice");
            else
                b.append("Choice");
            b.append(i).append(": ");
            b.append(s).append("\n");
        }
        return b.toString();
    }
}
