package com.example.practice5.service;

import com.example.practice5.model.Category;
import com.example.practice5.model.Post;
import com.example.practice5.model.User;
import com.example.practice5.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class PostService {
    private final PostRepository postRepository;

    @Autowired
    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Optional<Post> findById(long id) {
        return postRepository.findById(id);
    }

    public List<Post> findAllByOrderByPromotionDesc() {
        return postRepository.findAllBySoldOrderByPromotionDescRatingDesc(false);
    }

    public List<Post> findAllFilter(String field, String option) {
        List<Post> posts;
        if (Objects.equals(field, "price") && Objects.equals(option, "asc"))
            posts = postRepository.findAllBySoldOrderByPriceAscPromotionDescRatingDesc(false);
        else if (Objects.equals(field, "price") && Objects.equals(option, "desc"))
            posts = postRepository.findAllBySoldOrderByPriceDescPromotionDescRatingDesc(false);
        else if (Objects.equals(field, "postingDate") && Objects.equals(option, "asc"))
            posts = postRepository.findAllBySoldOrderByPostingDateAscPromotionDescRatingDesc(false);
        else
            posts = postRepository.findAllBySoldOrderByPostingDateDescPromotionDescRatingDesc(false);
        return posts;
    }

    public List<Post> findAllByCategory(Category category) {
        return postRepository.findAllByCategoryAndSoldOrderByPromotionDescRatingDesc(category, false);
    }

    public List<Post> findAllByUserAndSold(User user, boolean sold) {
        return postRepository.findAllByUserAndSoldOrderByPromotionDesc(user, sold);
    }

    public List<Post> findAllByPriceLessThan(double price) {
        return postRepository.findAllBySoldAndPriceLessThanOrderByPromotionDescRatingDesc(false, price);
    }

    public List<Post> findAllByPriceGreaterThan(double price) {
        return postRepository.findAllBySoldAndPriceGreaterThanOrderByPromotionDescRatingDesc(false, price);
    }

    public List<Post> findAllByPostingDateIsBefore(LocalDate postingDate) {
        return postRepository.findAllBySoldAndPostingDateIsBeforeOrderByPromotionDescRatingDesc(false, postingDate);
    }

    public List<Post> findAllByPostingDateIsAfter(LocalDate postingDate) {
        return postRepository.findAllBySoldAndPostingDateIsAfterOrderByPromotionDescRatingDesc(false, postingDate);
    }

    public List<Post> findAllByTitleContainingIgnoreCase(String title) {
        return postRepository.findAllBySoldAndTitleContainingIgnoreCaseOrderByPromotionDescRatingDesc(false, title);
    }

    @Transactional
    public int updatePostSetRatingForUser(Integer rating, User user) {
        return postRepository.updatePostSetRatingForUser(rating, user);
    }

    @Transactional
    public void create(Post post) {
        postRepository.save(post);
    }

    @Transactional
    public void delete(Post post) {
        postRepository.delete(post);
    }

    @Transactional
    public void update(Post post) {
        postRepository.save(post);
    }
}
