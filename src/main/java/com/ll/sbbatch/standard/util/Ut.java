package com.ll.sbbatch.standard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Ut {
    public static class date{
        public static LocalDateTime parse(String pattern, String dateText){
            //DateTimeFormatter.ofPattern(pattern)을 사용하여 주어진 패턴에 맞는 DateTimeFormatter 객체를 생성
            //LocalDateTime.parse를 통해서 LocalDateTime 객체로 변환해서 반환
            return LocalDateTime.parse(dateText, DateTimeFormatter.ofPattern(pattern));
        }

        public static LocalDateTime parse(String dateText){
            //SSSSSS는 마이크로 초로 여섯자리 숫자를 표시하는 형식
            return parse("yyyy-MM-dd HH:mm:ss.SSSSSS", dateText);
        }
    }
}
