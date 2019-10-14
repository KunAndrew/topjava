package ru.javawebinar.topjava.repository.inmemory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.meal.MealRestController;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepository.class);
    private Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private List<Map<Integer, Meal>> repositoryAll = new ArrayList<>();
    private AtomicInteger counter = new AtomicInteger(0);
    private Integer UserId = MealRestController.userId;


    {
        for (Meal MEAL : MealsUtil.MEALS) {
            save(MEAL);
        }
    }


    @Override
    public Meal save(Meal meal) {
        log.info("save {}", meal);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            repositoryAll.add(repository);
            return meal;
        }
        // treat case: update, but not present in storage
        return repositoryAll.get(UserId).computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @Override
    public boolean delete(int id) {
        log.info("delete {}", id);
        return repositoryAll.get(UserId).remove(id) != null;
    }

    @Override
    public Meal get(int id) {
        log.info("get {}", id);
        return repositoryAll.get(UserId).get(id);
    }

    @Override
    public Collection<Meal> getAll() {
        log.info("getAll meals");
        ConcurrentHashMap<Integer, Meal> map = new ConcurrentHashMap<>();
        map = (ConcurrentHashMap<Integer, Meal>) repositoryAll.get(UserId);
        List list = new ArrayList(map.values());
        Collections.sort(list, new Comparator<Meal>() {
            @Override
            public int compare(Meal o1, Meal o2) {
                return o2.getDateTime().compareTo(o1.getDateTime()); //getDate()-sorted only Date
            }
        });

        return list;
    }
}

