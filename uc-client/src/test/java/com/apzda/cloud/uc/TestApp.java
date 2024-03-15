package com.apzda.cloud.uc;

import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * @author fengz (windywany@gmail.com)
 * @version 1.0.0
 * @since 1.0.0
 **/

@SpringBootApplication
@ComponentScan(
    excludeFilters = {
        @ComponentScan.Filter(classes = {SpringBootApplication.class, AutoConfiguration.class})
    }
)
public class TestApp {
}
