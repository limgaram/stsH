package com.example.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.dto.Member;
import com.example.dto.ResultData;
import com.example.service.MemberService;

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
}
