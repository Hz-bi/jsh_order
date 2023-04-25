

DROP TABLE IF EXISTS `t_order`;
CREATE TABLE `t_order`  (
                            `id` bigint(20) NOT NULL COMMENT '主键',
                            `distance` int(10) NOT NULL COMMENT '距离',
                            `status` varchar(128)  NOT NULL DEFAULT '' COMMENT '订单状态 UNASSIGNED，SUCCESS',
                            PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_general_ci ROW_FORMAT = Compact COMMENT='订单表';




