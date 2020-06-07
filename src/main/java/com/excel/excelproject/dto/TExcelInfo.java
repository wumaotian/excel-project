package com.excel.excelproject.dto;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TExcelInfo {

    @TableId(type = IdType.AUTO)
    private String id;

    private String idKey;

    private String url;

    private String remark;

    private String str;
    @TableField(value = "INSERTTIME")
    private String insertTime;

}
