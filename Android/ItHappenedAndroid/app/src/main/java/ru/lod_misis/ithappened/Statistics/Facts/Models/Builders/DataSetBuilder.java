package ru.lod_misis.ithappened.Statistics.Facts.Models.Builders;

import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;

public class DataSetBuilder {

    public static DataSet<Double> BuildDoubleDataSet(List<Event> events1, List<Event> events2, int DaysToTrack) {
        if (events1 == null || events2 == null) return new DataSet<>(false);
        List<Event> copy1 = MakeEventListCopy(events1);
        SortCopyByTime(copy1);
        List<Event> copy2 = MakeEventListCopy(events2);
        SortCopyByTime(copy2);

        DataSet<Double> data = new DataSet<>(true);
        for (Event e1: copy1) {
            for (Event e2: copy2) {
                if (e1.getEventDate().before(e2.getEventDate())) {
                    Interval timedelta = new Interval(e1.GetEventDate().getTime(),
                            e2.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            e1.GetScale() != null && e2.GetScale() != null) {
                        data.AddRow(e1.GetScale(), e2.GetScale());
                        break;
                    }
                }
            }
        }
        for (Event e2: copy2) {
            for (Event e1: copy1) {
                if (e2.getEventDate().before(e1.getEventDate())) {
                    Interval timedelta = new Interval(e2.GetEventDate().getTime(),
                            e1.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            e1.GetScale() != null && e2.GetScale() != null) {
                        data.AddRow(e1.GetScale(), e2.GetScale());
                        break;
                    }
                }
            }
        }
        return data;
    }

    public static DataSet<Integer> BuildBooleanDataset(List<Event> events1, List<Event> events2, int DaysToTrack) {
        if (events1 == null || events2 == null) return new DataSet<>(false);
        List<Event> copy1 = MakeEventListCopy(events1);
        SortCopyByTime(copy1);
        List<Event> copy2 = MakeEventListCopy(events2);
        SortCopyByTime(copy2);

        DataSet<Integer> data = new DataSet<>(true);
        for (Event e1: copy1) {
            for (Event e2: copy2) {
                if (e1.getEventDate().before(e2.getEventDate())) {
                    Interval timedelta = new Interval(e1.GetEventDate().getTime(),
                            e2.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack) {
                        data.AddRow(1, 1);
                        break;
                    }
                    else {
                        data.AddRow(1, 0);
                        data.AddRow(0, 0);
                        break;
                    }
                }
            }
        }
        for (Event e2: copy2) {
            for (Event e1: copy1) {
                if (e2.getEventDate().before(e1.getEventDate())) {
                    Interval timedelta = new Interval(e2.GetEventDate().getTime(),
                            e1.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack) {
                        data.AddRow(1, 1);
                        break;
                    }
                    else {
                        data.AddRow(0, 1);
                        data.AddRow(0, 0); // Если между событиями больше 10 дней,
                                           // то можно сказать, что в течении 10 дней
                                           // ни одного из них не произошло.
                        break;
                    }
                }
            }
        }
        return data;
    }
    // Сначала делаем копию коллекции событий и сортируем ее по времени. Затем идем по коллекции
    // и смотрим, на каждую пару событий из 1 и 2 коллекции. Есть 2 варианта: Между событиями
    // менее чем в {DaysToTrack} дней или между ними прошло больше времени. В первом случае
    // кладем в датасет запись о том, что события могут влиять друг на друга, в противном случае,
    // так как массив отсортирован по времени, смотреть остальные элементы смысла нет, так как
    // они еще дальше по времени от первого события. Также в данном случае мы кладем в датасет
    // запись о том, что после события из группы 1 не произошло события из группы 2, а так же
    // запись о том, что в течении 10 дней не происходило ни одного события (потому что это
    // действительно так). Функции формирования скалярных и мультиномиальных (рейтинговых)
    // датасетов проще, так что описывать их смысла особо не вижу, так как принцип работы
    // один, а меняются только вытаскиваемые из эвентов поля.

