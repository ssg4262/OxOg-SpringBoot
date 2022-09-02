package com.Oxog.Beta.validator;


import com.Oxog.Beta.model.Board;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import org.thymeleaf.util.StringUtils;

public class BoardValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return Board.class.equals(clazz);
    }

    @Override
    public void validate(Object obj, Errors e) { // obj로 매개값을 받고
    Board b= (Board) obj; //Board타입으로 형변환
    if (StringUtils.isEmpty(b.getContent())){ // 비었는지 확인
        e.rejectValue("content","key","내용을 입력하세요");
    }




    }
}
