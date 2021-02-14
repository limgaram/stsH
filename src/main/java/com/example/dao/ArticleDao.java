package com.example.dao;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import com.example.dto.Article;
import com.example.util.Util;

@Component
public class ArticleDao {
	private int articlesLastId;
	private List<Article> articles;

	public ArticleDao() {
		articlesLastId = 0;
		articles = new ArrayList<>();

		// +앞에 => 아이디에 1이 더해져서 들어감
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목1", "내용1"));
		articles.add(new Article(++articlesLastId, "2020-12-12 12:12:12", "2020-12-12 12:12:12", "제목2", "내용2"));
	}

	// 찾는 게시글 하나만 가져오기
	public Article getArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				return article;
			}
		}
		return null;
	}

	// 전체 게시글 목록 가져오기(검색했으면 검색 게시글 목록 가져오기)
	public List<Article> getArticles(String searchkeywordType, String searchKeyword) {

		// 검색을 안했으면 전체 게시글 가져오기
		if (searchKeyword == null) {
			return articles;
		}

		// 검색된 게시글들 목록
		List<Article> filtered = new ArrayList<>();

		for (Article article : articles) {
			boolean contains = false;

			if (searchkeywordType.equals("title")) {
				contains = article.getTitle().contains(searchKeyword);
			} else if (searchkeywordType.equals("body")) {
				contains = article.getBody().contains(searchKeyword);
			} else {
				contains = article.getTitle().contains(searchKeyword);

				if (contains == false) {
					contains = article.getBody().contains(searchKeyword);
				}
			}

			if (contains) {
				filtered.add(article);
			}
		}
		return filtered;

	}

	public int addArticle(String title, String body) {
		int id = ++articlesLastId;
		String regDate = Util.getNowDateStr();
		String updateDate = regDate;

		articles.add(new Article(id, regDate, updateDate, title, body));

		return id;
	}

	public boolean deleteArticle(int id) {
		for (Article article : articles) {
			if (article.getId() == id) {
				articles.remove(article);
				return true;
			}
		}
		return false;
	}

	public void modifyArticle(int id, String title, String body) {
		Article article = getArticle(id);

		article.setTitle(title);
		article.setBody(body);
		article.setUpdateDate(Util.getNowDateStr());
	}

}
