package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

    public static List<UserMealWithExceed>  getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        // TODO return filtered list with correctly exceeded field
        List<UserMealWithExceed> list =new ArrayList<>();
        for (UserMeal meal:mealList) {
            if (meal.getDateTime().toLocalTime().isBefore(endTime) & startTime.isBefore(meal.getDateTime().toLocalTime()) &
                    sum(meal.getDateTime(), caloriesPerDay, mealList)) {
                list.add(new UserMealWithExceed(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
             //   System.out.println(meal.getDateTime()+" "+meal.getDescription());
            }
        }
        return list;
    }

    private static boolean sum(LocalDateTime dateTime, int caloriesPerDay, List<UserMeal> mealList) {
        //List<UserMeal> list =new ArrayList<>();
        int index=0;
        for (UserMeal meal:mealList) {
          if (meal.getDateTime().toLocalDate().equals(dateTime.toLocalDate())) {
            //  list.add(meal);
              index+=meal.getCalories();
          }

        }
      //  System.out.println(index);
        if (index>caloriesPerDay)return true;
        else return false;
    }
}
