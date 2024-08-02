package com.lzf.bibackend.model.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;
import lombok.Data;

/**
 * 图表信息表
 */
@Data
@TableName(value = "chart")
public class Chart {
    /**
     * id
     */
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 分析目标
     */
    @TableField(value = "goal")
    private String goal;

    /**
     * 图表名称
     */
    @TableField(value = "`name`")
    private String name;

    /**
     * 图表数据
     */
    @TableField(value = "chart_data")
    private String chartData;

    /**
     * 图表类型
     */
    @TableField(value = "chart_type")
    private String chartType;

    /**
     * 生成的图表数据
     */
    @TableField(value = "gen_chart")
    private String genChart;

    /**
     * 生成的分析结论
     */
    @TableField(value = "gen_result")
    private String genResult;

    /**
     * wait,running,succeed,failed
     */
    @TableField(value = "`status`")
    private String status;

    /**
     * 执行信息
     */
    @TableField(value = "exec_message")
    private String execMessage;

    /**
     * 创建用户 id
     */
    @TableField(value = "user_id")
    private Long userId;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @TableField(value = "update_time")
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableField(value = "is_delete")
    private Byte isDelete;
}