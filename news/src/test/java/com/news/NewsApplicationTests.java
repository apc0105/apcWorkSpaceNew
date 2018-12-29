package com.news;

import com.news.model.ApiInfo;
import com.news.model.SearchHintKeys;
import com.news.model.WebNode;
import com.news.service.ApiInfoService;
import com.news.service.SearchHintKeysService;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class NewsApplicationTests {


	private void addSearchHintKeys(){
		SearchHintKeys keys=new SearchHintKeys();
//		keys.setName("石油");
// 		keys.setName("石脑油");
		keys.setName("对二甲苯");

		searchHintKeysService.saveSearchHintKeys(keys);
	}

	private void addInfo(){
/*		ApiInfo apiInfo=new ApiInfo();
		apiInfo.setPackageType("Java");
		apiInfo.setPackageName("java1测试.jar");
		apiInfo.setPackageRelativeAddress("/api/package/3_测试.jar");
		apiInfo.setPackageVersion("1.0");
		apiInfo.setDocName("");
		apiInfo.setDocRelativeAddress("");
		apiInfo.setDependencyPackage(true);

		apiInfoService.saveApiInfo(apiInfo);*/

		for(int i=0;i<5;i++){
			ApiInfo apiInfo=new ApiInfo();
			apiInfo.setPackageType("Python1");
			apiInfo.setPackageName((i+1)+".py");
			apiInfo.setPackageRelativeAddress("/api/package/"+(i+1)+".py");
			apiInfo.setPackageVersion("1.0");
			apiInfo.setDocName((i+1)+".pyAPI");
			apiInfo.setDocRelativeAddress("");
			apiInfo.setDependencyPackage(false);

			apiInfoService.saveApiInfo(apiInfo);
		}
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


	private void testList(){
		List<WebNode> nodes = new ArrayList<WebNode>();

		for(int i=0;i<10;i++){
			WebNode webNode = new WebNode();
			webNode.setKey("test"+i);
			webNode.setText("test"+i);
			nodes.add(webNode);
		}
		for(int j=0;j<10;j++){
			WebNode webNode = new WebNode();
			webNode.setKey("test"+j);
			webNode.setText("test"+j);
			boolean flag=false;
			for(WebNode node:nodes){
				if(node.getKey().equals(webNode.getKey())){
					flag=true;
					break;
				}
			}
			if(!flag){
				nodes.add(webNode);
			}
		}



		for(WebNode webNode:nodes){
			System.out.println("------"+webNode.getKey()+"------"+webNode.getText()+"------");
		}
	}

	@Test
	public void contextLoads() {

		//testList();

		//addSearchHintKeys();

		addInfo();
	}
	@Autowired
	private ApiInfoService apiInfoService;

	@Autowired
	private SearchHintKeysService searchHintKeysService;
}
