package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.SearchResponse;
import com.example.handwrittennotesapi.model.User;
import com.example.handwrittennotesapi.repository.OcrResultRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SearchServiceImpl implements SearchService {

    private final OcrResultRepository ocrResultRepository;
    private final UserService userService;

    @Autowired
    public SearchServiceImpl(OcrResultRepository ocrResultRepository, UserService userService) {
        this.ocrResultRepository = ocrResultRepository;
        this.userService = userService;
    }

    @Override
    public List<SearchResponse> search(String query) {
        User currentUser = userService.getCurrentUser();
        return ocrResultRepository.searchByUserIdAndQuery(currentUser.getId(), query).stream()
                .map(ocrResult -> {
                    SearchResponse response = new SearchResponse();
                    response.setPageId(String.valueOf(ocrResult.getPage().getId()));
                    // In a real app, we would generate a more intelligent snippet
                    response.setSnippet(ocrResult.getExtractedText().substring(0, Math.min(ocrResult.getExtractedText().length(), 100)));
                    return response;
                })
                .collect(Collectors.toList());
    }
}
