package com.example.citizensonscience.classes;

public class Project {
    private int periodChoice, spacetimeChoice;

    public Project(int periodChoice, int spacetimeChoice) {
        this.periodChoice = periodChoice;
        this.spacetimeChoice = spacetimeChoice;
    }

    public int getPeriodChoice() {
        return periodChoice;
    }

    public void setPeriodChoice(int periodChoice) {
        this.periodChoice = periodChoice;
    }

    public int getSpacetimeChoice() {
        return spacetimeChoice;
    }

    public void setSpacetimeChoice(int spacetimeChoice) {
        this.spacetimeChoice = spacetimeChoice;
    }
}
