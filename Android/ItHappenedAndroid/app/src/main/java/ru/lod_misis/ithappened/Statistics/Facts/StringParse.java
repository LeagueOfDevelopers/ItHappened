package ru.lod_misis.ithappened.Statistics.Facts;

/**
 * Created by Ded on 06.04.2018.
 */

public class StringParse {

    public static String days(int dayNum){

        if (dayNum % 100 <=20 && dayNum % 100 >=10) return "дней";
        if (dayNum % 10 == 1) return "день";
        if (dayNum % 10 >= 2 && dayNum % 10 <= 4) return "дня";
        return "дней";
    }

    public static String time(int times){
        if (times % 10 == 1 || times % 100 <=20 && times % 100 >=10) return "раз";
        if (times % 10 >= 2 && times % 10 <= 4) return "раза";
        return "раз";
    }

    public static String event(int times) {
        if (times % 100 <=20 && times % 100 >=10) return "событий";
        if (times % 10 >= 2 && times % 10 <= 4) return "события";
        return "событие";
    }
}
