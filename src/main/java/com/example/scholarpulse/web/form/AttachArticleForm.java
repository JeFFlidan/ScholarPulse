package com.example.scholarpulse.web.form;

import jakarta.validation.constraints.NotNull;

public class AttachArticleForm {

    @NotNull(message = "Please select an article")
    private Long articleId;

    public AttachArticleForm() {
    }

    public AttachArticleForm(Long articleId) {
        this.articleId = articleId;
    }

    public Long getArticleId() {
        return articleId;
    }

    public void setArticleId(Long articleId) {
        this.articleId = articleId;
    }
}
