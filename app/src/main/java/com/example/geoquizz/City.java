package com.example.geoquizz;

import org.json.JSONObject;

import java.util.ArrayList;

public class City {
    private String name;
    private String code;
    private ArrayList<String> codes;
    private String surface;
    private String codeDepartment;
    private String codeRegion;
    private String population;
    private JSONObject department;
    private JSONObject region;

    public City(String name, String code, ArrayList<String> codes, String surface, String codeDepartment, String codeRegion, String population, JSONObject department, JSONObject region) {
        this.name = name;
        this.code = code;
        this.codes = codes;
        this.surface = surface;
        this.codeDepartment = codeDepartment;
        this.codeRegion = codeRegion;
        this.population = population;
        this.department = department;
        this.region = region;
    }

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<String> getCodes() {
        return codes;
    }

    public String getSurface() {
        return surface;
    }

    public String getCodeDepartment() {
        return codeDepartment;
    }

    public String getCodeRegion() {
        return codeRegion;
    }

    public String getPopulation() {
        return population;
    }

    public JSONObject getDepartment() {
        return department;
    }

    public JSONObject getRegion() {
        return region;
    }
}
