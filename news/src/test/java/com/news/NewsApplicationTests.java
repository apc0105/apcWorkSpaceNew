package com.news;

import com.news.model.ApiInfo;
import com.news.service.ApiInfoService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class NewsApplicationTests {

	@Test
	public void contextLoads() {
		getAllByParams();
	}

	private void addInfo(){
		ApiInfo apiInfo=new ApiInfo();
		apiInfo.setPackageType("Java");
		apiInfo.setPackageName("java1.jar");
		apiInfo.setPackageRelativeAddress("/api/package/3_2.jar");
		apiInfo.setPackageVersion("1.0");
		apiInfo.setDocName("接口3开发文档");
		apiInfo.setDocRelativeAddress("/api/doc/3_2.doc");
		apiInfo.setDependencyPackage(false);

		apiInfoService.saveApiInfo(apiInfo);
	}


	private void getTypes(){
		List<String> list=apiInfoService.findFieldValues("packageType");
		for(String str:list){
			System.out.println(str);
		}
	}

	private void getAll(){
		List<ApiInfo> list=apiInfoService.findAll();

		System.out.println("------类型------包名------");
		for(ApiInfo apiInfo:list){
			System.out.println("------"+apiInfo.getPackageType()+"------"+apiInfo.getPackageName()+"------");
		}
	}

	private void getAllByParams(){
		ApiInfo info=new ApiInfo();
		info.setPackageType("Java");

		List<ApiInfo> list=apiInfoService.findListByParam(info);

		System.out.println("------类型------包名------");
		for(ApiInfo apiInfo:list){
			System.out.println("------"+apiInfo.getPackageType()+"------"+apiInfo.getPackageName()+"------");
		}
	}

	@Autowired
	private ApiInfoService apiInfoService;
}
