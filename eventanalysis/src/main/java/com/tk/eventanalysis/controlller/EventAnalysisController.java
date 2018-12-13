package com.tk.eventanalysis.controlller;

import com.tk.eventanalysis.service.EventAnalysisService;
import com.tk.eventanalysis.support.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class EventAnalysisController {

    @ResponseBody
    @PostMapping(value = "/uploadFile")
    public Response uploadFile(MultipartFile file) {

        if (file.isEmpty()) {
            return Response.error("上传文件不能为空");
        }

        return   eventAnalysisService.uploadFile(file);

    }

    @ResponseBody
    @PostMapping(value = "/getAnalysisData")
    public Response getAnalysisData(int pageNumber, int pageSize,String keyword) {

        return eventAnalysisService.getAnalysisData(pageNumber,pageSize,keyword);

    }

    @Autowired
    private EventAnalysisService eventAnalysisService;
}
