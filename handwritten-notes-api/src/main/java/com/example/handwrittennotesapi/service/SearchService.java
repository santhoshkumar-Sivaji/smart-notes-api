package com.example.handwrittennotesapi.service;

import com.example.handwrittennotesapi.dto.SearchResponse;

import java.util.List;

public interface SearchService {
    List<SearchResponse> search(String query);
}
