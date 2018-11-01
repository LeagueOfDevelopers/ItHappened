package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models;

/**
 * Created by Ded on 29.03.2018.
 */

public class WeekDaysFactModel {

    Double percetage;
    WeekDay weekDay;

    public WeekDaysFactModel() {
    }

    public void calculateData(double percetage, int day){
        switch (day) {
            case 1: {
                weekDay = WeekDay.SUNDAY;
                break;
            }
            case 2: {
                weekDay = WeekDay.MONDAY;
                break;
            }
            case 3: {
                weekDay = WeekDay.TUESDAY;
                break;
            }
            case 4: {
                weekDay = WeekDay.WEDNESDAY;
                break;
            }
            case 5: {
                weekDay = WeekDay.THURSDAY;
                break;
            }
            case 6: {
                weekDay = WeekDay.FRIDAY;
                break;
            }
            case 7: {
                weekDay = WeekDay.SATURDAY;
                break;
            }
        }

        this.percetage = percetage;
    }

    public String getWeekDayAsString(){
        switch (weekDay) {
            case SUNDAY: {
                return "в воскресенье.";
            }
            case MONDAY: {
                return "в понедельник.";
            }
            case TUESDAY: {
                return "во вторник.";
            }
            case WEDNESDAY: {
                return "в среду.";
            }
            case THURSDAY: {
                return "в четверг.";
            }
            case FRIDAY: {
                return "в пятницу.";
            }
            case SATURDAY: {
                return "в субботу.";
            }
        }

        return null;
    }

    public double getPercetage() {
        return percetage.doubleValue();
    }

    public WeekDay getWeekDay() {
        return weekDay;
    }
}