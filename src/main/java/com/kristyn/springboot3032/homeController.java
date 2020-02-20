package com.kristyn.springboot3032;

import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.Map;

@Controller
public class homeController {
    @Autowired
    TodoRepository todoRepository;

    @Autowired
    CloudinaryConfig cloudc;

    @RequestMapping("/")
    public String listTodos(Model model){
        model.addAttribute("todos", todoRepository.findAll());
        return "list";
    }
    @RequestMapping("/about")
    public String about() {
        return "about";
    }
    @GetMapping("/add")
    public String todoForm(Model model){
        model.addAttribute("todo", new Todo());
        return "todoform";
    }
/*    @PostMapping("/process")
    public String processForm(@Valid Todo todo, BindingResult result){
        if(result.hasErrors()){
            return "todoform";
        }
        todoRepository.save(todo);
        return "redirect:/";
    }*/
    @PostMapping("/add")
    public String processActor(@Valid @ModelAttribute Todo todo,BindingResult result, @RequestParam("file") MultipartFile image){
        if(result.hasErrors()){
            System.out.println("im here in result");
            return "todoform";
        }
        if(image.isEmpty()){
            System.out.println("im here in image");
            return "redirect:/add";
        }
        try {
            Map uploadResult = cloudc.upload(image.getBytes(),
                    ObjectUtils.asMap("resourcetype", "auto"));
            todo.setImage(uploadResult.get("url").toString());
            todoRepository.save(todo);
        } catch (IOException e) {
            e.printStackTrace();
            return "redirect:/add";
        }
        return "redirect:/";
    }
    @RequestMapping("/detail/{id}")
    public String showTodo(@PathVariable("id") long id, Model model){
        model.addAttribute("todo", todoRepository.findById(id).get());
        return "show";
    }
    @RequestMapping("/update/{id}")
    public String updateTodo(@PathVariable("id") long id, Model model){
        model.addAttribute("todo", todoRepository.findById(id).get());
        return "todoform";
    }
    @RequestMapping("/delete/{id}")
    public String updateTodo(@PathVariable("id") long id){
        todoRepository.deleteById(id);
        return "redirect:/";
    }
}
