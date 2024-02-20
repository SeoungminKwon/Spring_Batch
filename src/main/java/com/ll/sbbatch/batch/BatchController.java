package com.ll.sbbatch.batch;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Controller
@RequestMapping("/batch")
@RequiredArgsConstructor
public class BatchController {
    private final BatchService batchService;

    @GetMapping("/hello")
    @ResponseBody
    public String runHelloJob(){
        batchService.runHelloJobJob();
        return "runHelloJob OK";
    }

    @GetMapping("/makeProductLog")
    @ResponseBody
    public String runMakeProductLogJob(){
        LocalDateTime startDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);
        LocalDateTime endDate = LocalDateTime.now().withHour(0).withMinute(0).withSecond(0).withNano(0);

        batchService.runMakeProductLogJob(startDate, endDate);
        return "makeProductLog OK";
    }
}
