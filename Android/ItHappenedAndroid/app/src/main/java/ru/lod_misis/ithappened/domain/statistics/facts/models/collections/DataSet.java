package ru.lod_misis.ithappened.domain.statistics.facts.models.collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataSet<T> {

    private List<List<T>> Rows = new ArrayList<>();
    private boolean isMultinomial;
    private int length = 0;

    public DataSet(boolean isMultinomial) {
        this.isMultinomial = isMultinomial;
    }

    public DataSet(List<T> column1, List<T> column2, boolean isMultinomial) {
        if (column1.size() != column2.size()) throw new IllegalArgumentException();
        for (int i = 0; i < column1.size(); i++) {
            AddRow(column1.get(i), column2.get(i));
        }
    }

    public List<T> GetRow(int i) {
        return Rows.get(i);
    }

    public List<T> GetColumn(int j) {
        ArrayList<T> col = new ArrayList<>();
        for (List<T> list: Rows) {
            col.add(list.get(j));
        }
        return col;
    }

    public void AddRow(T first, T second){
        List<T> l = new ArrayList<>();
        l.add(first);
        l.add(second);
        Rows.add(l);
        length++;
    }

    public void SortBy(Comparator<List<T>> comparator) {
        Collections.sort(Rows, comparator);
    }

    public boolean IsMultimonial() {
        return isMultinomial;
    }

    public int Length() {
        return length;
    }
}
