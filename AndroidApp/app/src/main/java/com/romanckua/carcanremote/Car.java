package com.romanckua.carcanremote;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Car {

    private final String[] carList = {"default", "Ravon R4"};
    private static HashMap<String, String> listCarFunc = new HashMap<>();


    public Car(String car) {

        switch (car) {
            case "default":
                listCarFunc.put("close all door", "241-8-07AE010101000000");
                listCarFunc.put("open all door", "241-8-07AE010101000000");
                break;
            case "Ravon R4":
                listCarFunc.put("close all door", "241-8-07AE010101000000");
                listCarFunc.put("open all door", "241-8-07AE010101000000");
                listCarFunc.put("open the trunk", "241-8-07AE013030000000");
                break;

        }
    }

    public ArrayList<String> getListCarFunc() {
        ArrayList<String> arrayListCarFunc = new ArrayList<>();
        for (Map.Entry<String, String> index: listCarFunc.entrySet()
             ) {
            arrayListCarFunc.add(index.getKey());
        }
        return arrayListCarFunc;
    }

}

