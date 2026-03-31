package com.example.scholarpulse.web.mvc;

import com.example.scholarpulse.application.service.CollectionService;
import com.example.scholarpulse.application.service.TopicService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class ViewController {

    private final TopicService topicService;
    private final CollectionService collectionService;

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("topics", topicService.findAll());
        model.addAttribute("collections", collectionService.findAll());
        return "index";
    }

    @GetMapping("/topics")
    public String topics(Model model) {
        model.addAttribute("topics", topicService.findAll());
        return "topics";
    }

    @GetMapping("/collections")
    public String collections(Model model) {
        model.addAttribute("collections", collectionService.findAll());
        return "collections";
    }
}
