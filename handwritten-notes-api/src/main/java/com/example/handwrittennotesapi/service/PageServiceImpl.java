package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.PageInfo;
import com.example.handwrittennotesapi.dto.PageUploadResponse;
import com.example.handwrittennotesapi.model.NotebookInstance;
import com.example.handwrittennotesapi.model.Page;
import com.example.handwrittennotesapi.model.User;
import com.example.handwrittennotesapi.repository.NotebookInstanceRepository;
import com.example.handwrittennotesapi.repository.PageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class PageServiceImpl implements PageService {

    private final PageRepository pageRepository;
    private final NotebookInstanceRepository notebookInstanceRepository;
    private final UserService userService;
    private final FileStorageService fileStorageService;

    @Autowired
    public PageServiceImpl(PageRepository pageRepository,
                           NotebookInstanceRepository notebookInstanceRepository,
                           UserService userService,
                           FileStorageService fileStorageService) {
        this.pageRepository = pageRepository;
        this.notebookInstanceRepository = notebookInstanceRepository;
        this.userService = userService;
        this.fileStorageService = fileStorageService;
    }

    @Override
    @Transactional
    public PageUploadResponse uploadPage(MultipartFile imageFile, String notebookInstanceId, String clientPageId) {
        User currentUser = userService.getCurrentUser();

        if (clientPageId != null) {
            Optional<Page> existingPage = pageRepository.findByUserIdAndClientPageId(currentUser.getId(), clientPageId);
            if (existingPage.isPresent()) {
                PageUploadResponse response = new PageUploadResponse();
                response.setPageId(String.valueOf(existingPage.get().getId()));
                response.setStatus("EXISTING");
                return response;
            }
        }

        String imageUrl = fileStorageService.storeFile(imageFile);

        Page page = new Page();
        page.setUser(currentUser);
        page.setImageUrl(imageUrl);
        page.setClientPageId(clientPageId);
        page.setCreatedAt(LocalDateTime.now());

        if (notebookInstanceId != null) {
            NotebookInstance notebookInstance = notebookInstanceRepository.findById(Long.parseLong(notebookInstanceId))
                    .orElseThrow(() -> new IllegalArgumentException("Notebook instance not found"));
            if (!notebookInstance.getUser().getId().equals(currentUser.getId())) {
                throw new SecurityException("User does not have permission to access this notebook instance");
            }
            page.setNotebookInstance(notebookInstance);
        }

        page = pageRepository.save(page);

        PageUploadResponse response = new PageUploadResponse();
        response.setPageId(String.valueOf(page.getId()));
        response.setStatus("STORED");
        return response;
    }

    @Override
    public List<PageInfo> listPages() {
        User currentUser = userService.getCurrentUser();
        return pageRepository.findByUserId(currentUser.getId()).stream()
                .map(page -> {
                    PageInfo pageInfo = new PageInfo();
                    pageInfo.setPageId(String.valueOf(page.getId()));
                    pageInfo.setCreatedAt(page.getCreatedAt());
                    // In a real app, we'd look up the OCR status
                    pageInfo.setOcrStatus(com.example.handwrittennotesapi.model.OcrJobStatus.NOT_REQUESTED);
                    return pageInfo;
                })
                .collect(Collectors.toList());
    }
}
