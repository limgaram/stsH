package com.example.controller;

import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.Member;
import com.example.dto.ResultData;
import com.example.service.MemberService;
import com.sun.net.httpserver.Authenticator.Result;

@Controller
public class UsrMemberController {

	@Autowired
	private MemberService memberService;

	@RequestMapping("usr/member/join")
	@ResponseBody
	public ResultData doJoin(@RequestParam Map<String, Object> param) {

		if (param.get("loginId") == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		// 기존의 가입된 회원들 loginId 가져오기
		Member existingMember = memberService.getMemberByLoginId((String) param.get("loginId"));

		// 기존의 입력된 loginId로 가입된 회원이 있을 경우
		if (existingMember != null) {
			return new ResultData("F-2", String.format("%s (은)는 이미 사용 중인 아이디입니다.", param.get("loginId")));
		}

		if (param.get("loginPw") == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}
		if (param.get("name") == null) {
			return new ResultData("F-1", "name를 입력해주세요.");
		}
		if (param.get("nickname") == null) {
			return new ResultData("F-1", "nickname를 입력해주세요.");
		}
		if (param.get("cellphoneNo") == null) {
			return new ResultData("F-1", "cellphoneNo를 입력해주세요.");
		}
		if (param.get("email") == null) {
			return new ResultData("F-1", "email를 입력해주세요.");
		}

		return memberService.join(param);
	}

	@RequestMapping("/usr/member/doLogin")
	@ResponseBody
	public ResultData doLogin(String loginId, String loginPw, HttpSession session) {

		// 로그인 아이디가 비어있지 않을 때(=로그인이 되어있을 때)
		if (session.getAttribute("loginedMemberId") != null) {
			return new ResultData("F-4", "이미 로그인 되었습니다.");
		}

		if (loginId == null) {
			return new ResultData("F-1", "loginId를 입력해주세요.");
		}

		Member existingMember = memberService.getMemberByLoginId(loginId);

		// 기존 회원 아이디에서 못찾은 경우
		if (existingMember == null) {
			return new ResultData("F-2", "존재하지 않는 로그인 아이디입니다.", "loginId", loginId);
		}

		if (loginPw == null) {
			return new ResultData("F-1", "loginPw를 입력해주세요.");
		}

		if (existingMember.getLoginPw().equals(loginPw) == false) {
			return new ResultData("F-3", "비밀번호가 일치하지 않습니다.");
		}

		// session에 회원 아이디 정보 입력
		session.setAttribute("loginedMemberId", existingMember.getId());

		return new ResultData("S-1", String.format("%s님 환영합니다.", existingMember.getNickname()));
	}
}
