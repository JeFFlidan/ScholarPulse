package com.example.scholarpulse.web.mvc;

import com.example.scholarpulse.application.dto.article.ArticleResponse;
import com.example.scholarpulse.application.dto.article.ArticleUpdateRequest;
import com.example.scholarpulse.application.dto.collection.CollectionCreateRequest;
import com.example.scholarpulse.application.dto.collection.CollectionResponse;
import com.example.scholarpulse.application.dto.collection.CollectionUpdateRequest;
import com.example.scholarpulse.application.dto.importing.ImportResponse;
import com.example.scholarpulse.application.dto.topic.TopicCreateRequest;
import com.example.scholarpulse.application.dto.topic.TopicResponse;
import com.example.scholarpulse.application.dto.topic.TopicUpdateRequest;
import com.example.scholarpulse.application.service.ArticleService;
import com.example.scholarpulse.application.service.CollectionService;
import com.example.scholarpulse.application.service.ImportService;
import com.example.scholarpulse.application.service.TopicService;
import com.example.scholarpulse.web.form.ArticleForm;
import com.example.scholarpulse.web.form.AttachArticleForm;
import com.example.scholarpulse.web.form.CollectionForm;
import com.example.scholarpulse.web.form.TopicForm;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.Set;

@Controller
@RequiredArgsConstructor
@RequestMapping("/manage")
public class ManagementPageController {

    private static final int DEFAULT_PAGE_SIZE = 100;

    private final TopicService topicService;
    private final ArticleService articleService;
    private final CollectionService collectionService;
    private final ImportService importService;

    @GetMapping("/topics/new")
    public String newTopicPage(Model model) {
        model.addAttribute("topicForm", new TopicForm());
        model.addAttribute("pageTitle", "Create Topic");
        model.addAttribute("formAction", "/manage/topics");
        model.addAttribute("editMode", false);
        return "topic-form";
    }

    @PostMapping("/topics")
    public String createTopic(
            @Valid @ModelAttribute("topicForm") TopicForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create Topic");
            model.addAttribute("formAction", "/manage/topics");
            model.addAttribute("editMode", false);
            return "topic-form";
        }

        TopicResponse created = topicService.create(
                new TopicCreateRequest(form.getName(), form.getDescription(), form.getSearchQuery())
        );

