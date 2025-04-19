package com.example.backend;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

//@SpringBootTest
class BackendApplicationTests {

//    @Test
//    void contextLoads() {
//    }

    Calc calc = new Calc();

    @Test
    void itShouldAddTwoNumbers(){
        // given
        int numberOne = 20;
        int numberTwo = 30;

        // when
        int result = calc.add(numberTwo, numberOne);

        // then
        int aa = 50;
        assertThat(result).isEqualTo(aa);
    }

    class Calc{
        int add(int a, int b){
            return a+b;
        }
    }
}
