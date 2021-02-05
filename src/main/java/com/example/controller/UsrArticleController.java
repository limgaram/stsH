package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.Article;
import com.example.dto.ResultData;
import com.example.util.Util;

public class UsrArticleController {

	private List<Article> articles;
	private int articleLastId;

	public UsrArticleController() {
		articles = new ArrayList<>();
		articleLastId = 0;

		// +앞에 => 아이디에 1이 더해져서 들어감
		articles.add(new Article(++articleLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목1", "내용1"));
		articles.add(new Article(++articleLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목2", "내용2"));
	}

	@RequestMapping("usr/article/detail")
	@ResponseBody
	public Article showDetail(int id) {
		return articles.get(id - 1);
	}

	@RequestMapping("usr/article/list")
	@ResponseBody
	public List<Article> showList() {
		return articles;
	}

	@RequestMapping("usr/article/doAdd")
	@ResponseBody
	public ResultData doAdd(String title, String body) {

		String regDate = Util.getNowDateStr();
		String updateDate = regDate;
		articles.add(new Article(++articleLastId, regDate, updateDate, title, body));

		// articleLastId(+1된 id)를 추가된 게시물 번호로 씀

		return new ResultData("S-1", "성공하였습니다.", articleLastId);
	}

	@RequestMapping("usr/article/doDelete")
	@ResponseBody
	public ResultData doDelete(int id) {
		boolean deleteArticleRs = deleteArticle(id);

		Map<String, Object> rs = new HashMap<>();

		if (deleteArticleRs == false) {
			return new ResultData("F-1", "해당 게시물은 존재하지 않습니다.");
		}
		// 받아온 id가 삭제되는 id

		return new ResultData("S-1", "성공하였습니다.", id);
	}

	private boolean deleteArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				articles.remove(article);
				return true;
			}
		}
		return false;
	}

	@RequestMapping("usr/article/doModify")
	@ResponseBody
	public ResultData doModify(int id, String title, String body) {
		Article selArticle = null;

		for (Article article : articles) {
			if (article.getId() == id) {
				selArticle = article;
				break;
			}
		}

		Map<String, Object> rs = new HashMap<>();

		if (selArticle == null) {
			return new ResultData("F-1", String.format("%d번 게시물은 존재하지 않습니다.", id));
		}

		selArticle.setUpdateDate(Util.getNowDateStr());
		selArticle.setTitle(title);
		selArticle.setBody(body);

		return new ResultData("S-1", String.format("%d번 게시물이 수정되었습니다.", id), id);
	}

}
