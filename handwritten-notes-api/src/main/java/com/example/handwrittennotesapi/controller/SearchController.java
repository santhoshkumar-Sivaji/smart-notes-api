package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.SearchResponse;
import com.example.handwrittennotesapi.service.SearchService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/search")
@Tag(name = "Search", description = "APIs for searching content")
public class SearchController {

    private final SearchService searchService;

    @Autowired
    public SearchController(SearchService searchService) {
        this.searchService = searchService;
    }

    @GetMapping
    @Operation(summary = "Search content", description = "Searches for content based on the query")
    @ApiResponse(responseCode = "200", description = "Search results retrieved")
    @ApiResponse(responseCode = "400", description = "Invalid query")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<SearchResponse>> search(@Parameter(description = "Search query") @RequestParam("q") String query) {
        return ResponseEntity.ok(searchService.search(query));
    }
}
