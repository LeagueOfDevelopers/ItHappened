package ru.lod_misis.ithappened.Statistics.Facts.Models.Builders;

import org.joda.time.DateTime;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.lod_misis.ithappened.Domain.Event;
import ru.lod_misis.ithappened.Domain.EventV1;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Statistics.Facts.Models.Collections.Sequence;

public class DataSetBuilder {

    public static DataSet<Double> BuildDoubleDataSet(List<EventV1> events1,
                                                     List<EventV1> events2,
                                                     int DaysToTrack) {
        if (events1 == null || events2 == null)
            return new DataSet<>(false);
        List<EventV1> copy1 = MakeEventListCopy(SelectNotDeletedEventsInThePast(events1));
        SortCopyByTime(copy1);
        List<EventV1> copy2 = MakeEventListCopy(SelectNotDeletedEventsInThePast(events2));
        SortCopyByTime(copy2);

        DataSet<Double> data = new DataSet<>(false);
        for (EventV1 e1: copy1) {
            for (EventV1 e2: copy2) {
                if (IsObjectNull(e1.GetScale()) || IsObjectNull(e2.GetScale())) continue;
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
        for (EventV1 e2: copy2) {
            for (EventV1 e1: copy1) {
                if (IsObjectNull(e1.GetScale()) || IsObjectNull(e2.GetScale())) continue;
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

    public static DataSet<Integer> BuildBooleanDataset(List<EventV1> events1,
                                                       List<EventV1> events2,
                                                       int DaysToTrack) {
        if (events1 == null || events2 == null)
            return new DataSet<>(false);
        List<EventV1> copy1 = MakeEventListCopy(SelectNotDeletedEventsInThePast(events1));
        SortCopyByTime(copy1);
        List<EventV1> copy2 = MakeEventListCopy(SelectNotDeletedEventsInThePast(events2));
        SortCopyByTime(copy2);

        DataSet<Integer> data = new DataSet<>(true);
        for (EventV1 e1: copy1) {
            for (EventV1 e2: copy2) {
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
        for (EventV1 e2: copy2) {
            for (EventV1 e1: copy1) {
                if (e2.getEventDate().before(e1.getEventDate())) {
                    Interval timedelta = new Interval(e2.GetEventDate().getTime(),
                            e1.GetEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack) {
                        data.AddRow(1, 1);
                        break;
                    }
                    else {
                        data.AddRow(0, 1);
                        data.AddRow(0, 0); // Если между событиями больше 1 дня,
                                           // то можно сказать, что в течении 1 дня
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

    public static DataSet<Integer> BuildMultinomialDataset(List<EventV1> events1,
                                                           List<EventV1> events2,
                                                           int DaysToTrack) {
        if (events1 == null || events2 == null)
            return new DataSet<>(false);
        List<EventV1> copy1 = MakeEventListCopy(SelectNotDeletedEventsInThePast(events1));
        SortCopyByTime(copy1);
        List<EventV1> copy2 = MakeEventListCopy(SelectNotDeletedEventsInThePast(events2));
        SortCopyByTime(copy2);

        DataSet<Integer> data = new DataSet<>(true);
        for (EventV1 e1: copy1) {
            for (EventV1 e2: copy2) {
                if (IsObjectNull(e1.GetRating()) || IsObjectNull(e2.GetRating())) continue;
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
        for (EventV1 e2: copy2) {
            for (EventV1 e1: copy1) {
                if (IsObjectNull(e1.GetRating()) || IsObjectNull(e2.GetRating())) continue;
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

    private static List<EventV1> SelectNotDeletedEventsInThePast(List<EventV1> events) {
        List<EventV1> validEvents = new ArrayList<>();
        for (EventV1 e: events) {
            if (e != null && !e.isDeleted() && new DateTime(e.getEventDate()).isBefore(DateTime.now())) {
                validEvents.add(e);
            }
        }
        return validEvents;
    }

    private static boolean IsObjectNull(Object e) {
        return e == null;
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

    private static void SortCopyByTime(List<EventV1> events) {
        Collections.sort(events, new Comparator<EventV1>() {
            @Override
            public int compare(EventV1 event, EventV1 t1) {
                return event.getEventDate().compareTo(t1.getEventDate());
            }
        });
    }

    private static List<EventV1> MakeEventListCopy(List<EventV1> events) {
        return new ArrayList<>(events);
    }

    public static Sequence BuildScaleSequence(List<EventV1> events) {
        List<Double> result = new ArrayList<>();
        for (EventV1 e: events) {
            if (e.GetScale() != null && !e.isDeleted())
                result.add(e.GetScale());
        }
        return new Sequence(result);
    }

    public static Sequence BuildRatingSequence(List<EventV1> events) {
        List<Double> result = new ArrayList<>();
        for (EventV1 e: events) {
            if (e.GetRating() != null)
                result.add((double)e.GetRating().GetRatingValue());
        }
        return new Sequence(result);
    }

    public static Sequence BuildFrequencySequence(List<EventV1> events) {
        List<EventV1> copy = MakeEventListCopy(events);
        SortCopyByTime(copy);
        long leftBorder = copy.get(0).GetEventDate().getTime();
        long rightBorder = copy.get(copy.size() - 1).GetEventDate().getTime();
        long dateDelta = (rightBorder - leftBorder) / (events.size() - 1);
        List<Double> frequencies = new ArrayList<>();
        int eventInd = 0;
        for (int i = 0; i < copy.size() - 1; i++) {
            int count = 0;
            for (int j = eventInd; j < copy.size(); j++) {

                if (leftBorder + dateDelta * (i) <= copy.get(j).getEventDate().getTime() &&
                        copy.get(j).getEventDate().getTime() < leftBorder + dateDelta * (i + 1)) {
                    count++;
                }
                else {
                    eventInd = j;
                    break;
                }
            }
            frequencies.add((double)count);
        }
        return new Sequence(frequencies);
    }
}
