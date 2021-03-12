package net.renfei.demo.jarreadfile;

import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;

/**
 * <p>Title: TestController</p>
 * <p>Description: </p>
 *
 * @author RenFei
 */
@Controller
public class TestController {
    /**
     * 我写过一篇博文
     * - https://blog.csdn.net/qq_41337646/article/details/103495110
     * - https://www.renfei.net/posts/1003293
     * 我说获取Jar包里的文件，得用 getInputStream 获取流的方式
     * 网友们都留言，亲测获取不到
     * 我就演示给你们看，能不能用 getInputStream 获取流的方式获取Jar包里的内容
     */
    @RequestMapping("/")
    public void getImageTest(HttpServletResponse response) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = resolver.getResources("image/WX20210312-225547.png");
        Resource resource = resources[0];
        try (InputStream input = resource.getInputStream()) {
            response.setContentType("image/png");
            int len = 0;
            byte[] buffer = new byte[1024];

            while ((len = input.read(buffer)) != -1) {
                System.out.println("读取流，写入response输出流");
                response.getOutputStream().write(buffer, 0, len);
            }
        }
    }
}
