package com.ehddy.ssb.question;


// 사용자 로그
import lombok.extern.slf4j.Slf4j;


import com.ehddy.ssb.answer.AnswerForm;
import com.ehddy.ssb.answer.AnswerService;
import com.ehddy.ssb.answer.Answer;

import java.util.List;

import java.security.Principal;
import java.util.Optional;

import com.ehddy.ssb.user.SiteUser;
import com.ehddy.ssb.user.UserService;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

// 폼 유효성
import jakarta.validation.Valid;
import org.apache.catalina.User;
import org.springframework.validation.BindingResult;

import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
// 상세 경로 지정

// prefix

import lombok.RequiredArgsConstructor;

//페이지
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.data.domain.Page;

// 로그인 필요
import org.springframework.security.access.prepost.PreAuthorize;
@Slf4j
@RequestMapping("/question")
@RequiredArgsConstructor
@Controller
public class QuestionController {

    private final QuestionService questionService;
    private final UserService userService;
    private final AnswerService answerService;

    @GetMapping("/list")
    public String list(Model model, @RequestParam(value="page", defaultValue="0") int page,
                       @RequestParam(value = "kw", defaultValue = "") String kw) {
        log.info("page:{}, kw:{}", page, kw);
        Page<Question> paging = this.questionService.getList(page, kw);
        model.addAttribute("paging", paging);
        model.addAttribute("kw", kw);
        return "question_list";
    }

    @GetMapping(value = "/detail/{id}")
    public String detail(Model model, @PathVariable("id")
                         Integer id,
                         @RequestParam(value="answerPage", defaultValue="0") int answerPage,
                         AnswerForm answerForm) {
        Question question = this.questionService.getQuestion(id);

        Page<Answer> answerPaging = this.answerService.getList(question, answerPage);

        model.addAttribute("answerPaging", answerPaging);
        model.addAttribute("question", question);
        return "question_detail";
    }
    @PreAuthorize("isAuthenticated()")
    @GetMapping("/create")
    public String questionCreate(QuestionForm questionForm) {
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/create")
    public String questionCreate(@Valid QuestionForm questionForm, BindingResult bindingResult, Principal principal) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.create(questionForm.getSubject(), questionForm.getSportType(), questionForm.getContent(), siteUser);
        return "redirect:/question/list";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/modify/{id}")
    public String questionModify(QuestionForm questionForm, @PathVariable("id") Integer id,
                                 Principal principal) {
        Question question = this.questionService.getQuestion(id);
        if(!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다. ");
        }
        questionForm.setSubject(question.getSubject());
        questionForm.setContent((question.getContent()));
        questionForm.setSportType(question.getSportType());
        return "question_form";
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/modify/{id}")
    public String questionModify(@Valid QuestionForm questionForm, BindingResult bindingResult,
                                 Principal principal, @PathVariable("id") Integer id) {
        if (bindingResult.hasErrors()) {
            return "question_form";
        }
        Question question = this.questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "수정 권한이 없습니다. ");
        }
        this.questionService.modify(question, questionForm.getSubject(), questionForm.getSportType(), questionForm.getContent());
        return String.format("redirect:/question/detail/%s", id);
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/delete/{id}")
    public String questionDelete(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);

        if (!question.getAuthor().getUsername().equals(principal.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "삭제권한이 없습니다. ");
        }
        this.questionService.delete(question);

        return "redirect:/";
    }

    @PreAuthorize("isAuthenticated()")
    @GetMapping("/vote/{id}")
    public String questionVote(Principal principal, @PathVariable("id") Integer id) {
        Question question = this.questionService.getQuestion(id);
        SiteUser siteUser = this.userService.getUser(principal.getName());
        this.questionService.vote(question, siteUser);

        return String.format("redirect:/question/detail/%s", id);
    }

}
