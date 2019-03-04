package com.example.geoquizz;

public class QuestionLibrary {
    private String mQuestions [] = {
            "Dans quelle région se trouve cette commune ?",
            "Dans quel département se trouve cette commune ?",
            "Quel est le numéro de ce département ?",
            "Quelle est la surface de cette commune ?",
            "Combien d'habitants y vivent ?"
    };

    private String mChoices [][] = {
            {"Roots", "Stem", "Flower","Auvergne-Rhône-Alpes"},
            {"Fruit", "Haute-Savoie", "Leaves", "Seeds"},
            {"Bark", "Flower",  "74", "Roots"},
            {"20km²", "Flower", "Leaves", "Stem" },
            {"jsp", "jsp", "jsp", "100 000"}
    };



    private String mCorrectAnswers[] = {"Auvergne-Rhône-Alpes", "Haute-Savoie", "74", "20km²", "100 000"};




    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }


    public String getChoice1(int a) {
        String choice0 = mChoices[a][0];
        return choice0;
    }


    public String getChoice2(int a) {
        String choice1 = mChoices[a][1];
        return choice1;
    }

    public String getChoice3(int a) {
        String choice2 = mChoices[a][2];
        return choice2;
    }

    public String getChoice4(int a) {
        String choice3 = mChoices[a][3];
        return choice3;
    }

    public String getCorrectAnswer(int a) {
        String answer = mCorrectAnswers[a];
        return answer;
    }
}