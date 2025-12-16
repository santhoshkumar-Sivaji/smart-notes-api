package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.PageInfo;
import com.example.handwrittennotesapi.dto.PageUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface PageService {
    PageUploadResponse uploadPage(MultipartFile imageFile, String notebookInstanceId, String clientPageId);
    List<PageInfo> listPages();
}
