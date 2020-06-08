package com.excel.excelproject.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.excel.excelproject.dto.TExcelInfo;
import com.excel.excelproject.qo.ExcelInfoQO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ExcelService  {


    void insert(MultipartFile file)  throws Exception ;


    Page<TExcelInfo> select(ExcelInfoQO qo)  throws Exception ;

}
