package com.excel.excelproject.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.metadata.OrderItem;
import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Service
public class ExcelServiceImpl implements ExcelService {

    @Autowired
    private TExcelInfoDao excelInfoDao;

    @Override
    public void insert(MultipartFile file) throws Exception {
        String[] colums = {"idKey","url","remark","str","insertTime"};
        List<Map<String, Object>> leading = ExcelUtils.leading(file, colums);
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

        // 每500条数据开启一条线程
        int threadSize = 10000;
        // 总数据条数
        int dataSize = leading.size();
        // 线程数
        int threadNum = dataSize / threadSize + 1;
        // 定义标记,过滤threadNum为整数
        boolean special = dataSize % threadSize == 0;


        // 创建一个线程池
        ExecutorService exec = Executors.newFixedThreadPool(threadNum);
        // 定义一个任务集合
        List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
        Callable<Integer> task = null;
        List<Map<String, Object>> cutList = null;

        // 确定每条线程的数据
        for (int i = 0; i < threadNum; i++) {
            if (i == threadNum - 1) {
                if (special) {
                    break;
                }
                cutList = leading.subList(threadSize * i, dataSize);
            } else {
                cutList = leading.subList(threadSize * i, threadSize * (i + 1));
            }
            final List<Map<String, Object>> listStr = cutList;
            task = () -> {
                List<TExcelInfo> recordList = new ArrayList<>();
                Class<? extends TExcelInfo> aClass = TExcelInfo.class;
                for (Map<String, Object> map : listStr) {
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
                        e.printStackTrace();
                    }
                    recordList.add(tExcelInfo);
                }
                excelInfoDao.insertList(recordList);
                return 1;
            };
            // 这里提交的任务容器列表和返回的Future列表存在顺序对应的关系
            tasks.add(task);
        }
        exec.invokeAll(tasks);
        exec.shutdown();

    }




    @Override
    public Page<TExcelInfo> select(ExcelInfoQO qo) throws Exception {
        final String insertTime = qo.getInsertTime();
        String startTime = "";
        String endTime = "";
        if (!StringUtils.isEmpty(insertTime)) {
            final String[] split = insertTime.split(" - ");
            if (split.length == 2) {
                startTime = split[0];
                endTime = split[1];
            }
        }

        final Page<TExcelInfo> page = new Page<>(qo.getPageNumber(), qo.getPageSize());

        final QueryWrapper<TExcelInfo> ge = new QueryWrapper<TExcelInfo>()
                .eq(!StringUtils.isEmpty(qo.getIdKey()), "IDKEY", qo.getIdKey())
                .eq(!StringUtils.isEmpty(qo.getUrl()), "URL", qo.getUrl())
                .eq(!StringUtils.isEmpty(qo.getRemark()), "REMARK", qo.getRemark())
                .like(!StringUtils.isEmpty(qo.getStr()), "STR", "%" + qo.getStr() + "%")
                .le(!StringUtils.isEmpty(endTime), "INSERTTIME", endTime)
                .ge(!StringUtils.isEmpty(startTime), "INSERTTIME", startTime);
        return excelInfoDao.selectPage(page, ge);
    }

}
