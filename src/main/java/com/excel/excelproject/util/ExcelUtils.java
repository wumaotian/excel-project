package com.excel.excelproject.util;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.poi.excel.ExcelUtil;
import cn.hutool.poi.excel.ExcelWriter;
import cn.hutool.poi.excel.sax.handler.RowHandler;
import com.excel.excelproject.exception.OperationException;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExcelUtils {


    private static final List<List<Object>> lineList = new ArrayList<>();

    /**
     * excel 导出工具类
     *
     * @param response
     * @param fileName    文件名
     * @param projects    对象集合
     * @param columnNames 导出的excel中的列名
     * @param keys        对应的是对象中的字段名字
     * @throws IOException
     */
    public static void export(HttpServletResponse response, String fileName, List<?> projects, String[] columnNames, String[] keys) throws IOException {

        ExcelWriter bigWriter = ExcelUtil.getBigWriter();

        for (int i = 0; i < columnNames.length; i++) {
            bigWriter.addHeaderAlias(columnNames[i], keys[i]);
            bigWriter.setColumnWidth(i, 20);
        }
        // 一次性写出内容，使用默认样式，强制输出标题
        bigWriter.write(projects, true);
        //response为HttpServletResponse对象
        response.setContentType("application/octet-stream;charset=utf-8");
        //test.xls是弹出下载对话框的文件名，不能为中文，中文请自行编码
        response.setHeader("Content-Disposition", "attachment;filename=" + new String((fileName + ".xlsx").getBytes(), "iso-8859-1"));
        ServletOutputStream out = response.getOutputStream();
        bigWriter.flush(out, true);
        // 关闭writer，释放内存
        bigWriter.close();
        //此处记得关闭输出Servlet流
        IoUtil.close(out);
    }

    /**
     * excel导入工具类
     *
     * @param file       文件
     * @param columNames 列对应的字段名
     * @return 返回数据集合
     * @throws OperationException
     * @throws IOException
     */
    public static List<Map<String, Object>> leading(MultipartFile file, String[] columNames) throws OperationException, IOException {
        String fileName = file.getOriginalFilename();
        // 上传文件为空
        if (StringUtils.isEmpty(fileName)) {
            throw new OperationException("E0001", "没有导入文件");
        }
        /*//上传文件大小为1000条数据
        if (file.getSize() > 1024 * 1024 * 10) {
            throw new OperationException("E0002", "上传失败: 文件大小不能超过10M!");
        }*/
        // 上传文件名格式不正确
        if (fileName.lastIndexOf(".") != -1 && !".xlsx".equals(fileName.substring(fileName.lastIndexOf(".")))) {
            throw new OperationException("E0003", "文件名格式不正确, 请使用后缀名为.XLSX的文件");
        }

        //读取数据
        ExcelUtil.read07BySax(file.getInputStream(), 0, createRowHandler());
        //去除excel中的第一行数据
        lineList.remove(0);

        //将数据封装到list<Map>中
        List<Map<String, Object>> dataList = new ArrayList<>();
        for (int i = 0; i < lineList.size(); i++) {
            if (null != lineList.get(i)) {
                Map<String, Object> hashMap = new HashMap<>();
                for (int j = 0; j < lineList.get(i).size(); j++) {
                    Object property = lineList.get(i).get(j);
                    hashMap.put(columNames[j], property);
                }
                dataList.add(hashMap);
            } else {
                break;
            }
        }
        return dataList;
    }

    /**
     * 通过实现handle方法编写我们要对每行数据的操作方式
     */
    private static RowHandler createRowHandler() {
        //清空一下集合中的数据
        lineList.removeAll(lineList);
        return (i, l, list) -> {
            //将读取到的每一行数据放入到list集合中
            JSONArray jsonObject = new JSONArray(list);
            lineList.add(jsonObject.toList(Object.class));
        };
    }

}
