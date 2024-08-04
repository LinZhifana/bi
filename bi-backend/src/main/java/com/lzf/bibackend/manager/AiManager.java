package com.lzf.bibackend.manager;

import com.lzf.bibackend.client.AiClient;
import com.lzf.bibackend.common.BaseResponse;
import com.lzf.bibackend.common.ChartStatus;
import com.lzf.bibackend.common.ErrorCode;
import com.lzf.bibackend.exception.BusinessException;
import com.lzf.bibackend.model.dto.ai.DoChatRequest;
import com.lzf.bibackend.model.dto.ai.DoChatResponse;
import com.lzf.bibackend.model.entity.Chart;
import com.lzf.bibackend.service.ChartService;
import com.lzf.bibackend.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class AiManager {
    @Autowired
    private AiClient client;

    public DoChatResponse doChat(DoChatRequest doChatRequest, HttpServletRequest httpServletRequest, Long chartId) throws BusinessException {
        String content = client.doChat(doChatRequest);
        String[] contents = content.split("\\+\\+\\+\\+");
        if (contents.length < 3) {
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "ai 输出异常");
        }

        String option = contents[1].trim();
        String message = contents[2].trim();

        DoChatResponse doChatResponse = new DoChatResponse();
        doChatResponse.setChartId(chartId);
        doChatResponse.setMessage(message);
        doChatResponse.setOption(option);

        return doChatResponse;
    }

}
