package com.livegoods.recommendation.service;

import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Items;

public interface RecommendationService {
    Result<Items> getRecommendations(String city);
}
