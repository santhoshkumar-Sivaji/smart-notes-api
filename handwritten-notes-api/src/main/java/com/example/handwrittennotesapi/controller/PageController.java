package com.example.handwrittennotesapi.controller;

import com.example.handwrittennotesapi.dto.PageInfo;
import com.example.handwrittennotesapi.dto.PageUploadResponse;
import com.example.handwrittennotesapi.service.PageService;
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
public class PageController {

    private final PageService pageService;

    @Autowired
    public PageController(PageService pageService) {
        this.pageService = pageService;
    }

    @PostMapping
    public ResponseEntity<PageUploadResponse> uploadPage(
            @RequestParam("imageFile") MultipartFile imageFile,
            @RequestParam(value = "notebookInstanceId", required = false) String notebookInstanceId,
            @RequestParam(value = "clientPageId", required = false) String clientPageId
    ) {
        return ResponseEntity.ok(pageService.uploadPage(imageFile, notebookInstanceId, clientPageId));
    }

    @GetMapping
    public ResponseEntity<List<PageInfo>> listPages() {
        return ResponseEntity.ok(pageService.listPages());
    }
}
