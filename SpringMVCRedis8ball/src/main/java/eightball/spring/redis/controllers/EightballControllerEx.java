package eightball.spring.redis.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


import eightball.spring.redis.service.RedisServiceEx;

@Controller
public class EightballControllerEx {
	
private RedisServiceEx service;
	
	@Autowired
	public void setService(RedisServiceEx service) {
		this.service = service;
	}
	
	@RequestMapping("/")
	public String index() {
		return "Eightball";
	}
	
	@RequestMapping("/list")
	   public String list(Model model) {

		   return "list";
	   }
	
	@PostMapping("/questionAction")
	public String setQuestion(@RequestParam("question") String question, Model model) {
		
		String questionString = question;
		String answer;
		if (service.getKey(questionString) != null) {
			answer = service.getKey(questionString);
		}
		else {
			if (service.scard("answer") >= 1) {
				answer = service.spop("answer");
			}
			else {
				byte[] answer1 = "It is certain".getBytes();
				byte[] answer2 = "Very good question.".getBytes();
				byte[] answer3 = "Without a doubt".getBytes();
				byte[] answer4 = "Yes definitely".getBytes();
				byte[] answer5 = "You may rely on it, or not who knows.".getBytes();
				byte[] answer6 = "Be assured, no one knows.".getBytes();
				byte[] answer7 = "The chances are looking good.".getBytes();
				byte[] answer8 = "Outlook good".getBytes();
				byte[] answer9 = "Yes".getBytes();
				byte[] answer10 = "Follow your gut.".getBytes();
				byte[] answer11 = "Yes, that's the way the cookie crumbles.".getBytes();
				byte[] answer12 = "Ask again later".getBytes();
				byte[] answer13 = "Better not tell you now".getBytes();
				byte[] answer14 = "This can't be predicted at the moment.".getBytes();
				byte[] answer15 = "That question doesn't make sense.".getBytes();
				byte[] answer16 = "Not in this case".getBytes();
				byte[] answer17 = "Don't count on it".getBytes();
				byte[] answer18 = "My sources say no, my sources are not very reliable though.".getBytes();
				byte[] answer19 = "Ik heb geen glazen bol eh.".getBytes();
				byte[] answer20 = "Doubt".getBytes();
				service.sadd("answers",answer1,answer2,answer3,answer4,answer5,answer6,answer7,answer8,answer9,answer10,answer11,answer12,answer13,answer14,answer15,answer16,answer17,answer18,answer19,answer20);
				answer = service.spop("answers");
			}
			service.setKey(question, answer);
		}
		
		model.addAttribute("answer",answer);
		return "Validation";
	}

}
