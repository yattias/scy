package eu.scy.agents.conceptmap;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;

public class RankedKeywordList {

    private TreeMap<Double, Set<String>> map;

    public RankedKeywordList() {
        map = new TreeMap<Double, Set<String>>(new Comparator<Double>() {

            @Override
            public int compare(Double o1, Double o2) {
                return o2.compareTo(o1);
            }
        });
    }

    public void put(double ranking, String keyword) {
        Set<String> set = map.get(ranking);
        if (set == null) {
            set = new HashSet<String>();
            map.put(ranking, set);
        }
        set.add(keyword);
    }

    public Set<String> get(double ranking) {
        Set<String> set = map.get(ranking);
        if (set != null) {
            return set;
        } else {
            return new HashSet<String>();
        }
    }

    public List<String> getKeywords() {
        return getKeywords(Integer.MAX_VALUE);
    }

    public List<String> getKeywords(int amount) {
        ArrayList<String> result = new ArrayList<String>();
        for (Double d : map.keySet()) {
            Set<String> set = map.get(d);
            for (String s : set) {
                result.add(s);
                if (result.size() == amount) {
                    return result;
                }
            }
        }
        return result;
    }

    public double getWeightFor(String keyword) {
        for (Double d : map.keySet()) {
            Set<String> set = map.get(d);
            if (set.contains(keyword)) {
                return d;
            }
        }
        throw new IllegalArgumentException("keyword not found in list");
    }
}
