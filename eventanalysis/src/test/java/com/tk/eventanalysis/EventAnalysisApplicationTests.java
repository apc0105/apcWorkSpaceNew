package com.tk.eventanalysis;

import com.alibaba.fastjson.JSON;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.SQLOutput;

@RunWith(SpringRunner.class)
@SpringBootTest
@Ignore
public class EventAnalysisApplicationTests {

	@Test
	public void contextLoads() {
        String uid="324243352sdaddada";
        String newstarget="债转股";
		int index=5;
		String jsonParameters= "{\"UID\":\"" +uid+ "\",\"index\":" +index+ ",\"newstarget\":\"" +newstarget+ "\"}";

		System.out.println(jsonParameters);
	}

}
