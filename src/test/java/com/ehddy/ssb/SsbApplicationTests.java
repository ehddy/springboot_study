package com.ehddy.ssb;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDateTime;
import java.util.List;

import java.util.Optional;

import com.ehddy.ssb.answer.Answer;
import com.ehddy.ssb.answer.AnswerRepository;
import com.ehddy.ssb.question.Question;
import com.ehddy.ssb.question.QuestionRepository;
import com.ehddy.ssb.question.QuestionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
// db 세션 유지
import org.springframework.transaction.annotation.Transactional;

import java.util.*;


@SpringBootTest
class SsbApplicationTests {

	@Autowired
	private QuestionService questionService;

	@Transactional
	@Test
	void testJpa() {
		ArrayList list = new ArrayList();

		int totalPages = 20;
		int showPages = 5;

		int pageNum = 0;

		int Num = totalPages - (pageNum * showPages) - 1;


		System.out.println(Num);

	}
}


