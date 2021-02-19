package com.example.controller;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.Article;
import com.example.dto.ResultData;
import com.example.service.ArticleService;
import com.example.util.Util;

public class UsrArticleController {

	@Autowired
	private ArticleService articleService;

	private List<Article> articles;
	private int articleLastId;

	@RequestMapping("usr/article/detail")
	@ResponseBody
	public ResultData showDetail(Integer id) {

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Article article = articleService.getForPrintArticle(id);

		if (article == null) {
			return new ResultData("F-2", "존재하지 않는 게시물 번호입니다.");
		}
		return new ResultData("S-1", "성공", "article", article);
	}

	@RequestMapping("usr/article/list")
	@ResponseBody
	public ResultData showList(String searchKeywordType, String searchKeyword) {
		if (searchKeywordType != null) {
			searchKeywordType = searchKeywordType.trim();
		}

		if (searchKeywordType == null || searchKeywordType.length() == 0) {
			searchKeywordType = "titleAndBody";
		}

		if (searchKeyword != null && searchKeyword.length() == 0) {
			// searchKeyword가 비어있지 않은데 길이가 0이면 null로 판단
			searchKeyword = null;
		}
		if (searchKeyword != null) {
			// searchKeyword가 들어왔을 때, 공백값은 없애주고 searchKeyword값만
			searchKeyword = searchKeyword.trim();
		}

		if (searchKeyword == null) {
			searchKeywordType = null;
		}

		List<Article> articles = articleService.getForPrintArticles(searchKeywordType, searchKeyword);

		return new ResultData("S-1", "성공", "articles", articles);
	}

	@RequestMapping("usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(@RequestParam Map<String, Object> param, HttpSession session) {

		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);
		if (loginedMemberId == 0) {
			return new ResultData("F-2", "로그인 후 이용해주세요.");
		}

		if (param.get("title") == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if (param.get("body") == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}

		// param에 로그인된 멤버 아이디를 집어넣기
		param.put("memberId", loginedMemberId);

		return articleService.addArticle(param);
	}

	@RequestMapping("usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id, HttpSession session) {

		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if (loginedMemberId == 0) {
			return new ResultData("F-2", "로그인 후 이용해주세요.");
		}

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		// 이 사용자가 게시글은 삭제할 수 있는 권한이 있는지 articleService에 게시글과 로그인된 회원 아이디 넘겨서 물어보기
		ResultData actorCanDeleteRd = articleService.getActorCanDeleteRd(article, loginedMemberId);

		if (actorCanDeleteRd.isFail()) {
			return actorCanDeleteRd;
		}

		return articleService.deleteArticle(id);
	}

	@RequestMapping("usr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body, HttpSession session) {

		int loginedMemberId = Util.getAsInt(session.getAttribute("loginedMemberId"), 0);

		if (loginedMemberId == 0) {
			return new ResultData("F-2", "로그인 후 이용해주세요.");
		}

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}
		if (title == null) {
			return new ResultData("F-1", "title를 입력해주세요.");
		}
		if (body == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}
		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		ResultData actorCanModifyRd = articleService.getActorCanModifyRd(article, loginedMemberId);

		if (actorCanModifyRd.isFail()) {
			return actorCanModifyRd;
		}
		return articleService.modifyArticle(id, title, body);
	}

}
