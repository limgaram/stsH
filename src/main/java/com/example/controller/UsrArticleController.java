package com.example.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.Article;
import com.example.util.Util;

public class UsrArticleController {
	
	private List<Article> articles;
	private int articleLastId;
	
	public UsrArticleController() {
		articles = new ArrayList<>();
		articleLastId = 0;
		
		//+앞에 => 아이디에 1이 더해져서 들어감
		articles.add(new Article(++articleLastId, "2020-12-12 12:12:12", "제목1", "내용1"));
		articles.add(new Article(++articleLastId, "2020-12-12 12:12:12", "제목2", "내용2"));
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
	public Map<String, Object> doAdd(String title, String body) {
		String regDate = Util.getNowDateStr();
		articles.add(new Article(++articleLastId, regDate, title, body));
		
		Map<String, Object> rs = new HashMap<>();
		rs.put("resultCode", "S-1");
		rs.put("msg", "성공하였습니다.");
		rs.put("id", articleLastId);
		
		return rs;
	}
	
	
	

}
