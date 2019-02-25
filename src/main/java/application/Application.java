package application;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import springfox.documentation.spring.data.rest.configuration.SpringDataRestConfiguration;

@SpringBootApplication
//@Import(SpringDataRestConfiguration.class)
/*
    Please note that the annotation above is required to autogenerate the Swagger documentation for Spring Data REST
    APIs, but a known issue causes it to attempt to call
    org.springframework.data.repository.support.Repositories.getRepositoryInformation, but this method does not exist.
    See https://github.com/springfox/springfox/issues/2581 for details.
 */
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
