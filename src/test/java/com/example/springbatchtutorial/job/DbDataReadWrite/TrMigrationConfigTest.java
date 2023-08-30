package com.example.springbatchtutorial.job.DbDataReadWrite;

import com.example.springbatchtutorial.SpringBatchTestConfig;
import com.example.springbatchtutorial.domain.Orders;
import com.example.springbatchtutorial.repository.AccountsRepository;
import com.example.springbatchtutorial.repository.OrdersRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@SpringBatchTest
@SpringBootTest(classes = {SpringBatchTestConfig.class, TrMigrationConfig.class})
class TrMigrationConfigTest {

    @Autowired private JobLauncherTestUtils jobLauncherTestUtils;

    @Autowired private OrdersRepository ordersRepository;

    @Autowired private AccountsRepository accountsRepository;

    @AfterEach
    public void cleanUpEach(){
        ordersRepository.deleteAll();
        accountsRepository.deleteAll();
    }

    @Test()
    public void success_noData() throws Exception{
        //when -- 실행에 대한 결과
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(jobExecution.getExitStatus(), "exitCode="+BatchStatus.COMPLETED+";exitDescription=");
        //exitCode=COMPLETED;exitDescription= <--?? BatchStatus.COMPLETED
        Assertions.assertEquals(0, accountsRepository.count()); // 리턴 수 비교
    }

    @Test
    public void success_existData() throws Exception{
        //given
        Orders orders1 = new Orders(null,"kakao gift", 15000, new Date());
        Orders orders2 = new Orders(null,"naver gift", 16000, new Date());

        ordersRepository.save(orders1);
        ordersRepository.save(orders2);

        //when
        JobExecution jobExecution = jobLauncherTestUtils.launchJob();

        //then
        Assertions.assertEquals(jobExecution.getExitStatus(), ExitStatus.COMPLETED);
        Assertions.assertEquals(2, accountsRepository.count());
    }
}