package ru.javawebinar.topjava.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class UserMeal {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    public UserMeal(LocalDateTime dateTime, String description, int calories) {
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return "UserMeal{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }

    public LocalDate getLocalDate() {
        return dateTime.toLocalDate();
    }

    //return true if the condition is satisfied for at least one element
    public boolean isInList(List<LocalDate> collection) {
        UserMeal key = this;
        return collection.stream().anyMatch(x -> x == key.getDateTime().toLocalDate());
    }
}
