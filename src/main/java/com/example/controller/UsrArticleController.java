package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
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
	public Article showDetail(int id) {

		Article article = articleService.getArticle(id);
		return article;
	}

	@RequestMapping("usr/article/list")
	@ResponseBody
	public List<Article> showList() {
		return articleService.getArticles();
	}

	@RequestMapping("usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(String title, String body) {

		if (title == null) {
			return new ResultData("F-1", "title을 입력해주세요.");
		}
		if (body == null) {
			return new ResultData("F-1", "body를 입력해주세요.");
		}

		return articleService.add(title, body);
	}

	@RequestMapping("usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(Integer id) {

		if (id == null) {
			return new ResultData("F-1", "id를 입력해주세요.");
		}

		Article article = articleService.getArticle(id);

		if (article == null) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}

		return articleService.deleteArticle(id);
	}

	@RequestMapping("usr/article/doModify")
	@ResponseBody
	public ResultData doModify(Integer id, String title, String body) {

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

		return articleService.modify(id, title, body);
	}

}
