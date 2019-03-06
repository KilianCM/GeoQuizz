package com.example.geoquizz;

import org.json.JSONObject;

import java.util.Random;

public class QuestionLibrary {
    private String region = QuizzGeolocalisation.mCity.getRegion().getName();
    private String department = QuizzGeolocalisation.mCity.getDepartment().getName();

    private Integer population = Integer.parseInt(QuizzGeolocalisation.mCity.getPopulation());

    Random r = new Random();

    private String mQuestions [] = {
            "Dans quelle région se trouve cette commune ?",
            "Dans quel département se trouve cette commune ?",
            "Quel est le numéro de ce département ?",
            "Quelle est la surface de cette commune ?",
            "Combien d'habitants y vivent ?"
    };


    private Object mChoices [][] = {
            {MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName(), MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName(), MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName(),region},
            {MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName(), department, MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName(), MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName()},
            {MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode(), MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode(), QuizzGeolocalisation.mCity.getCodeDepartment() , MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode()},
            { QuizzGeolocalisation.mCity.getSurface()+" km²",  QuizzGeolocalisation.mCity.getSurface()-10+" km²", QuizzGeolocalisation.mCity.getSurface()-10+" km²", QuizzGeolocalisation.mCity.getSurface()-10+" km²" },
            {population-5000, population-5000, population-5000, QuizzGeolocalisation.mCity.getPopulation()}
    };



    public Object mCorrectAnswers[] = {region, department, QuizzGeolocalisation.mCity.getCodeDepartment(), QuizzGeolocalisation.mCity.getSurface()+" km²", QuizzGeolocalisation.mCity.getPopulation()};




    public String getQuestion(int a) {
        String question = mQuestions[a];
        return question;
    }


    public Object getChoice1(int a) {
        Object choice0 = mChoices[a][0];
        return choice0;
    }


    public Object getChoice2(int a) {
        Object choice1 = mChoices[a][1];
        return choice1;
    }

    public Object getChoice3(int a) {
        Object choice2 = mChoices[a][2];
        return choice2;
    }

    public Object getChoice4(int a) {
        Object choice3 = mChoices[a][3];
        return choice3;
    }

    public Object getCorrectAnswer(int a) {
        Object answer = mCorrectAnswers[a];
        return answer;
    }
}