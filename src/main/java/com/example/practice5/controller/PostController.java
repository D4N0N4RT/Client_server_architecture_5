package com.example.practice5.controller;

import com.example.practice5.dto.CommentDTO;
import com.example.practice5.dto.CreatePostDTO;
import com.example.practice5.dto.GetPostDTO;
import com.example.practice5.dto.UpdatePostDTO;
import com.example.practice5.exception.PriceValidException;
import com.example.practice5.exception.WrongAuthorityException;
import com.example.practice5.exception.WrongIdException;
import com.example.practice5.mapper.PostMapper;
import com.example.practice5.model.Category;
import com.example.practice5.model.Comment;
import com.example.practice5.model.Post;
import com.example.practice5.model.User;
import com.example.practice5.security.JwtTokenProvider;
import com.example.practice5.service.CommentService;
import com.example.practice5.service.PostService;
import com.example.practice5.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/posts")
public class PostController {
    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final JwtTokenProvider jwtTokenProvider;
    private final PostMapper postMapper;

    @Autowired
    public PostController(PostService postService, UserService userService, CommentService commentService,
                          JwtTokenProvider jwtTokenProvider, PostMapper postMapper) {
        this.postService = postService;
        this.userService = userService;
        this.commentService = commentService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.postMapper = postMapper;
    }

    @GetMapping("")
    public ResponseEntity<?> getAll() {
        List<Post> posts = postService.findAllByOrderByPromotionDesc();
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/by_user/sold")
    public ResponseEntity<?> getSoldPostsByUser(@RequestParam(name="email") @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        List<Post> posts = postService.findAllByUserAndSold(user, true);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/by_user/available")
    public ResponseEntity<?> getAvailablePostsByUser(@RequestParam(name="email") @NotBlank String email) {
        User user = (User) userService.loadUserByUsername(email);
        List<Post> posts = postService.findAllByUserAndSold(user, false);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/category/{category}")
    public ResponseEntity<?> getAllByCategory(@PathVariable(name="category") @NotNull Category category) {
        List<Post> posts = postService.findAllByCategory(category);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/sort/{field}/{order}")
    public ResponseEntity<?> getAllSort(@PathVariable(name="field") @NotBlank String field,
                                        @PathVariable(name="order") @NotBlank String order) {
        List<Post> posts = postService.findAllFilter(field, order);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/filter/price/{option}")
    public ResponseEntity<?> getAllFilterPrice(@RequestParam(name="price") double price,
                                               @PathVariable(name="option") @NotBlank String option) {
        List<Post> posts;
        if (Objects.equals(option, "less"))
            posts = postService.findAllByPriceLessThan(price);
        else
            posts = postService.findAllByPriceGreaterThan(price);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/filter/date/{option}")
    public ResponseEntity<?> getAllFilterDate(@RequestParam(name="date") LocalDate date,
                                              @PathVariable(name="option") String option) {
        List<Post> posts;
        if (Objects.equals(option, "before"))
            posts = postService.findAllByPostingDateIsBefore(date);
        else
            posts = postService.findAllByPostingDateIsAfter(date);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByTitle(@RequestParam @NotBlank String title) {
        List<Post> posts = postService.findAllByTitleContainingIgnoreCase(title);
        List<GetPostDTO> dtos = posts.stream().map(Post::fromPost).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable(name="id") long id) throws WrongIdException {
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        GetPostDTO dto = post.fromPost();
        return new ResponseEntity<>(dto, HttpStatus.FOUND);
    }

    @PostMapping("")
    public ResponseEntity<?> createPost(@RequestBody @Valid CreatePostDTO dto, HttpServletRequest request) {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = dto.toPost(user);
        postService.create(post);
        return new ResponseEntity<>("???????? ???????????????????? ??????????????", HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deletePost(@PathVariable(name="id") long id, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            postService.delete(post);
            return new ResponseEntity<>("???????????????????? ??????????????", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("???? ???? ???????????? ?????????????? ???????????? ????????????????????");
        }
    }

    @PatchMapping("edit/{id}")
    public ResponseEntity<?> editPost(@PathVariable(name="id") long id, @RequestBody @Valid UpdatePostDTO dto, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException, PriceValidException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            if (dto.getPrice() < 0)
                throw new PriceValidException("???????????? ??????????????????, ?????????????????? ?????????????????? ????????????\n????????????: ???????? ???????????? ???????? ???? ???????????? 1");
            postMapper.updatePostFromDto(dto, post);
            postService.update(post);
            return new ResponseEntity<>("???????????? ???????????????????? ??????????????????", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("???? ???? ???????????? ???????????????? ???????????? ????????????????????");
        }
    }

    @PatchMapping("/buy/{id}")
    public ResponseEntity<?> buyPost(@PathVariable(name="id") long id, @RequestParam int grade, HttpServletRequest request)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        if (!Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            post.setSold(true);
            postService.update(post);
            User seller = post.getUser();
            seller.setRating(seller.getRating() + grade);
            userService.update(seller);
            postService.updatePostSetRatingForUser(seller.getRating(), seller);
            return new ResponseEntity<>("???????????????????? ??????????????", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("???? ???? ???????????? ???????????????? ???????????? ????????????????????");
        }
    }

    @PatchMapping("/promote/{id}")
    public ResponseEntity<?> promotePost(@PathVariable(name="id") long id, HttpServletRequest request,
                                         @RequestParam(name="promotion") double promotion)
            throws WrongIdException, WrongAuthorityException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        if (Objects.equals(post.getUser().getUsername(), user.getUsername())) {
            post.setPromotion(promotion);
            postService.update(post);
            return new ResponseEntity<>("???? ???????????????????? ?????????????????????? ???????????????????? ?? ???????? ????????????", HttpStatus.OK);
        } else {
            throw new WrongAuthorityException("???? ???? ???????????? ???????????????????? ?????????????????????? ?????????????? ????????????????????");
        }
    }

    @GetMapping("/{id}/comments")
    public ResponseEntity<?> getComments(@PathVariable(name="id") long id)
            throws WrongIdException {
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        List<Comment> comments = commentService.findAllByPost(post);
        List<CommentDTO> dtos = comments.stream().map(Comment::toDTO).collect(Collectors.toList());
        return new ResponseEntity<>(dtos, HttpStatus.FOUND);
    }

    @PostMapping("/{id}/comments")
    public ResponseEntity<?> addComment(@PathVariable(name="id") long id,
                                        @RequestBody @Valid @NotBlank(message = "?????????????????????? ???? ?????????? ???????? ????????????") String text,
                                        HttpServletRequest request) throws WrongIdException {
        String token = jwtTokenProvider.resolveToken(request);
        String username = jwtTokenProvider.getUsername(token);
        User user = (User) userService.loadUserByUsername(username);
        Post post = postService.findById(id).orElseThrow(() -> new WrongIdException("?????????????????????? id"));
        Comment comment = Comment.builder().post(post).user(user).content(text)
                .time(LocalDateTime.now()).build();
        commentService.create(comment);
        return new ResponseEntity<>("?????? ?????????????????????? ????????????????", HttpStatus.CREATED);
    }
}
