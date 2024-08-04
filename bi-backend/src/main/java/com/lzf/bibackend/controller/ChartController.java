package com.lzf.bibackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.lzf.bibackend.common.BaseResponse;
import com.lzf.bibackend.common.ChartStatus;
import com.lzf.bibackend.common.ErrorCode;
import com.lzf.bibackend.common.ResultUtils;
import com.lzf.bibackend.exception.BusinessException;
import com.lzf.bibackend.manager.AiManager;
import com.lzf.bibackend.manager.RedissonManager;
import com.lzf.bibackend.model.dto.ai.DoChatRequest;
import com.lzf.bibackend.model.dto.ai.DoChatResponse;
import com.lzf.bibackend.model.dto.chart.ChartQueryRequest;
import com.lzf.bibackend.model.entity.Chart;
import com.lzf.bibackend.model.entity.User;
import com.lzf.bibackend.model.vo.ChartVO;
import com.lzf.bibackend.mq.MsgProducer;
import com.lzf.bibackend.service.ChartService;
import com.lzf.bibackend.service.UserService;
import com.lzf.bibackend.utils.ExcelUtils;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/chart")
@Tag(name = "chart controller")
public class ChartController {
    @Autowired
    private AiManager aiManager;

    @Autowired
    private RedissonManager redissonManager;

    @Autowired
    private UserService userService;

    @Autowired
    private ChartService chartService;

    @Autowired
    private MsgProducer msgProducer;

    @PostMapping("/generate")
    public BaseResponse<DoChatResponse> generateChartByAi(@RequestPart("file") MultipartFile multipartFile, @RequestPart("doChatRequest") DoChatRequest doChatRequest, HttpServletRequest request) {

        checkGenerateChartParam(multipartFile, doChatRequest);
        // excel 转换
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        doChatRequest.setData(csvData);
        // 登录用户的id
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        // 限流
        String key = "generateChartByAi_" + loginUserId;
        redissonManager.setupRateLimiter(key);
        boolean triedAcquire = redissonManager.tryAcquire(key);
        if (!triedAcquire) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "请求次数过多");
        }

        // 插入数据库
        Chart chart = new Chart();

        // ai 处理
        DoChatResponse doChatResponse = aiManager.doChat(doChatRequest, chart.getId());

        chart.setName(doChatRequest.getName());
        chart.setChartData(doChatRequest.getData());
        chart.setGoal(doChatRequest.getQuestion());
        chart.setChartType(doChatRequest.getChatType());
        chart.setGenChart(doChatResponse.getOption());
        chart.setGenResult(doChatResponse.getMessage());
        chart.setStatus(ChartStatus.SUCCEED.getMessage());
        chart.setUserId(loginUserId);

        boolean saved = chartService.save(chart);
        if (!saved) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "图标保存失败");
        }

        return ResultUtils.success(doChatResponse);
    }

    @PostMapping("/get_all")
    public BaseResponse<List<ChartVO>> getAllByUid(@RequestBody ChartQueryRequest chartQueryRequest, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();

        chartQueryRequest.setUserId(loginUserId);
        QueryWrapper<Chart> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id", loginUserId);
        queryWrapper.eq("status", ChartStatus.SUCCEED);

        Page<Chart> chartPage = new Page<>(chartQueryRequest.getPageNum(), chartQueryRequest.getPageSize());

        Page<Chart> page = chartService.page(chartPage, queryWrapper);
        List<Chart> charts = page.getRecords();
        // chart 转换 chartVO
        List<ChartVO> chartVOs = new ArrayList<>();
        for(Chart chart : charts){
            ChartVO chartVO = new ChartVO();

            chartVO.setId(chart.getId());
            chartVO.setChartType(chart.getChartType());
            chartVO.setName(chart.getName());
            chartVO.setGenChart(chart.getGenChart());
            chartVO.setGenResult(chart.getGenResult());
            chartVO.setGoal(chart.getGoal());
            chartVOs.add(chartVO);
        }
        return ResultUtils.success(chartVOs);
    }

    @PostMapping("/generate/mq")
    public BaseResponse<DoChatResponse> generateChartByAiMQ(@RequestPart("file") MultipartFile multipartFile, @RequestPart("doChatRequest") DoChatRequest doChatRequest, HttpServletRequest request) {
        checkGenerateChartParam(multipartFile, doChatRequest);
        // excel 转换
        String csvData = ExcelUtils.excelToCsv(multipartFile);
        doChatRequest.setData(csvData);
        // 登录用户的id
        User loginUser = userService.getLoginUser(request);
        Long loginUserId = loginUser.getId();
        // 限流
        String key = "generateChartByAi_" + loginUserId;
        redissonManager.setupRateLimiter(key);
        boolean triedAcquire = redissonManager.tryAcquire(key);
        if (!triedAcquire) {
            throw new BusinessException(ErrorCode.TOO_MANY_REQUEST, "请求次数过多");
        }

        // 插入数据库
        Chart chart = new Chart();
        chart.setName(doChatRequest.getName());
        chart.setChartData(doChatRequest.getData());
        chart.setGoal(doChatRequest.getQuestion());
        chart.setChartType(doChatRequest.getChatType());
        chart.setStatus(ChartStatus.WAIT.getMessage());
        chart.setUserId(loginUserId);
        boolean saved = chartService.save(chart);
        if(!saved){
            throw  new BusinessException(ErrorCode.SYSTEM_ERROR, "设置WAIT状态出错");
        }

        msgProducer.send(String.valueOf(chart.getId()));
        DoChatResponse doChatResponse = new DoChatResponse();
        doChatResponse.setChartId(chart.getId());
        return ResultUtils.success(doChatResponse);
    }

    private void checkGenerateChartParam(MultipartFile multipartFile, DoChatRequest doChatRequest) {
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
    }
}
