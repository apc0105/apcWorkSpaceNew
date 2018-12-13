package com.tk.eventanalysis.service;

import com.tk.eventanalysis.support.Response;
import org.springframework.web.multipart.MultipartFile;

public interface EventAnalysisService {

    Response getAnalysisData(int pageNumber,int pageSize,String keyword);

    Response uploadFile(MultipartFile file);
}
