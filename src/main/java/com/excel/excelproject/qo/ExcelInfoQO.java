package com.excel.excelproject.qo;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

@Data
public class ExcelInfoQO {

    private String id;

    private String idKey;

    private String url;

    private String remark;

    private String str;

    private String insertTimeStart;

    private String insertTimeEnd;

}
