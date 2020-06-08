package com.excel.excelproject.controller;

import com.excel.excelproject.dto.TExcelInfo;
import com.excel.excelproject.qo.ExcelInfoQO;
import com.excel.excelproject.res.WsResult;
import com.excel.excelproject.service.ExcelService;
import com.excel.excelproject.util.ExcelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    ExcelService excelService;


    @GetMapping("/menu")
    public String menu() {
        return "menu.html";
    }


    @GetMapping("/upload")
    public String init() {
        return "upload.html";
    }


    @PostMapping(value = "/upload")
    @ResponseBody
    public WsResult upload(MultipartFile file) throws Exception {
        try {
            excelService.insert(file);
        } catch (Exception e) {
            e.printStackTrace();
            return new WsResult(WsResult.FAILD, e.getMessage(), null);
        }
        return new WsResult(WsResult.SUCCESS, "上传成功", null);
    }


    @GetMapping("/search")
    public String searchGet() {
        return "search.html";
    }

    @PostMapping(value = "/search")
    @ResponseBody
    public WsResult search(@RequestBody ExcelInfoQO qo) throws Exception {
        return new WsResult(WsResult.SUCCESS, excelService.select(qo));
    }

    @RequestMapping(value = "/download")
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExcelInfoQO qo = new ExcelInfoQO();
        Class<? extends ExcelInfoQO> aClass = qo.getClass();
        String[] colums = {"idKey","url","remark","str","insertTime"};
        for (String colum : colums) {
            Field declaredField = aClass.getDeclaredField(colum);
            declaredField.setAccessible(true);
            declaredField.set(qo, request.getParameter(colum));
        }

        final String pageNumber = request.getParameter("pageNumber");
        final String pageSize = request.getParameter("pageSize");
        qo.setPageNumber(Integer.parseInt(pageNumber));
        qo.setPageSize(Integer.parseInt(pageSize));
        final List<TExcelInfo> records = excelService.select(qo).getRecords();
        ExcelUtils.export(response, "导出文件", records, colums, colums);
    }

}
