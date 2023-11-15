package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;


@Controller
@RequestMapping(value = "/meals")
public class JspMealController extends AbstractMealController{

    protected JspMealController(MealService service) {
        super(service);
    }

    @GetMapping
    public String getAll(Model model) {
        List<MealTo> mealTos = super.getAll();
        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("/filter")
    public String getAllFiltered(HttpServletRequest request, Model model) {
        LocalDate startDate = parseLocalDate(request.getParameter("startDate"));
        LocalDate endDate = parseLocalDate(request.getParameter("endDate"));
        LocalTime startTime = parseLocalTime(request.getParameter("startTime"));
        LocalTime endTime = parseLocalTime(request.getParameter("endTime"));
        List<MealTo> mealTos = super.getBetween(startDate, startTime, endDate, endTime);
        model.addAttribute("meals", mealTos);
        return "meals";
    }

    @GetMapping("/edit")
    public String edit(HttpServletRequest request, Model model) {
        final Meal meal = super.get(getId(request));
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/add")
    public String add(Model model) {
        final Meal meal = new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000);
        model.addAttribute("meal", meal);
        return "mealForm";
    }

    @GetMapping("/delete")
    public String delete(HttpServletRequest request) {
        super.delete(getId(request));
        return "redirect:/meals";
    }

    @PostMapping
    public String save(HttpServletRequest request) throws UnsupportedEncodingException {
        Meal meal = new Meal(
                LocalDateTime.parse(request.getParameter("dateTime")),
                request.getParameter("description"),
                Integer.parseInt(request.getParameter("calories")));

        if (StringUtils.hasLength(request.getParameter("id"))) {
            super.update(meal, getId(request));
        } else {
            super.create(meal);
        }
        return "redirect:meals";
    }

    private int getId(HttpServletRequest request) {
        String paramId = Objects.requireNonNull(request.getParameter("id"));
        return Integer.parseInt(paramId);
    }
}