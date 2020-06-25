package com.filmi3k.movies;

import com.filmi3k.movies.services.base.UserService;
import com.filmi3k.movies.utils.FileParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ContextLoaderListener;

@Component
public class ApplicationContextListener implements ApplicationListener<ContextRefreshedEvent> {
    private final UserService userService;

    @Autowired
    public ApplicationContextListener(UserService userService) {
        this.userService = userService;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent contextRefreshedEvent) {
        FileParser fileParser = FileParser.initialize();
        userService.banUsersByIP(fileParser);
    }

}
