package com.news;

import com.news.model.ApiInfo;
import com.news.service.ApiInfoService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class NewsApplicationTests {

	@Test
	public void contextLoads() {
		addInfo();
	}

	private void addInfo(){
		ApiInfo apiInfo=new ApiInfo();
		apiInfo.setPackageType("Java");
		apiInfo.setPackageName("java1测试.jar");
		apiInfo.setPackageRelativeAddress("/api/package/3_测试.jar");
		apiInfo.setPackageVersion("1.0");
		apiInfo.setDocName("");
		apiInfo.setDocRelativeAddress("");
		apiInfo.setDependencyPackage(true);

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
