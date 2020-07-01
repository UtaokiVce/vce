package com.weilai9.common.utils.wechat;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.generator.AutoGenerator;
import com.baomidou.mybatisplus.generator.InjectionConfig;
import com.baomidou.mybatisplus.generator.config.*;
import com.baomidou.mybatisplus.generator.config.po.TableInfo;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;

import java.util.ArrayList;
import java.util.List;

/**
 * 代码生成器
 *
 * @author QYB
 * 时间:2018年7月3日上午9:09:41
 */
public class CustomGenerator {
    public static void main(String[] args) throws InterruptedException {
        AutoGenerator mpg = new AutoGenerator();

        // 全局配置
        GlobalConfig gc = new GlobalConfig();
        String projectPath = System.getProperty("user.dir");
        gc.setOutputDir(projectPath + "/src/main/java");
        gc.setFileOverride(true);
        gc.setActiveRecord(true);
        gc.setEnableCache(false);// XML 二级缓存
        gc.setBaseResultMap(true);// XML ResultMap
        gc.setBaseColumnList(true);// XML columList
        gc.setAuthor("xjh");

        // 自定义文件命名，注意 %s 会自动填充表实体属性！
        gc.setMapperName("%sMapper");
        gc.setXmlName("%sMapper");
        gc.setServiceName("%sService");
        gc.setServiceImplName("%sServiceImpl");
        gc.setControllerName("%sController");
        mpg.setGlobalConfig(gc);

        // 数据源配置
        DataSourceConfig dsc = new DataSourceConfig();
        dsc.setDbType(DbType.MYSQL);
        dsc.setDriverName("com.mysql.cj.jdbc.Driver");
        dsc.setUrl("jdbc:mysql://132.232.54.221:3306/xqsh?useUnicode=true&characterEncoding=UTF-8&generateSimpleParameterMetadata=true&serverTimezone=Asia/Shanghai");
        dsc.setUsername("hejinlo");
        dsc.setPassword("so123");
        mpg.setDataSource(dsc);

        // 策略配置
        StrategyConfig strategy = new StrategyConfig();
        // strategy.setCapitalMode(true);// 全局大写命名 ORACLE 注意
        //strategy.setTablePrefix(new String[]{""});// 此处可以修改为您的表前缀
        strategy.setNaming(NamingStrategy.underline_to_camel);// 表名生成策略
        strategy.setInclude(new String[]{"customerrelation"}); // 需要生成的表,默认全部
        // strategy.setExclude(new String[]{"test"}); // 排除生成的表,不配置生成表的话，可以配置排除表
        mpg.setStrategy(strategy);

        // 自定义Controller父类
//		strategy.setSuperControllerClass("com.baomidou.ant.common.BaseController");
        // 自定义实体，公共字段
//        strategy.setSuperEntityColumns("id");
        // 自定义 mapper 父类
//        strategy.setSuperMapperClass("com.lz.lijiamapp.mapper.SuperMapper");
        // 自定义实体父类
//		strategy.setSuperEntityClass("com.baomidou.ant.common.BaseEntity");
        // 实体开启lombok
        strategy.setEntityLombokModel(true);
        // 生成controller为rest风格
        strategy.setRestControllerStyle(true);

        // 包配置
        PackageConfig pc = new PackageConfig();
        pc.setParent("com");//设置包名
        pc.setModuleName("miniapp");//根据项目不同决定包名
        pc.setController("controller");//设置controller包名，默认web
        //pc.setEntity("entity");//设置entity包名,默认entity
        mpg.setPackageInfo(pc);
        // 自定义配置
        InjectionConfig cfg = new InjectionConfig() {
            @Override
            public void initMap() {
                // to do nothing
            }
        };
        // 如果模板引擎是 velocity
        String templatePath = "/templates/mapper.xml.vm";
        // 自定义输出配置
        List<FileOutConfig> focList = new ArrayList<>();
        // 自定义配置会被优先输出
        focList.add(new FileOutConfig(templatePath) {
            @Override
            public String outputFile(TableInfo tableInfo) {
                // 自定义输出文件名 ， 如果你 Entity 设置了前后缀、此处注意 xml 的名称会跟着发生变化！！
                return projectPath + "/src/main/resources/mapper/"
                        + "/" + tableInfo.getEntityName() + "Mapper" + StringPool.DOT_XML;
            }
        });
        cfg.setFileOutConfigList(focList);
        mpg.setCfg(cfg);
        // 配置模板
        TemplateConfig templateConfig = new TemplateConfig();
        templateConfig.setXml(null);
        mpg.setTemplate(templateConfig);

        // 执行生成
        mpg.execute();
    }

}