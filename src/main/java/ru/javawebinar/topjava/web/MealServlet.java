package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MealDAO;
import ru.javawebinar.topjava.dao.MealDaoInMemoryImpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final int CALORIES_PER_DAY = 2000;
    private static String INSERT_OR_EDIT = "/editMeal.jsp";
    private static String LIST_MEAL = "/meals.jsp";
    private final MealDAO mealDAO;
    private static final Logger log = getLogger(MealServlet.class);

    public MealServlet() {
        super();
        mealDAO = new MealDaoInMemoryImpl();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        String forward="";
        String action = request.getParameter("action");
        if (action.equalsIgnoreCase("delete")){
            Integer id = Integer.parseInt(request.getParameter("id"));
            mealDAO.delete(id);
            forward = LIST_MEAL;
            List<MealTo> meals = MealsUtil.convertWithExceed(mealDAO.getAll(), CALORIES_PER_DAY);
            request.setAttribute("meals", meals);
//            response.sendRedirect("meals.jsp");
        } else if (action.equalsIgnoreCase("edit")){
            forward = INSERT_OR_EDIT;
            Integer id = Integer.parseInt(request.getParameter("id"));
            Meal meal = mealDAO.get(id);
            request.setAttribute("meal", meal);
        } else if (action.equalsIgnoreCase("listMeal")){
            forward = LIST_MEAL;
            List<MealTo> meals = MealsUtil.convertWithExceed(mealDAO.getAll(), CALORIES_PER_DAY);
            request.setAttribute("meals", meals);
        } else {
            forward = INSERT_OR_EDIT;
        }
        RequestDispatcher view = request.getRequestDispatcher(forward);
        log.debug("forward to " + forward);
        view.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        Meal meal = new Meal();
        meal.setDateTime(LocalDateTime.parse(request.getParameter("datetime")));
        meal.setDescription(request.getParameter("description"));
        meal.setCalories(Integer.parseInt(request.getParameter("calories")));
        String id = request.getParameter("id");
        if (id == null || id.isEmpty()) {
            mealDAO.save(meal);
        } else {
            meal.setId(Integer.parseInt(id));
            mealDAO.save(meal);
        }
        RequestDispatcher view = request.getRequestDispatcher(LIST_MEAL);
        List<MealTo> meals = MealsUtil.convertWithExceed(mealDAO.getAll(),CALORIES_PER_DAY);
        request.setAttribute("meals", meals);
        view.forward(request, response);
    }
}