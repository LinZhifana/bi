package com.lzf.bibackend.utils;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.support.ExcelTypeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.ehcache.core.util.CollectionUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
@Slf4j
public class ExcelUtils {
    public static String excelToCsv(MultipartFile multipartFile) {
        List<Map<Integer, String>> list = null;
        try {
            list = EasyExcel.read(multipartFile.getInputStream())
                    .excelType(ExcelTypeEnum.XLSX)
                    .sheet()
                    .headRowNumber(0)
                    .doReadSync();
        } catch (IOException e) {
            // log.error("表格处理错误", e);
            System.err.println("表格处理错误: " + e.getMessage());
            return "";
        }

        if (CollectionUtils.isEmpty(list)) {
            return "";
        }

        StringBuilder stringBuilder = new StringBuilder();

        // 获取表头
        Map<Integer, String> headerMap = list.get(0);
        List<String> headerList = headerMap.keySet().stream()
                .map(headerMap::get)
                .collect(Collectors.toList());
        stringBuilder.append(StringUtils.join(headerList, ",")).append("\n");

        // 读取数据
        for (int i = 1; i < list.size(); i++) {
            Map<Integer, String> dataMap = list.get(i);
            List<String> dataList = headerMap.keySet().stream()
                    .map(dataMap::get)
                    .map(value -> value == null ? "" : value) // 替换空值为空字符串
                    .collect(Collectors.toList());
            stringBuilder.append(StringUtils.join(dataList, ",")).append("\n");
        }

        return stringBuilder.toString();
    }
}
