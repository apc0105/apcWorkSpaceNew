package com.news.util;

import ai.hual.sentiment.pojo.xsd.InferenceResult;
import com.tk.ws.TKNewsServiceClient;

public class NewsUtil {
    public static void main(String[] args) {
  /*      try {
            String url = "http://xxxx.xxxx.xxxx.xxxx:9999/blablablabla";
            TKNewsServiceClient client = new TKNewsServiceClient();
            {
                String strKey = "港口设备";
                int nDirection = 0;
                InferenceResult[] results = client.getRelatedCompaniesByKey(strKey, 0, 0, 0, nDirection);//UP
                if (results != null) {

                }
            }

            {
                String strKey = "港口设备";
                int nDirection = 1;
                InferenceResult[] results = client.getRelatedCompaniesByKey(strKey, 0, 0, 0, nDirection);//UP
                if (results != null) {

                }
            }


            {
                String strKey = "港口设备";
                int nDirection = 2;
                InferenceResult[] results = client.getRelatedCompaniesByKey(strKey, 0, 0, 0, nDirection);//UP
                if (results != null) {

                }
            }

            {
                String strKey = "焊接设备";
                int nDirection = 0;
                InferenceResult[] results = client.getRelatedCompaniesByKey(strKey, 0, 0, 0, nDirection);//UP
                if (results != null) {

                }
            }

            {
                String strKey = "焊接设备";
                int nDirection = 1;
                InferenceResult[] results = client.getRelatedCompaniesByKey(strKey, 0, 0, 0, nDirection);//UP
                if (results != null) {

                }
            }


            {
                String strKey = "焊接设备";
                int nDirection = 2;
                InferenceResult[] results = client.getRelatedCompaniesByKey(strKey, 0, 0, 0, nDirection);//UP
                if (results != null) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        int a = 11;
        int b = 10;
        int totalPageNum = (a  +  b  - 1) / b;
        System.out.println("====" + totalPageNum);
    }

}
