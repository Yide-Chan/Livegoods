package com.livegoods.search.service;

import com.livegoods.commons.pojo.Result;
import com.livegoods.search.pojo.House4ES;

public interface SearchService {
    boolean init();
    Result<House4ES> search(String city, String content, int page);
}
