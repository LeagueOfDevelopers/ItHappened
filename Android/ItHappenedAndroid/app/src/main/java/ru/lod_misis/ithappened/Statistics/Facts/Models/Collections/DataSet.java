package ru.lod_misis.ithappened.Statistics.Facts.Models.Collections;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class DataSet<T> {

    private List<T> firstColumn = new ArrayList<>();
    private List<T> secondColumn = new ArrayList<>();
    private int length = 0;
    private DataSetType Type;

    public DataSet(DataSetType type) {
        Type = type;
    }

    public DataSet(List<T> column1, List<T> column2, DataSetType type) {
        if (column1.size() != column2.size()) throw new IllegalArgumentException();
        Type = type;
        for (int i = 0; i < column1.size(); i++) {
            AddRow(column1.get(i), column2.get(i));
        }
    }

    public List<T> GetRow(int i) {
        List<T> row = new ArrayList<>();
        row.add(firstColumn.get(i));
        row.add(secondColumn.get(i));
        return row;
    }

    public List<T> GetColumn(int j) {
        if (j > 1 || j < 0) throw new IndexOutOfBoundsException();
        return j == 0 ? firstColumn : secondColumn;
    }

    public void AddRow(T first, T second){
        firstColumn.add(first);
        secondColumn.add(second);
        length++;
    }

    public boolean IsMultimonial() {
        return Type == DataSetType.Boolean || Type == DataSetType.Integer;
    }

    public int Length() {
        return length;
    }

    public DataSetType getType() {
        return Type;
    }
}
