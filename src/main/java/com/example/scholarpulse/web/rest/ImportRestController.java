package com.example.scholarpulse.web.rest;

import com.example.scholarpulse.application.dto.importing.ImportResponse;
import com.example.scholarpulse.application.service.ImportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/import")
@RequiredArgsConstructor
public class ImportRestController {

    private final ImportService importService;

    @PostMapping("/topics/{topicId}")
    public ImportResponse importForTopic(
            @PathVariable Long topicId,
            @RequestParam(defaultValue = "10") int limit
    ) {
        return importService.importArticlesForTopic(topicId, limit);
    }
}
