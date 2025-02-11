package org.example.proyectotareas;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class TaskController {

    @RequestMapping("/")
    public String index(Model model, HttpSession session) {
        List<String> tasks = (List<String>) session.getAttribute("tasks");
        if (tasks == null) {
            tasks = new ArrayList<>();
            session.setAttribute("tasks", tasks);
        }
        model.addAttribute("tasks", tasks);
        return "index";
    }

    @RequestMapping("/addTask")
    public String addTask(@RequestParam String title, Model model, HttpSession session) {
        List<String> tasks = (List<String>) session.getAttribute("tasks");
        if (tasks == null) {
            tasks = new ArrayList<>();
            session.setAttribute("tasks", tasks);
        }

        if (tasks.contains(title)) {
            model.addAttribute("errorMessage", "¡La tarea ya existe!");
            return "index";
        }

        tasks.add(title);
        session.setAttribute("tasks", tasks);
        return "redirect:/";
    }

    @RequestMapping("/markCompleted")
    public String markCompleted(@RequestParam int index, HttpSession session) {
        List<String> tasks = (List<String>) session.getAttribute("tasks");
        if (tasks == null || tasks.size() <= index) {
            return "redirect:/";
        }

        String task = tasks.get(index);
        if (!task.contains("(Completada)")) {
            tasks.set(index, task + " (Completada)");
        }
        session.setAttribute("tasks", tasks);
        return "redirect:/";
    }

    @RequestMapping("/deleteTask")
    public String deleteTask(@RequestParam int index, HttpSession session) {
        List<String> tasks = (List<String>) session.getAttribute("tasks");
        if (tasks == null || tasks.size() <= index) {
            return "redirect:/";
        }

        tasks.remove(index);
        session.setAttribute("tasks", tasks);
        return "redirect:/";
    }

    @RequestMapping("/summary")
    public String summary(Model model, HttpSession session) {
        List<String> tasks = (List<String>) session.getAttribute("tasks");
        if (tasks == null) {
            tasks = new ArrayList<>();
            session.setAttribute("tasks", tasks);
        }

        long completedCount = tasks.stream().filter(task -> task.contains("(Completada)")).count();
        long pendingCount = tasks.size() - completedCount;

        model.addAttribute("completedCount", completedCount);
        model.addAttribute("pendingCount", pendingCount);
        model.addAttribute("totalCount", tasks.size());

        return "summary";
    }
}
