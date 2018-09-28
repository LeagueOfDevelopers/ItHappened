package ru.lod_misis.ithappened;

import android.content.Intent;

import java.util.HashMap;

public class AllId {
    public static HashMap map;
    private static int i;

    public static Integer addNewValue(Object object){
        if(map==null){
            map=new HashMap();
        }
        if(!map.containsValue(object)){
            map.put(++i,object);
            return i;
        }else{
            for(int index=0;index<i;index++){
                if(object.equals(map.get(index))){
                    return i;
                }
            }
        }
        return null;
    }
}
