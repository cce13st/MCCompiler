package Compile;

import java.util.HashMap;

/**
 * Created by yeonni on 15. 6. 7..
 */
public class Environment {
    private HashMap<String, Integer> map = new HashMap<String, Integer>();

    public void add(String name, int n) {
        this.map.put(name, new Integer(n));
    }

    public int get(String name) {
        return this.map.get(name).intValue();
    }
}
