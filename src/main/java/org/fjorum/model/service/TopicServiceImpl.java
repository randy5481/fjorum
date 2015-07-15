package org.fjorum.model.service;

import org.fjorum.controller.form.TopicCreateForm;
import org.fjorum.model.entity.Category;
import org.fjorum.model.entity.Topic;
import org.fjorum.model.entity.User;
import org.fjorum.model.repository.TopicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

@Service
public class TopicServiceImpl extends AbstractEntityServiceImpl<Topic> implements TopicService {

    private final TopicRepository topicRepository;
    private final CategoryService categoryService;
    private final UserService userService;

    @Autowired
    public TopicServiceImpl(TopicRepository topicRepository,
                            CategoryService categoryService,
                            UserService userService) {
        this.topicRepository = topicRepository;
        this.categoryService = categoryService;
        this.userService = userService;
    }


    @Override
    public Topic createNewTopic(TopicCreateForm form) {
        return categoryService.getById(form.getCategoryId())
                .<Topic>map(category -> userService.getById(form.getUserId())
                        .map(user -> createNewTopic(category, user, form.getName()))
                        .orElseThrow(() -> new DataIntegrityViolationException("User not found")))
                .orElseThrow(() -> new DataIntegrityViolationException("Category not found"));
    }

    @Override
    public Topic createNewTopic(Category category, User user, String name) {
        Topic topic = new Topic(category, user, name);
        return topicRepository.save(topic);
    }

    @Override
    protected JpaRepository<Topic, Long> getRepository() {
        return topicRepository;
    }
}
