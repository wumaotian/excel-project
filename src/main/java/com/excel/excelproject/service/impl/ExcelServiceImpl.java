package com.excel.excelproject.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.excel.excelproject.dao.TExcelInfoDao;
import com.excel.excelproject.dto.TExcelInfo;
import com.excel.excelproject.qo.ExcelInfoQO;
import com.excel.excelproject.service.ExcelService;
import com.excel.excelproject.util.ExcelUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private TExcelInfoDao excelInfoDao;

    @Override
    public void insert(MultipartFile file) throws Exception {
        String[] colums = {"idKey","url","remark","str","insertTime"};
        List<Map<String, Object>> leading = ExcelUtils.leading(file, colums);
        Class<? extends TExcelInfo> aClass = TExcelInfo.class;

        if (CollectionUtil.isEmpty(leading)) {
            throw new Exception("上传文件为空，请确认上传内容！");
        }


        Object idKey = leading.get(0).get("idKey");
        List<TExcelInfo> list = new LambdaQueryChainWrapper<>(excelInfoDao)
                .eq(TExcelInfo::getIdKey, idKey)
                .list();
        if (CollectionUtil.isNotEmpty(list)) {
            throw new Exception("首行内容重复，请确认后重新提交！ ====> " + idKey);
        }

        for (Map<String, Object> map : leading) {
            TExcelInfo tExcelInfo = new TExcelInfo();
            try {
                for (Map.Entry<String, Object> entry : map.entrySet()) {
                    String k = entry.getKey();
                    Object v = entry.getValue();
                    try {
                        Field field = aClass.getDeclaredField(k);
                        field.setAccessible(true);
                        field.set(tExcelInfo, v.toString());
                    } catch (NoSuchFieldException | IllegalAccessException e) {
                        e.printStackTrace();
                        throw e;
                    }
                }
            } catch (Exception e) {
                throw e;
            }
            excelInfoDao.insert(tExcelInfo);
        }
    }


    @Override
    public List<TExcelInfo> select(ExcelInfoQO qo) throws Exception {
     return new LambdaQueryChainWrapper<>(excelInfoDao)
                .eq(!StringUtils.isEmpty(qo.getIdKey()), TExcelInfo::getIdKey, qo.getIdKey())
                .eq(!StringUtils.isEmpty(qo.getUrl()), TExcelInfo::getUrl, qo.getUrl())
                .eq(!StringUtils.isEmpty(qo.getRemark()), TExcelInfo::getRemark, qo.getRemark())
                .like(!StringUtils.isEmpty(qo.getStr()), TExcelInfo::getStr, "%"+qo.getStr()+"%")
                .le(!StringUtils.isEmpty(qo.getInsertTimeEnd()), TExcelInfo::getInsertTime, StringUtils.isEmpty(qo.getInsertTimeEnd()) ? qo.getInsertTimeEnd() : qo.getInsertTimeEnd().replace("T", " "))
                .ge(!StringUtils.isEmpty(qo.getInsertTimeStart()), TExcelInfo::getInsertTime,StringUtils.isEmpty(qo.getInsertTimeStart()) ? qo.getInsertTimeStart() : qo.getInsertTimeStart().replace("T", " "))
                .list();
    }
}
