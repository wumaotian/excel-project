package com.excel.excelproject.qo;

import cn.hutool.db.Page;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.core.metadata.IPage;
import lombok.Data;

@Data
public class ExcelInfoQO extends Page {

    private String id;

    private String idKey;

    private String url;

    private String remark;

    private String str;

    private String insertTime;
}
