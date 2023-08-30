package com.example.springbatchtutorial.job.HelloWorld;

import com.example.springbatchtutorial.job.StepListener.StepLoogerListener;
import com.example.springbatchtutorial.methodInvoke.InvokMethodTest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.*;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;

import java.util.concurrent.Callable;

/**
 * packageName    : com.example.springbatchtutorial.job
 * fileName       : HelloWorldJobConfig
 * author         : kmy
 * date           : 2023/08/29
 * description    :
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2023/08/29        kmy       최초 생성
 */
@Slf4j
@Configuration
public class HelloWorldJobConfig {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
//--spring.batch.job.names=HelloWorldJob
    @Bean
    public Job HelloWorldJob(Step callableStep2
                           , Step methodInvokingStep3
                           , Step systemCmdStep4
                           , Step systemCmdStep5){
        return jobBuilderFactory.get("HelloWorldJob")
                .incrementer(new RunIdIncrementer())
                .start(Step1())
                .next(callableStep2)
                .next(methodInvokingStep3)
                .next(systemCmdStep5) //지우고
                .next(systemCmdStep4) //생성하고
                .build();
    }

    @Bean
    public Step Step1(){
        return stepBuilderFactory.get("HelloWorldStep")
                .tasklet((stepContribution,chunkContext)->{
                    log.debug("Hello World Batch!");
                  return RepeatStatus.FINISHED;
                }).build();
    }

    @Bean
    public Step callableStep2(Tasklet callableTasklet){
        return stepBuilderFactory.get("callableStep2")
                .tasklet(callableTasklet)
                .build();
    }

    /*
        Step이 실행되는 Thread와 별개의 스레드로 실행된다
        (Callable 객체가 RepeatStatus 객체 반환전에 완료된것으로 간주하지 않음)
        (callableStep2 Step가 병렬 실행되는것은 아님)
     */
    @Bean
    public Callable<RepeatStatus> callableObject(){
        return () ->{
          log.debug("this was execute in another thread");
          return RepeatStatus.FINISHED;
        };
    }

    @Bean
    public CallableTaskletAdapter callableTasklet(){
        CallableTaskletAdapter callableTaskletAdapter = new CallableTaskletAdapter();
        callableTaskletAdapter.setCallable(callableObject());
        return callableTaskletAdapter;
    }

    @Bean
    public Step methodInvokingStep3(Tasklet methodInvokingTasklet1){
        return stepBuilderFactory.get("methodInvokingStep3")
//                .tasklet(methodInvokingTasklet)
                .tasklet(methodInvokingTasklet1)
                .listener(new StepLoogerListener())
                .build();
    }

    /*
        다른 클래스 내의 메서드를 Tasklet에서 실행
        파라미터 없는 경우
     */
    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTasklet(){
        MethodInvokingTaskletAdapter methodInvokingTaskletAdapter = new MethodInvokingTaskletAdapter();

        methodInvokingTaskletAdapter.setTargetObject(service());
        methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");

        return methodInvokingTaskletAdapter;
    }

    /*
        파라미터 있는 경우
        //jobParameters, 사용시 @StepScope를 함께 사용해준다.
        //--spring.batch.job.names=HelloWorldJob -msg=msg
     */
    @StepScope
    @Bean
    public MethodInvokingTaskletAdapter methodInvokingTasklet1(@Value("#{jobParameters['msg']}") String msg){
        MethodInvokingTaskletAdapter methodInvokingTaskletAdapter = new MethodInvokingTaskletAdapter();

        methodInvokingTaskletAdapter.setTargetObject(service());
        methodInvokingTaskletAdapter.setTargetMethod("serviceMethod");
        methodInvokingTaskletAdapter.setArguments(new String[]{msg}); // 파라미터 있을 경우,

        return methodInvokingTaskletAdapter;
    }

    @Bean
    public InvokMethodTest service(){
        return new InvokMethodTest();
    }

    @Bean
    public Step systemCmdStep4(Tasklet systemCmdTasklet){
        return stepBuilderFactory.get("systemCmdStep4")
                .tasklet(systemCmdTasklet)
                .build();
    }

    /*
        SystemCommand를 실행시키는 Tasklet
     */
    @Bean
    public SystemCommandTasklet systemCmdTasklet(){
        SystemCommandTasklet sysCmdTasklet = new SystemCommandTasklet();

//        sysCmdTasklet.setWorkingDirectory("~/Desktop");
        sysCmdTasklet.setWorkingDirectory("/Users/kmy/Desktop"); //경로
        sysCmdTasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
        sysCmdTasklet.setTerminationCheckInterval(5000);

        sysCmdTasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        sysCmdTasklet.setEnvironmentParams(new String[]{
//                "JAVA_HOME=/java",
//                "BATCH_HOME=/User/kmy"
//        });
        sysCmdTasklet.setCommand("touch test.txt"); // 명령어
        sysCmdTasklet.setTimeout(5000);
        sysCmdTasklet.setInterruptOnCancel(true);

        return sysCmdTasklet;
    }

    @Bean
    public SimpleSystemProcessExitCodeMapper touchCodeMapper(){
        return new SimpleSystemProcessExitCodeMapper();
    }

    @Bean
    public Step systemCmdStep5(Tasklet systemCmdTasklet1){
        return stepBuilderFactory.get("systemCmdStep5")
                .tasklet(systemCmdTasklet1)
                .build();
    }

    @Bean
    public SystemCommandTasklet systemCmdTasklet1(){
        SystemCommandTasklet sysCmdTasklet = new SystemCommandTasklet();

//        sysCmdTasklet.setWorkingDirectory("~/Desktop");
        sysCmdTasklet.setWorkingDirectory("/Users/kmy/Desktop"); //경로
        sysCmdTasklet.setSystemProcessExitCodeMapper(touchCodeMapper());
        sysCmdTasklet.setTerminationCheckInterval(5000);

        sysCmdTasklet.setTaskExecutor(new SimpleAsyncTaskExecutor());
//        sysCmdTasklet.setEnvironmentParams(new String[]{
//                "JAVA_HOME=/java",
//                "BATCH_HOME=/User/kmy"
//        });

        sysCmdTasklet.setCommand("rm -rf test.txt"); // 명령어
        sysCmdTasklet.setTimeout(5000);
        sysCmdTasklet.setInterruptOnCancel(true);

        return sysCmdTasklet;
    }

}
