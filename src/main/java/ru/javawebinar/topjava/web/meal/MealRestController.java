package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.SecurityUtil;

import java.util.Collection;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());
    public static Integer userId= SecurityUtil.authUserId();

    @Autowired
    private MealService service;

    public Collection<MealTo> getAll() {
        log.info("getAll in MealRestController");
        return MealsUtil.getTos(service.getAll(), MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get(int id) {
        return service.get(id);
    }


    public Meal create(Meal meal) {
        return service.create(meal);
    }


    public void delete(int id) {
        service.delete(id);
    }


    public void update(Meal meal) {
        service.update(meal);
    }


}