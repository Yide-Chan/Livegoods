package com.livegoods.feedback.dao;

import com.livegoods.pojo.Comments;

public interface FeedbackDao {
    void saveFeedback(Comments comments);

    void removeByComments(Comments comments);
}
