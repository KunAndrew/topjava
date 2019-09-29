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
        List<UserMeal> mealsWithTimeFilter = new LinkedList<>();
        List<UserMealWithExceed> mealsWithBothFilters = new LinkedList<>();
        HashMap<LocalDate, Integer> tempMap = new HashMap<>();
        for (UserMeal meal : mealList) {
            //Map match LocalDate to the calories in this day
            tempMap.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), (oldVal, newVal) -> oldVal + newVal);
            if (meal.getDateTime().toLocalTime().isBefore(endTime) & startTime.isBefore(meal.getDateTime().toLocalTime())) {
                mealsWithTimeFilter.add(meal);
            }
        }
        //Check UserMeals calories
        for (UserMeal meal : mealsWithTimeFilter) {
            if (tempMap.get(meal.getDateTime().toLocalDate()) > caloriesPerDay) {
                mealsWithBothFilters.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
                System.out.println(meal.getDescription() + " " + meal.getDateTime().toLocalDate() + " " + meal.getDateTime().toLocalTime());
            }
        }
        return mealsWithBothFilters;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededStream(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        return mealList.stream()
                .filter(s -> s.getDateTime().toLocalTime().isBefore(endTime) & startTime.isBefore(s.getDateTime().toLocalTime())    // time filters part
                                & s.isInList(                                                                                       //calories filters part
                        mealList.stream()                                //stream return List<LocalDate> where calories more then caloriesPerDay
                                .collect(Collectors.groupingBy(UserMeal::getLocalDate,
                                        Collectors.summingInt(UserMeal::getCalories))).entrySet().stream()
                                                .filter(i -> i.getValue() > caloriesPerDay)
                                                .map(i -> i.getKey())
                                                .collect(Collectors.toList())
                        )
                )
                .map(i -> new UserMealWithExceed(i.getDateTime(), i.getDescription(), i.getCalories(), true))
                .collect(Collectors.toList());
    }
}
