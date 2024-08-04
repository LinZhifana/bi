package com.lzf.bibackend.mq;

import com.alibaba.excel.util.StringUtils;
import com.lzf.bibackend.common.ChartStatus;
import com.lzf.bibackend.common.ErrorCode;
import com.lzf.bibackend.config.RabbitMQConfig;
import com.lzf.bibackend.exception.BusinessException;
import com.lzf.bibackend.manager.AiManager;
import com.lzf.bibackend.model.dto.ai.DoChatRequest;
import com.lzf.bibackend.model.dto.ai.DoChatResponse;
import com.lzf.bibackend.model.entity.Chart;
import com.lzf.bibackend.service.ChartService;
import com.rabbitmq.client.Channel;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MsgConsumer {

    @Autowired
    ChartService chartService;

    @Autowired
    AiManager aiManager;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME, ackMode = "MANUAL")
    public void receive(String msg, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) throws IOException {
        // 检测消息
        if (StringUtils.isBlank(msg)) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "消息为空");
        }

        long chartId = Long.parseLong(msg);
        Chart chart = chartService.getById(chartId);

        if (chart == null) {
            channel.basicNack(deliveryTag, false, false);
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR, "图表为空");
        }

        // 改为正在处理
        Chart updateChart = new Chart();
        updateChart.setId(chart.getId());
        updateChart.setStatus(ChartStatus.RUNNING.getMessage());
        boolean updatedRunning = chartService.updateById(updateChart);
        if (!updatedRunning) {
            channel.basicNack(deliveryTag, false, false);
            handleFailedChart(updateChart, "设置图表RUNNING时出错");
        }

        DoChatRequest doChatRequest = new DoChatRequest();

        doChatRequest.setData(chart.getChartData());
        doChatRequest.setChatType(chart.getChartType());
        doChatRequest.setName(chart.getName());
        doChatRequest.setQuestion(chart.getGoal());

        // 调用 ai
        DoChatResponse doChatResponse = aiManager.doChat(doChatRequest, chartId);

        // 改为成功
        updateChart.setGenChart(doChatResponse.getOption());
        updateChart.setGenResult(doChatResponse.getMessage());
        updateChart.setStatus(ChartStatus.SUCCEED.getMessage());

        boolean updateSucceed = chartService.updateById(updateChart);
        if(!updateSucceed) {
            channel.basicNack(deliveryTag, false, false);
            handleFailedChart(updateChart, "设置图表Succeed时出错");
        }

        channel.basicAck(deliveryTag, false);
    }

    private void handleFailedChart(Chart chart, String msg) {
        Chart failedChart = new Chart();
        failedChart.setId(chart.getId());
        failedChart.setStatus(ChartStatus.FAILED.getMessage());
        failedChart.setExecMessage(msg);
        chartService.updateById(failedChart);
    }
}
