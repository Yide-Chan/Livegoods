package com.livegoods.comments.contorller;

import com.livegoods.comments.service.CommentsService;
import com.livegoods.commons.pojo.Result;
import com.livegoods.pojo.Comments;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin
public class CommentsContorller {
    @Autowired
    private CommentsService commentsService;

    @GetMapping("/comment")
    public Result<Comments> getCommentsByHousesId(String id, int page) {
        return commentsService.getCommentsByHousesId(id, page);
    }
}
