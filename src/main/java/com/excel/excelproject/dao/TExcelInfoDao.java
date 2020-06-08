package com.excel.excelproject.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.excel.excelproject.dto.TExcelInfo;

import java.util.List;

public interface TExcelInfoDao extends BaseMapper<TExcelInfo> {

    void insertList(List<TExcelInfo> list);

}
