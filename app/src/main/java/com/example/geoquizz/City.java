package com.example.geoquizz;

import org.json.JSONObject;

import java.util.ArrayList;

public class City {
    private String name;
    private String code;
    private ArrayList<String> codes;
    private Double surface;
    private String population;
    private Department department;
    private Region region;

    public City(String name, String code, ArrayList<String> codes, Double surface, String population, Department department, Region region) {
        this.name = name;
        this.code = code;
        this.codes = codes;
        this.surface = surface;
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
        return this.department.getCode();
    }

    public String getCodeRegion() { return this.region.getCode(); }

    public String getPopulation() {
        return population;
    }

    public Department getDepartment() {
        return department;
    }

    public Region getRegion() {
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

    public void setPopulation(String population) {
        this.population = population;
    }

    public void setDepartment(Department department) {
        this.department = department;
    }

    public void setRegion(Region region) {
        this.region = region;
    }
}
