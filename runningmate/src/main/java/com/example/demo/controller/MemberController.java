package com.example.demo.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.example.demo.auth.PrincipalDetails;
import com.example.demo.DTO.MemberDTO;
import com.example.demo.DTO.PasswordDTO;
import com.example.demo.entity.Member;
import com.example.demo.service.MemberService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;

@RequestMapping("/")
@Controller
@RequiredArgsConstructor
public class MemberController {
	@Autowired
	private MemberService memberService;
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	//로그인 getmapping
	@GetMapping("/login2")
	public String login() {
		return "/login2";
	}
	
	// !!!! OAuth로 로그인 시 이 방식대로 하면 CastException 발생함
    @GetMapping("/form/loginInfo")
    @ResponseBody
    public String formLoginInfo(Authentication authentication, @AuthenticationPrincipal PrincipalDetails principalDetails){
        PrincipalDetails principal = (PrincipalDetails) authentication.getPrincipal();
        Member member = principal.getMember();
        System.out.println(member);
        //User(id=2, username=11, password=$2a$10$m/1Alpm180jjsBpYReeml.AzvGlx/Djg4Z9/JDZYz8TJF1qUKd1fW, email=11@11, role=ROLE_USER, createTime=2022-01-30 19:07:43.213, provider=null, providerId=null)

        Member member2 = principal.getMember();
        System.out.println(member);
        //User(id=2, username=11, password=$2a$10$m/1Alpm180jjsBpYReeml.AzvGlx/Djg4Z9/JDZYz8TJF1qUKd1fW, email=11@11, role=ROLE_USER, createTime=2022-01-30 19:07:43.213, provider=null, providerId=null)
        //user == user1

        return member.toString();
    }
	
	 @GetMapping("/login2/error")
	    public String loginError(Model model){
	        model.addAttribute("loginErrorMsg","아이디 또는 비밀번호를 확인해주세요.");
	        return  "/login2";
	    }
	
	 //회원가입
	@GetMapping("/join")
	public String join(@ModelAttribute MemberDTO memberDto){
	        return  "join";
	    }
	
	
	@PostMapping("/join")
	 public String joinform(@Validated MemberDTO memberDto,
             BindingResult bindingResult, Model model){
		if(bindingResult.hasErrors()){
			return "join";
		}
		try{
			Member newMember = Member.CreateMember(memberDto, bCryptPasswordEncoder);
			memberService.saveMember(newMember);
		}catch (Exception e){
			model.addAttribute("errorMessage",e.getMessage());
			return "join";
		}

		return  "redirect:login2"; // 회원 가입 후 메인 화면으로 이동
	}
	
	@GetMapping("/delete_account")
	public String deleteAccount(Model model) {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		
		MemberDTO memberDTO = memberService.getUsername(currentUsername);
		
		model.addAttribute("username", memberDTO);
		
		return "delete_account";
	}
	
	
	@GetMapping("/delete_account/member/{no}")
	public String deleteMember(@PathVariable("no") Long idx) {
		memberService.deleteMember(idx);
		return "redirect:/logout";
	}
	
	@GetMapping("/member_set")
	public String editMember(Model model) {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    String currentUsername = authentication.getName();
	    MemberDTO memberDTO = memberService.findInfo(currentUsername);
	
	    model.addAttribute("member", memberDTO);
	    
	    return "member_set";
	}


	@PostMapping("/memberedit")
	public String updateMember(@ModelAttribute MemberDTO updatedMemberDTO) {
	    // MemberService를 이용하여 회원 정보를 업데이트합니다.
	    memberService.updateMemberInfo(updatedMemberDTO);
	    return "redirect:/myself";
	} 
	
	@GetMapping("/pw_set")
	public String showPasswordResetForm(Model model) {
	    model.addAttribute("passwordDTO", new PasswordDTO());
	    return "pw_set";
	}
	
	@PostMapping("/pw_set")
	public String updatePassword(@ModelAttribute @Valid PasswordDTO passwordDTO, BindingResult result) {
	    memberService.updatePassword(passwordDTO);

	    return "redirect:/index"; // 비밀번호 변경 후 홈페이지로 이동합니다.
	}
	
	@GetMapping("/myself")
	public String myself(Model model) {
		    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		    String currentUsername = authentication.getName();
		    MemberDTO memberDTO = memberService.findInfo(currentUsername);
		    model.addAttribute("member", memberDTO);
		    return "myself";
	}
	
	//중복처리 부분
		@GetMapping("/check-username")
	    public ResponseEntity<Map<String, Boolean>> checkUsername(@RequestParam String username) {
	        // 서비스에서 사용자 이름 확인 로직 수행
	        boolean isUsernameAvailable = !memberService.isUsernameTaken(username);

	        // 결과를 JSON 응답으로 반환
	        Map<String, Boolean> response = Collections.singletonMap("available", isUsernameAvailable);
	        return ResponseEntity.ok(response);
	    }
		
		@GetMapping("/check-userId")
	    public ResponseEntity<Map<String, Boolean>> checkUserId(@RequestParam String userId) {
	        // 서비스에서 사용자 이름 확인 로직 수행
	        boolean isUserIdAvailable = !memberService.isUserIdTaken(userId);

	        // 결과를 JSON 응답으로 반환
	        Map<String, Boolean> response = Collections.singletonMap("available", isUserIdAvailable);
	        return ResponseEntity.ok(response);
	    }	
}
