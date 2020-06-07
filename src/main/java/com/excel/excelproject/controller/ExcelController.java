package com.excel.excelproject.controller;

import com.excel.excelproject.dto.TExcelInfo;
import com.excel.excelproject.qo.ExcelInfoQO;
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
    public void upload(MultipartFile file) throws Exception {
        excelService.insert(file);
    }


    @GetMapping("/search")
    public String searchGet() {
        return "search.html";
    }

    @PostMapping(value = "/search")
    @ResponseBody
    public List<TExcelInfo> search(@RequestBody ExcelInfoQO qo) throws Exception {
        return excelService.select(qo);
    }

    @RequestMapping(value = "/download")
    public void downLoad(HttpServletRequest request, HttpServletResponse response) throws Exception {
        ExcelInfoQO qo = new ExcelInfoQO();
        Class<? extends ExcelInfoQO> aClass = qo.getClass();
        String[] colums = {"idKey","url","remark","str","insertTime"};
        String[] params = {"idKey","url","remark","str","insertTimeStart","insertTimeEnd"};
        for (String colum : params) {
            Field declaredField = aClass.getDeclaredField(colum);
            declaredField.setAccessible(true);
            declaredField.set(qo, request.getParameter(colum));
        }
        List<TExcelInfo> select = excelService.select(qo);
        ExcelUtils.export(response, "导出文件", select, colums, colums);
    }

}
