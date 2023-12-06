package com.example.demo.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.example.demo.DTO.FriendList1DTO;
import com.example.demo.DTO.FriendListDTO;
import com.example.demo.DTO.FriendRequestDTO;
import com.example.demo.DTO.RunDto;
import com.example.demo.repository.FriendListRepository;
import com.example.demo.service.FriendListService;
import com.example.demo.service.FriendRequestService;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/")
@Slf4j
public class FriendController {
	
	private FriendListService friendListService;
	private FriendRequestService friendRequestService;
	
	public FriendController(FriendListService friendListService, FriendRequestService friendRequestService) {
		this.friendListService = friendListService;
		this.friendRequestService = friendRequestService;
	}
	
	@GetMapping("/friendlist")
	public String friendList(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		List<FriendListDTO> friendDto = friendListService.getFriendlist(currentUsername,pageNum);
		Integer[] pageList = friendListService.getPageList(currentUsername, pageNum);
		
		model.addAttribute("friendList", friendDto);
		model.addAttribute("pageList", pageList);
		
		return "friendList";
	}
	
	@GetMapping("/friendDelete/{no}")
	public String friendDelete(@PathVariable("no") Long idx) { 
		 friendListService.deleteFriend(idx);
		  
	 return "redirect:/friendlist"; 
	 }
	
	@GetMapping("/request/{no}")
	public String request(@PathVariable("no") Long idx) {
		return "request";
	}
	
	@PostMapping("/request/{no}/a/{id}/b/{myid}")
	public String request(FriendRequestDTO frDTO, @PathVariable("no") Long idx, @PathVariable("id") String id, @PathVariable("myid") String myid) {
		friendRequestService.request(frDTO);
		return "redirect:/freeContent/" + idx + "/a/" + id +"/b/" + myid;
	}
	
	@GetMapping("/materequest/{no}")
	public String materequest(@PathVariable("no") Long idx) {
		return "request";
	}
	
	@PostMapping("/materequest/{no}/a/{id}/b/{myid}")
	public String materequest(FriendRequestDTO frDTO, @PathVariable("no") Long idx, @PathVariable("id") String id, @PathVariable("myid") String myid) {
		friendRequestService.request(frDTO);
		return "redirect:/mateContent/" + idx + "/a/" + id +"/b/" + myid;
	}
	
	@GetMapping("/friendRequest")
	public String friendRequest(Model model, @RequestParam(value="page", defaultValue = "1") Integer pageNum) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String currentUsername = authentication.getName();
		
		List<FriendRequestDTO> friendRequestDTO = friendRequestService.getRequestList(currentUsername, pageNum);
		Integer[] pageList = friendRequestService.getPageList(pageNum);
		
		model.addAttribute("request", friendRequestDTO);
		model.addAttribute("pageList", pageList);
		
		return "friendRequest";
	}
	
	@GetMapping("/requestDelete/{no}")
	public String requestDelete(@PathVariable("no") Long idx) {
		friendRequestService.deleteRequest(idx);
		
		return "redirect:/friendRequest";
	}
	
	@GetMapping("/agree")
	public String agree() {
		return "agree";
	}
	
	@PostMapping("/agree")
	public String agree(FriendListDTO flDTO, FriendList1DTO fl1DTO) {
		friendListService.addFriend(flDTO);
		friendListService.addFriend1(fl1DTO);
		friendRequestService.deleteRequest(flDTO.getRequestidx());
	
		return "redirect:/friendRequest";
	}

}
