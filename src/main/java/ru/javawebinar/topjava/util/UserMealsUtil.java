package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> meals = Arrays.asList(
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 0, 0), "Еда на граничное значение", 100),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2020, Month.JANUARY, 31, 20, 0), "Ужин", 410)
        );

        List<UserMealWithExcess> mealsTo = filteredByCycles(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000);
        mealsTo.forEach(System.out::println);

        System.out.println(filteredByStreams(meals, LocalTime.of(7, 0), LocalTime.of(12, 0), 2000));
    }

    public static List<UserMealWithExcess> filteredByCycles(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<Integer, Integer> map = new TreeMap<>();

        List<UserMealWithExcess> excessList = new ArrayList<>();

        for (UserMeal meal : meals){
            if (!map.containsKey(meal.getDateTime().getDayOfMonth())){
                map.put(meal.getDateTime().getDayOfMonth(), meal.getCalories());
            }
            else {
                int sum = map.get(meal.getDateTime().getDayOfMonth()) + meal.getCalories();
                map.put(meal.getDateTime().getDayOfMonth(), sum);
            }
        }

        for (UserMeal meal : meals){
            if (TimeUtil.isBetweenHalfOpen(LocalTime.of(meal.getDateTime().getHour(), meal.getDateTime().getMinute()), startTime, endTime)){
                if (map.getOrDefault(meal.getDateTime().getDayOfMonth(), caloriesPerDay)>caloriesPerDay){
                    excessList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), true));
                }
                else {
                    excessList.add(new UserMealWithExcess(meal.getDateTime(), meal.getDescription(), meal.getCalories(), false));
                }
            }
        }
        return excessList;
    }

    public static List<UserMealWithExcess> filteredByStreams(List<UserMeal> meals, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {
        Map<Integer, Integer> map = meals.stream()
                .collect(Collectors.groupingBy(userMeal -> userMeal.getDateTime().getDayOfMonth(), Collectors.summingInt(UserMeal::getCalories)));

        List<UserMealWithExcess> trueList = meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(LocalTime.of(userMeal.getDateTime().getHour(), userMeal.getDateTime().getMinute()), startTime, endTime) &&
                        map.getOrDefault(userMeal.getDateTime().getDayOfMonth(), caloriesPerDay)>caloriesPerDay)
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), true))
                .collect(Collectors.toList());

        List<UserMealWithExcess> falseList =  meals.stream()
                .filter(userMeal -> TimeUtil.isBetweenHalfOpen(LocalTime.of(userMeal.getDateTime().getHour(), userMeal.getDateTime().getMinute()), startTime, endTime) &&
                        map.getOrDefault(userMeal.getDateTime().getDayOfMonth(), caloriesPerDay)<=caloriesPerDay)
                .map(userMeal -> new UserMealWithExcess(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), false))
                .collect(Collectors.toList());

        return Stream.concat(trueList.stream(), falseList.stream()).collect(Collectors.toList());
    }
}
