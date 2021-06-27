package com.wangyang.bioinfo.init;

import com.wangyang.bioinfo.pojo.Option;
import com.wangyang.bioinfo.service.IOptionService;
import com.wangyang.bioinfo.util.StringCacheStore;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Configuration;

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
    @Override
    public void onApplicationEvent(ApplicationStartedEvent applicationStartedEvent) {
        System.out.println("################init########################");
        List<Option> options = optionService.listAll();
        options.forEach(option -> {
            StringCacheStore.setValue(option.getKey_(),option.getValue_());
        });
        StringCacheStore.setValue("workDir",workDir);

    }
}
