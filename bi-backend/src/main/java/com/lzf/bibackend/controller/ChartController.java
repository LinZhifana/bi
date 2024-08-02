package com.lzf.bibackend.controller;

import com.alibaba.excel.util.FileUtils;
import com.lzf.bibackend.common.BaseResponse;
import com.lzf.bibackend.common.ErrorCode;
import com.lzf.bibackend.common.ResultUtils;
import com.lzf.bibackend.exception.BusinessException;
import com.lzf.bibackend.manager.AiManager;
import com.lzf.bibackend.model.dto.ai.DoChatRequest;
import com.lzf.bibackend.model.dto.ai.DoChatResponse;
import com.lzf.bibackend.model.vo.AiVO;
import com.lzf.bibackend.service.ChartService;
import com.lzf.bibackend.service.UserService;
import com.lzf.bibackend.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.util.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/chart")
@Tag(name = "chart controller")
public class ChartController {
    @Autowired
    private AiManager aiManager;

    @PostMapping("/generate")
    public BaseResponse<DoChatResponse> generateChartByAi(@RequestPart("file") MultipartFile multipartFile, @RequestPart("doChatRequest") DoChatRequest doChatRequest, HttpServletRequest request) {

        // 参数检验
        if (StringUtils.isBlank(doChatRequest.getQuestion())) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "提问/目标为空");
        }
        if (StringUtils.isBlank(doChatRequest.getName()) || doChatRequest.getName().length() > 100) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件标题过长");
        }
        final long ONE_MB = 1024 * 1024;
        if (multipartFile.getSize() > ONE_MB) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件超过1M");
        }
        String originalFilename = multipartFile.getOriginalFilename();
        String suffix = "";
        if (originalFilename != null && !originalFilename.isEmpty()) {
            int index = originalFilename.lastIndexOf(".");
            if (index > 0) {
                suffix = originalFilename.substring(index + 1).toLowerCase();
            }
        }
        final List<String> validFileSuffixList = Arrays.asList("xlsx", "xls");
        if (!validFileSuffixList.contains(suffix)) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "文件格式不对");
        }

        String csvData = ExcelUtils.excelToCsv(multipartFile);
        doChatRequest.setData(csvData);
        DoChatResponse doChatResponse = aiManager.doChat(doChatRequest, request);
        return ResultUtils.success(doChatResponse);
    }
}
