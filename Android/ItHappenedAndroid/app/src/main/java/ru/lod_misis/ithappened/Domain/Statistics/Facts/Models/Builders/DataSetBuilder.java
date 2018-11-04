package ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Builders;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Interval;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ru.lod_misis.ithappened.Domain.Models.EventV1;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Collections.DataSet;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Collections.Sequence;
import ru.lod_misis.ithappened.Domain.Statistics.Facts.Models.Trends.EventsTimeDistribution;

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
        int firstIndex = 0;
        for (int i = 0; i < copy1.size(); i++) {
            for (int j = firstIndex; j < copy2.size(); j++) {
                if (IsObjectNull(copy1.get(i).getScale()) || IsObjectNull(copy2.get(j).getScale())) continue;
                if (copy1.get(i).getEventDate().before(copy2.get(j).getEventDate())) {
                    Interval timedelta = new Interval(copy1.get(i).getEventDate().getTime(),
                            copy2.get(j).getEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            copy1.get(i).getScale() != null && copy2.get(j).getScale() != null) {
                        data.AddRow(copy1.get(i).getScale(), copy2.get(j).getScale());
                    }
                    firstIndex = j;
                    break;
                }
            }
        }
        int secondIndex = 0;
        for (int i = 0; i < copy2.size(); i++) {
            for (int j = secondIndex; j < copy1.size(); j++) {
                if (IsObjectNull(copy1.get(j).getScale()) || IsObjectNull(copy2.get(i).getScale())) continue;
                if (copy2.get(i).getEventDate().before(copy1.get(j).getEventDate())) {
                    Interval timeDelta = new Interval(copy2.get(i).getEventDate().getTime(),
                            copy1.get(j).getEventDate().getTime());
                    if (timeDelta.toDuration().getStandardDays() <= DaysToTrack &&
                            copy1.get(j).getScale() != null && copy2.get(i).getScale() != null) {
                        data.AddRow(copy1.get(j).getScale(), copy2.get(i).getScale());
                    }
                    secondIndex = j;
                    break;
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

        DateTime firstDateInFirstSet = new DateTime(copy1.get(0).getEventDate());
        DateTime firstDateInSecondSet = new DateTime(copy2.get(0).getEventDate());
        DateTime firstDate = firstDateInFirstSet.isAfter(firstDateInSecondSet)
                ? firstDateInFirstSet : firstDateInSecondSet;

        DateTime lastDateInFirstSet = new DateTime(copy1.get(copy1.size() - 1).getEventDate());
        DateTime lastDateInSecondSet = new DateTime(copy2.get(copy2.size() - 1).getEventDate());
        DateTime lastDate = lastDateInFirstSet.isAfter(lastDateInSecondSet)
                ? lastDateInFirstSet : lastDateInSecondSet;

        int eventCount = 0;
        for (EventV1 e: copy1) {
            if (IsObjectNull(e)) continue;
            if (firstDate.minus(1).isBefore(e.getEventDate().getTime())
                    && lastDate.plus(1).isAfter(e.getEventDate().getTime())) {
                eventCount++;
            }
        }
        for (EventV1 e: copy2) {
            if (IsObjectNull(e)) continue;
            if (firstDate.minus(1).isBefore(e.getEventDate().getTime())
                    && lastDate.plus(1).isAfter(e.getEventDate().getTime())) {
                eventCount++;
            }
        }

        Duration dateDelta = new Duration(
                (lastDate.toDate().getTime() - firstDate.toDate().getTime()) / eventCount - 1);
        int event1Index = 0;
        int event2Index = 0;
        DataSet<Integer> data = new DataSet<>(true);
        DateTime date = firstDate.minus(1);
        while (date.isBefore(lastDate.plus(1))) {
            int firstEventCount = 0;
            int secondEventCount = 0;
            for (int i = event1Index; i < copy1.size(); i++) {
                if (date.isAfter(copy1.get(i).getEventDate().getTime()))
                    continue;
                if (date.isBefore(copy1.get(i).getEventDate().getTime())
                        && date.plus(dateDelta).isAfter(copy1.get(i).getEventDate().getTime())) {
                    firstEventCount++;
                }
                else {
                    event1Index = i;
                    break;
                }
            }
            for (int i = event2Index; i < copy2.size(); i++) {
                if (date.isAfter(copy2.get(i).getEventDate().getTime()))
                    continue;
                if (date.isBefore(copy2.get(i).getEventDate().getTime())
                        && date.plus(dateDelta).isAfter(copy2.get(i).getEventDate().getTime())) {
                    secondEventCount++;
                }
                else {
                    event2Index = i;
                    break;
                }
            }
            int first = firstEventCount > 0 ? 1 : 0;
            int second = secondEventCount > 0 ? 1 : 0;
            data.AddRow(first, second);
            date = date.plus(dateDelta);
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
        int firstIndex = 0;
        for (int i = 0; i < copy1.size(); i++) {
            for (int j = firstIndex; j < copy2.size(); j++) {
                if (IsObjectNull(copy1.get(i).getRating()) || IsObjectNull(copy2.get(j).getRating())) continue;
                if (copy1.get(i).getEventDate().before(copy2.get(j).getEventDate())) {
                    Interval timedelta = new Interval(copy1.get(i).getEventDate().getTime(),
                            copy2.get(j).getEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            copy1.get(i).getRating() != null && copy2.get(j).getRating() != null) {
                        data.AddRow(copy1.get(i).getRating().getRating(), copy2.get(j).getRating().getRating());
                    }
                    firstIndex = j;
                    break;
                }
            }
        }
        int secondIndex = 0;
        for (int i = 0; i < copy2.size(); i++) {
            for (int j = secondIndex; j < copy1.size(); j++) {
                if (IsObjectNull(copy1.get(j).getRating()) || IsObjectNull(copy2.get(i).getRating())) continue;
                if (copy2.get(i).getEventDate().before(copy1.get(j).getEventDate())) {
                    Interval timedelta = new Interval(copy2.get(i).getEventDate().getTime(),
                            copy1.get(j).getEventDate().getTime());
                    if (timedelta.toDuration().getStandardDays() <= DaysToTrack &&
                            copy1.get(j).getRating() != null && copy2.get(i).getRating() != null) {
                        data.AddRow(copy1.get(j).getRating().getRating(), copy2.get(i).getRating().getRating());
                    }
                    secondIndex = j;
                    break;
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
            if (e.getScale() != null && !e.isDeleted())
                result.add(e.getScale());
        }
        return new Sequence(result);
    }

    public static Sequence BuildRatingSequence(List<EventV1> events) {
        List<Double> result = new ArrayList<>();
        for (EventV1 e: events) {
            if (e.getRating() != null)
                result.add((double)e.getRating().getRating());
        }
        return new Sequence(result);
    }

    public static EventsTimeDistribution BuildFrequencySequence(List<EventV1> events) {
        List<EventV1> copy = MakeEventListCopy(events);
        SortCopyByTime(copy);

        // Находим правую и левую границы интервала,
        // а так же шаг разбиения данного интервала (дельту)
        // Правая граница - это текущая дата,
        // но dateDelta - это среднее время между эвентами,
        // поэтому вычисляем его через дату последнего эвента
        long leftBorder = copy.get(0).getEventDate().getTime();
        long rightBorder = DateTime.now().toDate().getTime();
        long lastEventDate = copy.get(copy.size() - 1).getEventDate().getTime();
        long dateDelta = (lastEventDate - leftBorder) / (events.size() - 1);

        // Создаем новый обьект, который будет хранить данные
        // о том, в какой период, какие события произошли
        EventsTimeDistribution distr = new EventsTimeDistribution();

        DateTime leftBorderTime = new DateTime(leftBorder - 1);
        int eventInd = 0;
        // Идем от самой первой даты с шагом dateDelta
        DateTime date = leftBorderTime;
        while (date.isBefore(rightBorder)) {

            // Создаем новый интервал для промежутка
            // размером в dateDelta с началом в date
            int id = distr.addNewInterval(date, date.plus(dateDelta));

            // Идем от первого эвента и смотрим,
            // принадлежит ли он нашему текущему промежутку
            for (int j = eventInd; j < copy.size(); j++) {
                if (date.isBefore(copy.get(j).getEventDate().getTime()) &&
                        date.plus(dateDelta).isAfter(copy.get(j).getEventDate().getTime())) {
                    // Если да, то доавляем его в коллекцию
                    distr.addEventToCollection(copy.get(j), id);
                }
                else {
                    // В противном случае можно закончить рассмотрение,
                    // так как если дата этого эвента вышла за пределы промежутка,
                    // то следующие эвены произошли еще позднее и рассматривать
                    // их в контексте этого интервала бессмысленно. Так же стоит
                    // отметить, что эвент может быть только больше, чем правая
                    // граница промежутка, так как они отсортированы по дате,
                    // а благодаря этому индексу мы не рассматриваем один эвент больше одного раза
                    eventInd = j;
                    break;
                }
            }
            // Меняем дату начала текущего промежутка
            date = date.plus(dateDelta);
        }
        return distr;
    }
}
