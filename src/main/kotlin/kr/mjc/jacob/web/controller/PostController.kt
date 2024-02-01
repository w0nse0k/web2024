package kr.mjc.jacob.web.controller

import jakarta.servlet.http.HttpServletRequest
import kr.mjc.jacob.web.dao.Limit
import kr.mjc.jacob.web.dao.Post
import kr.mjc.jacob.web.dao.PostDao
import kr.mjc.jacob.web.dao.User
import kr.mjc.jacob.web.fullUrl
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.SessionAttribute
import org.springframework.web.server.ResponseStatusException

/**
 * Servlet API를 사용하지 않는 컨트롤러
 */
@Controller
class PostController(val postDao: PostDao) {

  private val log = LoggerFactory.getLogger(this::class.java)

  /**
   * 글목록
   */
  @GetMapping("/post/posts")
  fun posts(req: HttpServletRequest, limit: Limit) {
    // 현재 목록을 세션에 저장
    req.session.setAttribute("CURRENT_PAGE", req.fullUrl)
    log.debug("currentPage = {}", req.fullUrl)
    val totalCount = postDao.countPosts()     // 전체 포스트 갯수
    val maxPage = (totalCount - 1) / limit.count + 1   // 페이지 끝

    req.setAttribute("posts", postDao.listPosts(limit))
    req.setAttribute("totalCount", totalCount)
    req.setAttribute("maxPage", maxPage)
  }

  /**
   * 글쓰기
   */
  @PostMapping("/post/postAdd")
  fun postAdd(post: Post, @AuthenticationPrincipal user: User): String {
    postDao.addPost(post.setUser(user))
    return "redirect:/post/posts"
  }

  /**
   * 글보기
   */
  @GetMapping("/post/post")
  fun post(postId: Int, @AuthenticationPrincipal user: User?, model: Model) {
    val post = postDao.getPost(postId)
    if (post?.userId == user?.userId) model.addAttribute("owner", true)
    model.addAttribute("post", postDao.getPost(postId))
  }

  /**
   * 글수정 화면
   */
  @GetMapping("/post/postUpdate")
  fun postUpdateForm(postId: Int, @AuthenticationPrincipal user: User,
      model: Model) {
    val post = getPost(postId, user.userId)
    model.addAttribute("post", post)
  }

  /**
   * 글수정
   */
  @PostMapping("/post/postUpdate")
  fun postUpdate(post: Post, @AuthenticationPrincipal user: User): String {
    getPost(post.postId, user.userId)
    post.setUser(user)
    postDao.updatePost(post)
    return "redirect:/post/post?postId=" + post.postId
  }

  /**
   * 글삭제
   */
  @GetMapping("/post/deletePost")
  fun deletePost(postId: Int, @AuthenticationPrincipal user: User,
      @SessionAttribute("CURRENT_PAGE") currentPage: String): String {
    getPost(postId, user.userId)
    postDao.deletePost(postId, user.userId)
    return "redirect:$currentPage"
  }

  /**
   * 게시글의 권한 체크
   *
   * @throws ResponseStatusException 권한이 없을 경우
   */
  private fun getPost(postId: Int, userId: Int): Post {
    val post = postDao.getPost(postId)
    if (userId != post?.userId) throw ResponseStatusException(
        HttpStatus.UNAUTHORIZED)
    return post
  }
}
