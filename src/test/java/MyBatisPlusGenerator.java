import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;

import java.io.File;
import java.util.Collections;

public class MyBatisPlusGenerator {

    public static void main(String[] args) {
        FastAutoGenerator.create("jdbc:mysql://localhost:3306/meituan", "root", "root")
                .globalConfig(builder -> {
                    builder.author("wzg") // 设置作者
//                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .disableOpenDir()
                            .outputDir(System.getProperty("user.dir")+ File.separator +"src/main/java"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com.meituan.order"); // 设置父包名
//                            .entity("entity")
//                            .pathInfo(Collections.singletonMap(OutputFile.mapperXml, "D://")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude("menu") // 设置需要生成的表名
                            .addTablePrefix("t_", "c_")// 设置过滤表前缀
                    .entityBuilder().enableLombok()
                    .mapperBuilder().enableMapperAnnotation();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }
}
