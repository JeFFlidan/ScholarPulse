package com.example.scholarpulse.infrastructure.client;

import com.example.scholarpulse.application.port.out.ArticleImportClient;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Component
public class OpenAlexArticleImportClient implements ArticleImportClient {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Value("${scholarpulse.openalex.base-url}")
    private String baseUrl;

    @Value("${scholarpulse.openalex.api-key:}")
    private String apiKey;

    private HttpClient httpClient;

    @PostConstruct
    void init() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(10))
                .build();
    }

    @Override
    public List<ImportedArticle> searchWorks(String query, int limit) {
        System.out.println("In search works");
        URI uri = buildSearchUri(query, limit);
        System.out.println(uri);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(uri)
                .GET()
                .timeout(Duration.ofSeconds(20))
                .header("Accept", "application/json")
                .build();
        System.out.println("after search works");
        try {
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() >= 400) {
                throw new IllegalStateException("OpenAlex request failed with status: " + response.statusCode());
            }

            return parseResponse(response.body());
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new IllegalStateException("OpenAlex request was interrupted", e);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to import articles from OpenAlex", e);
        }
    }

    private URI buildSearchUri(String query, int limit) {
        System.out.println(apiKey);

        UriComponentsBuilder builder = UriComponentsBuilder
                .fromUriString(baseUrl + "/works")
                .queryParam("search", query)
                .queryParam("per-page", limit);
        System.out.println("Before if");
        if (apiKey != null && !apiKey.isBlank()) {
            builder.queryParam("api_key", apiKey);
        }
        System.out.println("After if");
        System.out.println(query);

        return builder.encode().build().toUri();
    }

    private List<ImportedArticle> parseResponse(String body) throws IOException {
        JsonNode root = objectMapper.readTree(body);
        JsonNode results = root.path("results");

        List<ImportedArticle> importedArticles = new ArrayList<>();
        if (!results.isArray()) {
            return importedArticles;
        }

        for (JsonNode item : results) {
            String externalId = text(item, "id");
            String title = text(item, "display_name");
            String authors = extractAuthors(item.path("authorships"));
            String abstractText = reconstructAbstract(item.path("abstract_inverted_index"));
            Integer publicationYear = item.path("publication_year").isInt() ? item.path("publication_year").asInt() : null;
            String doi = normalizeDoi(text(item, "doi"));
            String url = extractLandingPageUrl(item);
            String sourceName = extractSourceName(item);
            Integer citationCount = item.path("cited_by_count").isInt() ? item.path("cited_by_count").asInt() : null;

            if (title == null || title.isBlank()) {
                continue;
            }

            importedArticles.add(new ImportedArticle(
                    externalId,
                    title,
                    authors,
                    abstractText,
                    publicationYear,
                    doi,
                    url,
                    sourceName,
                    citationCount
            ));
        }

        return importedArticles;
    }

    private String extractAuthors(JsonNode authorshipsNode) {
        if (!authorshipsNode.isArray()) {
            return null;
        }

        List<String> names = new ArrayList<>();
        for (JsonNode authorship : authorshipsNode) {
            JsonNode authorNode = authorship.path("author");
            String name = text(authorNode, "display_name");
            if (name != null && !name.isBlank()) {
                names.add(name);
            }
        }

        return names.isEmpty() ? null : String.join(", ", names);
    }

    private String reconstructAbstract(JsonNode abstractInvertedIndex) {
        if (!abstractInvertedIndex.isObject() || abstractInvertedIndex.isEmpty()) {
            return null;
        }

        int maxIndex = -1;
        Iterator<Map.Entry<String, JsonNode>> fields = abstractInvertedIndex.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            for (JsonNode indexNode : entry.getValue()) {
                if (indexNode.isInt()) {
                    maxIndex = Math.max(maxIndex, indexNode.asInt());
                }
            }
        }

        if (maxIndex < 0) {
            return null;
        }

        String[] words = new String[maxIndex + 1];
        fields = abstractInvertedIndex.fields();

        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String word = entry.getKey();
            for (JsonNode indexNode : entry.getValue()) {
                if (indexNode.isInt()) {
                    int position = indexNode.asInt();
                    if (position >= 0 && position < words.length) {
                        words[position] = word;
                    }
                }
            }
        }

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word == null) {
                continue;
            }
            if (!builder.isEmpty()) {
                builder.append(' ');
            }
            builder.append(word);
        }

        return builder.isEmpty() ? null : builder.toString();
    }

    private String extractLandingPageUrl(JsonNode item) {
        JsonNode primaryLocation = item.path("primary_location");
        String url = text(primaryLocation, "landing_page_url");
        if (url != null && !url.isBlank()) {
            return url;
        }
        return text(item, "id");
    }

    private String extractSourceName(JsonNode item) {
        JsonNode sourceNode = item.path("primary_location").path("source");
        return text(sourceNode, "display_name");
    }

    private String normalizeDoi(String doi) {
        if (doi == null || doi.isBlank()) {
            return null;
        }
        return doi.replace("https://doi.org/", "").trim();
    }

    private String text(JsonNode node, String fieldName) {
        JsonNode field = node.path(fieldName);
        return field.isMissingNode() || field.isNull() ? null : field.asText();
    }
}
