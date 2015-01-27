package com.crazyfish.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kamal on 2015/1/23.
 */
public class SerializableMap<T,T1> implements Serializable {
    private Map<T,T1> map;
    public SerializableMap(){
        map = new HashMap<T, T1>();
    }

    public Map<T,T1> getMap()
    {
        return map;
    }
    public void setMap(Map<T,T1> map)
    {
        this.map=map;
    }
}
