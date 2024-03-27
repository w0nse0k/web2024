package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import kr.mjc.jacob.web.fullUrl
import kr.mjc.jacob.web.repository.Post
import kr.mjc.jacob.web.repository.PostRepository
import kr.mjc.jacob.web.repository.User
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException
import java.time.LocalDateTime

/**
 * Servlet API를 사용하지 않는 컨트롤러
 */
@Controller
class PostController(val postRepository: PostRepository) {

  companion object {
    private const val PAGE_SIZE = 10
  }

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * 글목록
   */
  @GetMapping("/post/post_list")
  fun postList(req: HttpServletRequest, page: Int = 0) {
    // 현재 목록을 세션에 저장
    req.session.setAttribute("CURRENT_PAGE", req.fullUrl)
    log.debug("currentPage = {}", req.fullUrl)

    val postPage: Page<Post> = postRepository.findAll(
        PageRequest.of(page, PAGE_SIZE, Sort.Direction.DESC, "id"))
    req.setAttribute("page", postPage)
  }

  /**
   * 글쓰기
   */
  @PostMapping("/post/post_create")
  fun postCreate(post: Post, @AuthenticationPrincipal user: User): String {
    postRepository.save(post.setUser(user))
    return "redirect:/post/post_list"
  }

  /**
   * 글보기
   */
  @GetMapping("/post/post_detail")
  fun postDetail(id: Int, @AuthenticationPrincipal user: User?, model: Model) {
    try {
      val post: Post? = postRepository.findById(id).orElseThrow()
      if (post?.userId == user?.id) model.addAttribute("owner", true)
      model.addAttribute("post", post)
    } catch (e: Exception) {
      throw ResponseStatusException(HttpStatus.BAD_REQUEST)
    }
  }

  /**
   * 글수정 화면
   */
  @GetMapping("/post/post_update")
  fun postUpdate(id: Int, @AuthenticationPrincipal user: User, model: Model) {
    val post = getPost(id, user.id)
    model.addAttribute("post", post)
  }

  /**
   * 글수정
   */
  @PostMapping("/post/post_update")
  fun postUpdate(postForm: Post, @AuthenticationPrincipal user: User): String {
    val post = getPost(postForm.id, user.id)
    post.title = postForm.title
    post.content = postForm.content
    post.lastModified = LocalDateTime.now()
    postRepository.save(post)
    return "redirect:/post/post_detail?id=" + post.id
  }

  /**
   * 글삭제
   */
  @PostMapping("/post/deletePost")
  fun deletePost(id: Int, @AuthenticationPrincipal user: User,
                 @SessionAttribute("CURRENT_PAGE")
                 currentPage: String): String {
    getPost(id, user.id)
    postRepository.deleteById(id)
    return "redirect:$currentPage"
  }

  /**
   * 게시글의 권한 체크
   *
   * @throws ResponseStatusException 권한이 없을 경우
   */
  private fun getPost(id: Int, userId: Int): Post {
    val post = postRepository.findById(id).orElseThrow()
    if (userId != post.userId) throw ResponseStatusException(
        HttpStatus.UNAUTHORIZED)  // 401
    return post
  }
}
