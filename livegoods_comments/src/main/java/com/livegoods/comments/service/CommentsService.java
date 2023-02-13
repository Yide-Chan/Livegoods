package com.livegoods.comments.service;

import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Comments;

public interface CommentsService {
    Result<Comments> getCommentsByHousesId(String id, int page);
}