    public static DataSet<Integer> BuildMultinomialDataset(List<Event> events1, List<Event> events2, int DaysToTrack) {
        if (events1 == null || events2 == null) return new DataSet<>(false);
        List<Event> copy1 = MakeEventListCopy(events1);
        SortCopyByTime(copy1);
        List<Event> copy2 = MakeEventListCopy(events2);
        SortCopyByTime(copy2);

        DataSet<Integer> data = new DataSet<>(true);
        for (Event e1: copy1) {
            for (Event e2: copy2) {
                if (e1.getEventDate().before(e2.getEventDate())) {
                    Interval timedelta = new Interval(e1.GetEventDate().getTime(),
                            e2.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            e1.GetRating() != null && e2.GetRating() != null) {
                        data.AddRow(e1.GetRating().getRating(), e2.GetRating().getRating());
                        break;
                    }
                }
            }
        }
        for (Event e2: copy2) {
            for (Event e1: copy1) {
                if (e2.getEventDate().before(e1.getEventDate())) {
                    Interval timedelta = new Interval(e2.GetEventDate().getTime(),
                            e1.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            e1.GetRating() != null && e2.GetRating() != null) {
                        data.AddRow(e1.GetRating().getRating(), e2.GetRating().getRating());
                        break;
                    }
                }
            }
        }
        return data;
    }

    public static DataSet<Double> ConvertToRanks(DataSet<Double> ds) {
        return new DataSet<>(ConvertListToRankArray(ds.GetColumn(0)),
                ConvertListToRankArray(ds.GetColumn(1)), true);
    }

    private static List<Double> ConvertListToRankArray(List<Double> list) {
        List<Double> copy = new ArrayList<>(list);
        Collections.sort(copy, new Comparator<Double>() {
            @Override
            public int compare(Double aDouble, Double t1) {
                return aDouble.compareTo(t1);
            }
        });
        Double[] ranks = new Double[list.size()];
        Map<Double, Integer> countDict = new HashMap<>();
        Map<Double, Integer> indexSum = new HashMap<>();
        for (int i = 0; i < copy.size(); i++) {
            if (countDict.containsKey(copy.get(i))) {
                int count = countDict.get(copy.get(i));
                countDict.remove(copy.get(i));
                countDict.put(copy.get(i), count + 1);

                int sum = indexSum.get(copy.get(i));
                indexSum.remove(copy.get(i));
                indexSum.put(copy.get(i), sum + i);
            }
            else {
                countDict.put(copy.get(i), 1);
                indexSum.put(copy.get(i), i);
            }
        }
        for (int i = 0; i < list.size(); i++) {
            ranks[i] = (double)indexSum.get(list.get(i)) / (double)countDict.get(list.get(i));
        }
        return Arrays.asList(ranks);
    }
    // Сначала копируем массив, а затем сортируем копию (не так важно, по убыванию или возрастанию).
    // Затем начинаем заполнение словарей. Зачем словари? Если 2 элемента равны, то ранг у них
    // одинаковый и равне он среднему арифметическому от их индексов в отсортированном массиве.
    // Более подходящей структуры данных для организации подсчета я не нашел. Если вы где то
    // найдете информацию, что ранги начинаются с единицы а не с нуля, то это не важно, так
    // как отношение порядков так и так сохраняется (какая разница между 0 < 1 и 1 < 2?
    // Оба этих утверждения истины, так что никакой).
    // Таким образом операция сборки массива рангов из этих двух словарей не доставляет проблем.

    private static void SortCopyByTime(List<Event> events) {
        Collections.sort(events, new Comparator<Event>() {
            @Override
            public int compare(Event event, Event t1) {
                return event.getEventDate().compareTo(t1.getEventDate());
            }
        });
    }

    private static List<Event> MakeEventListCopy(List<Event> events) {
        return new ArrayList<>(events);
    }

    private static boolean IsDateInInterval(Date first, Date second, Date date) {
        return date.before(new Date(second.getTime() + 1)) &&
                date.after(new Date(first.getTime() - 1));
    }
    // Временной интервал развинут на единицу, чтобы туда влезли крайние события
}
