package application;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@Api(value = "Super hero company controller", description = "General operations not pertaining to heroes or missions")
public class SuperIncController {

    @ApiOperation(value = "View the homepage")
    @GetMapping(value = "/", produces = "text/html")
    public String homepage() {
        return "home";
    }

}
