package com.smallaswater.wild.utils;

import cn.nukkit.level.Position;

import com.smallaswater.wild.Wild;
import com.smallaswater.wild.exception.RandomPositionException;

import java.util.List;

/**
 * @author 若水
 */
public class Tools {


    public static String getMessage(String language){
        return Wild.getApi().getString(language);
    }

    public static Position getRoundPosition(Position old,int size){
        try {
            return Wild.getApi().getRoundPosition(old,size);
        }catch (RandomPositionException e){
            return null;
        }

    }


    public static List<String> getBanTransferLevels(){
        return Wild.getApi().getBanTransferLevels();
    }

}
