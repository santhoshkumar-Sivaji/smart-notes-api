package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.PageInfo;
import com.example.handwrittennotesapi.dto.PageUploadResponse;
import com.example.handwrittennotesapi.service.PageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/pages")
@Tag(name = "Page Management", description = "APIs for managing pages")
public class PageController {

    private final PageService pageService;

    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @PostMapping
    @Operation(summary = "Upload page", description = "Uploads an image file as a page")
    @ApiResponse(responseCode = "200", description = "Page uploaded successfully")
    @ApiResponse(responseCode = "400", description = "Invalid file or parameters")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<PageUploadResponse> uploadPage(
            @Parameter(description = "Image file to upload") @RequestParam("imageFile") MultipartFile imageFile,
            @Parameter(description = "Optional notebook instance ID") @RequestParam(value = "notebookInstanceId", required = false) String notebookInstanceId,
            @Parameter(description = "Optional client page ID") @RequestParam(value = "clientPageId", required = false) String clientPageId
    ) {
        return ResponseEntity.ok(pageService.uploadPage(imageFile, notebookInstanceId, clientPageId));
    }

    @GetMapping
    @Operation(summary = "List pages", description = "Retrieves a list of pages for the authenticated user")
    @ApiResponse(responseCode = "200", description = "Pages retrieved successfully")
    @ApiResponse(responseCode = "401", description = "Unauthorized")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<List<PageInfo>> listPages() {
        return ResponseEntity.ok(pageService.listPages());
    }
}
