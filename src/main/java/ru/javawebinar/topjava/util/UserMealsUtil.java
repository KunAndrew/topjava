package ru.javawebinar.topjava.util;

import javafx.concurrent.Worker;
import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,10,0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,13,0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30,20,0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,10,0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,13,0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31,20,0), "Ужин", 510)
        );
      //  getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
        getFilteredWithExceededStream(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        ArrayList<UserMeal> mealsWithTimeFilter = new ArrayList<>();
        ArrayList<UserMealWithExceed> mealsWithBothFilters = new ArrayList<>();
        HashMap<LocalDate, Integer> map = new HashMap<>();
        for (UserMeal meal : mealList) {
            map.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
            if (meal.getDateTime().toLocalTime().isBefore(endTime) & startTime.isBefore(meal.getDateTime().toLocalTime())) {
                mealsWithTimeFilter.add(meal);
            }
        }
        for (UserMeal meal : mealsWithTimeFilter) {
            if (map.get(meal.getDateTime().toLocalDate()) > caloriesPerDay) {
                mealsWithBothFilters.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
                System.out.println(meal.getDescription() + " " + meal.getDateTime().toLocalDate() + " " + meal.getDateTime().toLocalTime());
            }
        }
        return mealsWithBothFilters;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {


        Map<LocalDate, Integer> map5 = mealList.stream()
                .collect(Collectors.groupingBy(UserMeal::getLocalDate,
                        Collectors.summingInt(UserMeal::getCalories)));
          System.out.println(map5);

         List<LocalDate> localDatelist= map5.entrySet().stream().filter(i ->i.getValue()>caloriesPerDay)
                  .map(i ->i.getKey())
                 .collect(Collectors.toList());
      // System.out.println(localDatelist);

    return  mealList.stream()
            .filter(s -> s.getDateTime().toLocalTime().isBefore(endTime)& startTime.isBefore(s.getDateTime().toLocalTime())
                    & s.isInList(
                            localDatelist))
            .map(i->new UserMealWithExceed(i.getDateTime(),i.getDescription(),i.getCalories(),true) )
            .collect(Collectors.toList());
    }
}
