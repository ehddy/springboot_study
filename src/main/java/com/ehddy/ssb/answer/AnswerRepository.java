package com.ehddy.ssb.answer;

import com.ehddy.ssb.question.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AnswerRepository extends JpaRepository<Answer, Integer>{
    Page<Answer> findByQuestion(Question question, Pageable pageable);


}