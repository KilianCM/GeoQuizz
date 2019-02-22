package com.example.geoquizz;

import org.json.JSONObject;

import java.util.ArrayList;

public class City {
    private String name;
    private String code;
    private ArrayList<String> codes;
    private Double surface;
    private String codeDepartment;
    private String codeRegion;
    private String population;
    private JSONObject department;
    private JSONObject region;

    public City(String name, String code, ArrayList<String> codes, Double surface, String codeDepartment, String codeRegion, String population, JSONObject department, JSONObject region) {
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

    public City(){}

    public String getName() {
        return name;
    }

    public String getCode() {
        return code;
    }

    public ArrayList<String> getCodes() {
        return codes;
    }

    public Double getSurface() {
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

    public void setName(String name) {
        this.name = name;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setCodes(ArrayList<String> codes) {
        this.codes = codes;
    }

    public void setSurface(Double surface) {
        this.surface = surface;
    }

    public void setCodeDepartment(String codeDepartment) {
        this.codeDepartment = codeDepartment;
    }

    public void setCodeRegion(String codeRegion) {
        this.codeRegion = codeRegion;
    }

    public void setPopulation(String population) {
        this.population = population;
    }

    public void setDepartment(JSONObject department) {
        this.department = department;
    }

    public void setRegion(JSONObject region) {
        this.region = region;
    }
}
