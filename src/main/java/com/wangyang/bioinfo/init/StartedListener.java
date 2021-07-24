package com.wangyang.bioinfo.init;

import com.wangyang.bioinfo.pojo.Option;
import com.wangyang.bioinfo.pojo.Task;
import com.wangyang.bioinfo.pojo.file.CancerStudy;
import com.wangyang.bioinfo.service.ICancerStudyService;
import com.wangyang.bioinfo.service.ICodeService;
import com.wangyang.bioinfo.service.IOptionService;
import com.wangyang.bioinfo.service.ITaskService;
import com.wangyang.bioinfo.util.StringCacheStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

/**
 * @author wangyang
 * @date 2021/6/14
 */
@Slf4j
@Configuration
public class StartedListener implements ApplicationListener<ApplicationStartedEvent> {
    @Value("${bioinfo.workDir}")
    private String workDir;
    @Autowired
    IOptionService optionService;
    @Autowired
    ICodeService codeService;
    @Autowired
    ITaskService taskService;
    @Autowired
    ThreadPoolTaskExecutor executor;
    @Autowired
    ICancerStudyService cancerStudyService;

    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("################init########################");
        List<Option> options = optionService.listAll();
        options.forEach(option -> {
            StringCacheStore.setValue(option.getKey_(),option.getValue_());
        });
        StringCacheStore.setValue("workDir",workDir);
        addQueue();
    }


    //添加数据库中的任务到队列
    public void addQueue(){
        List<Task> tasks = taskService.addTas2Queue();
        int size = executor.getThreadPoolExecutor().getQueue().size();
        int activeCount = executor.getActiveCount();
        int corePoolSize = executor.getCorePoolSize();
        int maxPoolSize = executor.getMaxPoolSize();
        int keepAliveSeconds = executor.getKeepAliveSeconds();
        log.info(">>>>> Thread Pool size: {}",size);
        log.info(">>>>> active count: {}",activeCount);
        log.info(">>>>> core pool size: {}",corePoolSize);
        log.info(">>>>> max  pool size: {}",maxPoolSize);
        log.info(">>>>> keep alive seconds: {}",keepAliveSeconds);
    }
}
