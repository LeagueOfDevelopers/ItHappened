package ru.lod_misis.ithappened.UI.background;

import android.util.Log;

import java.util.HashMap;
import java.util.UUID;

public class AllId {
    public static HashMap<Integer,UUID> map;
    private static int i;

    public static Integer addNewValue(UUID uuid){
        if(map==null){
            map=new HashMap();
        }
        if(!map.containsValue(uuid)){
            map.put(i,uuid);
            i++;
            return i;
        }else{
            for(int index=0;index<i;index++){
                UUID uuid1=map.get(index);
                if(uuid.equals(uuid1)){
                    return index;
                }
            }
        }
        return null;
    }
}
