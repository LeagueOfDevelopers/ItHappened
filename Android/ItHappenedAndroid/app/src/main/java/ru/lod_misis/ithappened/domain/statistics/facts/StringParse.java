package ru.lod_misis.ithappened.domain.statistics.facts;

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
        if (times % 100 <=20 && times % 100 >=10 || times % 10 >= 5 && times % 10 <= 9 || times % 10 == 0) return "событий";
        return "событий";
    }

    public static String parseDouble(double d) {


        float thousand = 1000f;
        float mln = 1000000f;
        float bln = 1000000000f;



        if (d == (long) d){
            if ((d >= 1000 && d < 1000000)) {
                double returnable = d/thousand;
                return  zeroAfterPointDeleting(returnable)+ " тыс.";
            }
        if ((d >= 1000000 && d < 1000000000)) {
            double returnable = d/mln;
            return zeroAfterPointDeleting(returnable) + " млн.";
        }
        if (d >= 1000000000 && d < 1000000000000d) {
            double returnable = d/bln;
            return zeroAfterPointDeleting(returnable) + " млрд.";
        }
            return  zeroAfterPointDeleting(d);
    }else {
            if ((d >= 1000 && d < 1000000)) {
                double returnable = d/thousand;
                return String.format("%s", returnable) + " тыс.";
            }
            if ((d >= 1000000 && d < 1000000000)) {
                double returnable = d/mln;
                return String.format("%s", returnable) + " млн.";
            }
            if (d >= 1000000000 && d < 1000000000000d) {
                double returnable = d/bln;
                return String.format("%s", returnable) + " млрд.";
            }
            return String.format("%s", d);
        }
    }

    private static String zeroAfterPointDeleting(double d){
        String s = String.valueOf(d);
        char[] array = s.toCharArray();
        int pointIndex = 0;

        for(int i=0;i<s.length();i++){
            if(array[i]=='.'){
                pointIndex = i;
                break;
            }
        }

        boolean flag = false;

        for(int i=pointIndex+1;i<s.length();i++){
            if(array[i]!='0'){
                flag = true;
                break;
            }
        }

        if(flag){
            return s;
        }else{
            return s.substring(0,pointIndex);
        }
    }
}
