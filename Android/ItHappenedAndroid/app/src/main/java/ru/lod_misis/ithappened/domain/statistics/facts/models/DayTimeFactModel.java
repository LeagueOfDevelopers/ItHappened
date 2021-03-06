package ru.lod_misis.ithappened.domain.statistics.facts.models;

/**
 * Created by Ded on 29.03.2018.
 */

public class DayTimeFactModel {
    Double percetage;
    DayTime dayTime;

    public DayTimeFactModel() {
    }

    public Double getPercetage() {
        return percetage;
    }

    public DayTime getDayTime() {
        return dayTime;
    }

    public void calculateDate(int time, Double percetage){
        this.percetage = percetage;
        switch (time) {
            case 0:
                dayTime = DayTime.NIGHT;
                break;
            case 1:
                dayTime = DayTime.MORNING;
                break;
            case 2:
                dayTime = DayTime.AFTERNOON;
                break;
            case 3:
                dayTime = DayTime.EVENING;
                break;
        }
    }

    public String getDayTimeAsString(){
        switch (dayTime) {
            case NIGHT: {
                return "ночью.";
            }
            case MORNING: {
                return "утром.";
            }
            case AFTERNOON: {
                return "днем.";
            }
            case EVENING: {
                return "вечером.";
            }
        }

        return null;
    }
}
