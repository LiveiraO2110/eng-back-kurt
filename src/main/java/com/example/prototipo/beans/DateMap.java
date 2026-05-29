package com.example.prototipo.beans;

import com.example.prototipo.enums.Date;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class DateMap {
    private final HashMap<Date, Integer> map = new HashMap<>();

    public DateMap(){
        map.put(Date.DIA, 0);
        map.put(Date.SEMANA, 7);
        map.put(Date.MES, 30);
    }

    public Integer getDayValue(Date date){
        return map.get(date);
    }

    public Map<Date, Integer> getMap(){
        return map;
    }
}
