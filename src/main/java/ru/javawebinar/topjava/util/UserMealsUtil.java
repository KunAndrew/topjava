package ru.javawebinar.topjava.util;


import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;



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
        getFilteredWithExceeded(mealList, LocalTime.of(7, 0), LocalTime.of(12,0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        HashMap<LocalDate, Integer> caloriesInDay = new HashMap<>();
        for (UserMeal meal : mealList) {
            caloriesInDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum);
        }

        List<UserMealWithExceed> mealsWithBothFilters = new LinkedList<>();
        for (UserMeal meal : mealList) {
            if (TimeUtil.isBetween(meal.getDateTime().toLocalTime(), startTime, endTime)) {
                mealsWithBothFilters.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(),
                        caloriesInDay.get(meal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }
        return mealsWithBothFilters;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        final List daysWithMoreCalories = mealList.stream()          //stream return List<LocalDate> where calories more then caloriesPerDay
                .collect(Collectors.groupingBy(UserMeal::getLocalDate,
                        Collectors.summingInt(UserMeal::getCalories))).entrySet().stream()
                .filter(umc -> umc.getValue() > caloriesPerDay)
                .map(umc -> umc.getKey())
                .collect(Collectors.toList());

        List list = mealList.stream()
                .filter(um -> TimeUtil.isBetween(um.getDateTime().toLocalTime(), startTime, endTime))
                .map(um -> {
                    return new UserMealWithExceed(um.getDateTime(), um.getDescription(), um.getCalories(),
                            daysWithMoreCalories.contains(um.getDateTime().toLocalDate()));})
                .collect(Collectors.toList());
        return list;
    }
}
