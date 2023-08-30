package com.example.springbatchtutorial.methodInvoke;

import lombok.extern.slf4j.Slf4j;

/**
 * packageName    : com.example.springbatchtutorial.methodInvoke
 * fileName       : InvokMethodTest
 * author         : kmy
 * date           : 2023/08/30
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/30        kmy       최초 생성
 */
@Slf4j
public class InvokMethodTest {

    public void serviceMethod(){
        log.error("{}.serviceMethod() is called!!", this.getClass().getName());
    }

}
