package cookcloud.config;

import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import cookcloud.entity.Code;
import cookcloud.entity.CodeId;
import cookcloud.repository.CodeRepository;
import jakarta.servlet.ServletContext;
import jakarta.servlet.ServletException;

@Configuration
public class WebAppInitializer {
	
	@Autowired
	private CodeRepository codeRepository;

    @Bean
    public ServletContextInitializer servletContextInitializer() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                Map<CodeId, Code> codeMap = codeRepository.findAll().stream().collect(Collectors.toMap(
        			code ->  new CodeId(code.getParentCode(), code.getChildCode()), 
        			code -> code
        		));
                servletContext.setAttribute("codeMap", codeMap);
            }
        };
    }
    
}