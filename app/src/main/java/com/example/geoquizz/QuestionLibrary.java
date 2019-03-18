package com.example.geoquizz;

import org.json.JSONObject;
import android.util.Log;

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


    /*private Object mChoices [][] = {
            {MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName(), MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName(), MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName(),region},
            {MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName(), department, MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName(), MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName()},
            {MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode(), MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode(), QuizzGeolocalisation.mCity.getCodeDepartment() , MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode()},
            { QuizzGeolocalisation.mCity.getSurface()+" km²",  QuizzGeolocalisation.mCity.getSurface()-10+" km²", QuizzGeolocalisation.mCity.getSurface()-10+" km²", QuizzGeolocalisation.mCity.getSurface()-10+" km²" },
            {r.nextInt((population+population/10)-(population-population/10))+(population-population/10), r.nextInt((population+population/10)-(population-population/10))+(population-population/10), r.nextInt((population+population/10)-(population-population/10))+(population-population/10), QuizzGeolocalisation.mCity.getPopulation()}
    };*/
    private Object mChoices [][] = {
            {"Region","Region","Region","Region"},
            {"Dept","Dept","Dept","Dept"},
            {"Num","Num","Num","Num"},
            {"10","10","10","10"},
            {1000,1000,1000,1000}
    };



    public Object mCorrectAnswers[] = {region, department, QuizzGeolocalisation.mCity.getCodeDepartment(), QuizzGeolocalisation.mCity.getSurface().intValue()+" km²", QuizzGeolocalisation.mCity.getPopulation()};

    public QuestionLibrary(){

        for(int i = 0; i<4; i++){
            mChoices[0][i] = MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName();
        }

        while(this.findDuplicates(mChoices[0]) != -1){
            int j = this.findDuplicates(mChoices[0]);
            mChoices[0][j] = MainActivity.mRegionsData.get(r.nextInt(MainActivity.mRegionsData.size())).getName();
        }

        for(int i = 0; i<4; i++){
            mChoices[1][i] = MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName();
        }

        while(this.findDuplicates(mChoices[1]) != -1){
            int j = this.findDuplicates(mChoices[1]);
            mChoices[1][j] = MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getName();
        }

        for(int i = 0; i<4; i++){
            mChoices[2][i] = MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode();
        }

        while(this.findDuplicates(mChoices[2]) != -1){
            int j = this.findDuplicates(mChoices[2]);
            mChoices[2][j] = MainActivity.mDepartmentsData.get(r.nextInt(MainActivity.mDepartmentsData.size())).getCode();
        }

        for(int i = 0; i<4; i++){
            mChoices[3][i] = r.nextInt((QuizzGeolocalisation.mCity.getSurface().intValue()+(QuizzGeolocalisation.mCity.getSurface().intValue()/5))-(QuizzGeolocalisation.mCity.getSurface().intValue()-(QuizzGeolocalisation.mCity.getSurface().intValue()/5)))+(QuizzGeolocalisation.mCity.getSurface().intValue()-(QuizzGeolocalisation.mCity.getSurface().intValue()/5)) + " km²";
        }
        for(int i = 0; i<4; i++){
            mChoices[4][i] = r.nextInt((population+population/10)-(population-population/10))+(population-population/10);
        }

        mChoices[0][r.nextInt(4-1)+1] = region;
        mChoices[1][r.nextInt(4-1)+1] = department;
        mChoices[2][r.nextInt(4-1)+1] = QuizzGeolocalisation.mCity.getCodeDepartment();
        mChoices[3][r.nextInt(4-1)+1] = QuizzGeolocalisation.mCity.getSurface().intValue() + " km²";
        mChoices[4][r.nextInt(4-1)+1] = population;

    }

    private int findDuplicates(Object[] inputArray)
    {
        int result = -1;
        for (int i = 0; i < inputArray.length; i++)
        {
            for (int j = i+1; j < inputArray.length; j++)
            {
                if(inputArray[i] == inputArray[j])
                {
                    result = j;
                }
            }
        }
        return result;
    }


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