        return "redirect:/manage/topics/" + created.id();
    }

    @GetMapping("/topics/{id}")
    public String topicDetails(@PathVariable Long id, Model model) {
        TopicResponse topic = topicService.findById(id);
        List<ArticleResponse> articles = articleService.findByTopic(id, PageRequest.of(0, DEFAULT_PAGE_SIZE)).getContent();

        model.addAttribute("topic", topic);
        model.addAttribute("articles", articles);
        return "topic-details";
    }

    @GetMapping("/topics/{id}/edit")
    public String editTopicPage(@PathVariable Long id, Model model) {
        TopicResponse topic = topicService.findById(id);

        model.addAttribute("topicForm", new TopicForm(topic.name(), topic.description(), topic.searchQuery()));
        model.addAttribute("pageTitle", "Edit Topic");
        model.addAttribute("formAction", "/manage/topics/" + id);
        model.addAttribute("editMode", true);
        model.addAttribute("topicId", id);
        return "topic-form";
    }

    @PostMapping("/topics/{id}")
    public String updateTopic(
            @PathVariable Long id,
            @Valid @ModelAttribute("topicForm") TopicForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Topic");
            model.addAttribute("formAction", "/manage/topics/" + id);
            model.addAttribute("editMode", true);
            model.addAttribute("topicId", id);
            return "topic-form";
        }

        topicService.update(id, new TopicUpdateRequest(form.getName(), form.getDescription(), form.getSearchQuery()));
        return "redirect:/manage/topics/" + id;
    }

    @PostMapping("/topics/{id}/delete")
    public String deleteTopic(@PathVariable Long id) {
        topicService.delete(id);
        return "redirect:/topics";
    }

    @PostMapping("/topics/{id}/import")
    public String importArticlesForTopic(
            @PathVariable Long id,
            @RequestParam(defaultValue = "10") int limit,
            RedirectAttributes redirectAttributes
    ) {
        ImportResponse response = importService.importArticlesForTopic(id, limit);
        redirectAttributes.addFlashAttribute("successMessage",
                "Imported " + response.importedCount() + " articles for the topic.");
        return "redirect:/manage/topics/" + id;
    }

    @GetMapping("/articles")
    public String articlesPage(
            @RequestParam(required = false) String keyword,
            Model model
    ) {
        List<ArticleResponse> articles = articleService.findAll(keyword, PageRequest.of(0, DEFAULT_PAGE_SIZE)).getContent();
        model.addAttribute("articles", articles);
        model.addAttribute("keyword", keyword == null ? "" : keyword);
        return "article-list";
    }

    @GetMapping("/articles/{id}/edit")
    public String editArticlePage(@PathVariable Long id, Model model) {
        ArticleResponse article = articleService.findById(id);

        model.addAttribute("article", article);
        model.addAttribute("articleForm", new ArticleForm(
                article.title(),
                article.authors(),
                article.abstractText(),
                article.publicationYear(),
                article.doi(),
                article.url(),
                article.sourceName(),
                article.citationCount()
        ));
        return "article-form";
    }

    @PostMapping("/articles/{id}")
    public String updateArticle(
            @PathVariable Long id,
            @Valid @ModelAttribute("articleForm") ArticleForm form,
            BindingResult bindingResult,
            Model model
    ) {
        ArticleResponse current = articleService.findById(id);

        if (bindingResult.hasErrors()) {
            model.addAttribute("article", current);
            return "article-form";
        }

        articleService.update(id, new ArticleUpdateRequest(
                form.getTitle(),
                form.getAuthors(),
                form.getAbstractText(),
                form.getPublicationYear(),
                form.getDoi(),
                form.getUrl(),
                form.getSourceName(),
                form.getCitationCount()
        ));

        return "redirect:/manage/articles";
    }

    @PostMapping("/articles/{id}/delete")
    public String deleteArticle(@PathVariable Long id) {
        articleService.delete(id);
        return "redirect:/manage/articles";
    }

    @GetMapping("/collections/new")
    public String newCollectionPage(Model model) {
        model.addAttribute("collectionForm", new CollectionForm());
        model.addAttribute("pageTitle", "Create Collection");
        model.addAttribute("formAction", "/manage/collections");
        model.addAttribute("editMode", false);
        return "collection-form";
    }

    @PostMapping("/collections")
    public String createCollection(
            @Valid @ModelAttribute("collectionForm") CollectionForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Create Collection");
            model.addAttribute("formAction", "/manage/collections");
            model.addAttribute("editMode", false);
            return "collection-form";
        }

        CollectionResponse created = collectionService.create(
                new CollectionCreateRequest(form.getName(), form.getDescription())
        );

        return "redirect:/manage/collections/" + created.id();
    }

    @GetMapping("/collections/{id}")
    public String collectionDetails(@PathVariable Long id, Model model) {
        CollectionResponse collection = collectionService.findById(id);
        List<ArticleResponse> allArticles = articleService.findAll(null, PageRequest.of(0, DEFAULT_PAGE_SIZE)).getContent();
        Set<Long> attachedArticleIds = Set.copyOf(collection.articleIds());

        List<ArticleResponse> attachedArticles = allArticles.stream()
                .filter(article -> attachedArticleIds.contains(article.id()))
                .toList();

        List<ArticleResponse> availableArticles = allArticles.stream()
                .filter(article -> !attachedArticleIds.contains(article.id()))
                .toList();

        model.addAttribute("collection", collection);
        model.addAttribute("attachedArticles", attachedArticles);
        model.addAttribute("availableArticles", availableArticles);
        model.addAttribute("attachArticleForm", new AttachArticleForm());
        return "collection-details";
    }

    @GetMapping("/collections/{id}/edit")
    public String editCollectionPage(@PathVariable Long id, Model model) {
        CollectionResponse collection = collectionService.findById(id);

        model.addAttribute("collectionForm", new CollectionForm(collection.name(), collection.description()));
        model.addAttribute("pageTitle", "Edit Collection");
        model.addAttribute("formAction", "/manage/collections/" + id);
        model.addAttribute("editMode", true);
        model.addAttribute("collectionId", id);
        return "collection-form";
    }

    @PostMapping("/collections/{id}")
    public String updateCollection(
            @PathVariable Long id,
            @Valid @ModelAttribute("collectionForm") CollectionForm form,
            BindingResult bindingResult,
            Model model
    ) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("pageTitle", "Edit Collection");
            model.addAttribute("formAction", "/manage/collections/" + id);
            model.addAttribute("editMode", true);
            model.addAttribute("collectionId", id);
            return "collection-form";
        }

        collectionService.update(id, new CollectionUpdateRequest(form.getName(), form.getDescription()));
        return "redirect:/manage/collections/" + id;
    }

    @PostMapping("/collections/{id}/delete")
    public String deleteCollection(@PathVariable Long id) {
        collectionService.delete(id);
        return "redirect:/collections";
    }

    @PostMapping("/collections/{id}/articles")
    public String attachArticleToCollection(
            @PathVariable Long id,
            @Valid @ModelAttribute("attachArticleForm") AttachArticleForm form,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please select an article.");
            return "redirect:/manage/collections/" + id;
        }

        collectionService.addArticle(id, form.getArticleId());
        return "redirect:/manage/collections/" + id;
    }

    @PostMapping("/collections/{collectionId}/articles/{articleId}/delete")
    public String detachArticleFromCollection(
            @PathVariable Long collectionId,
            @PathVariable Long articleId
    ) {
        collectionService.removeArticle(collectionId, articleId);
        return "redirect:/manage/collections/" + collectionId;
    }
}
