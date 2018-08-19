package com.lvonce.lightfileserver.general

/**
 * twitter的snowflake算法 -- kotlin 实现
 */
class SnowFlake(private val datacenterId: Long  //数据中心
                , private val machineId: Long     //机器标识
) {


    private var sequence = 0L //序列号
    private var lastStmp = -1L//上一次时间戳

    private val nextMill: Long
        get() {
            var mill = newstmp
            while (mill <= lastStmp) {
                mill = newstmp
            }
            return mill
        }

    private val newstmp: Long
        get() = System.currentTimeMillis()

    init {
        if (datacenterId > MAX_DATACENTER_NUM || datacenterId < 0) {
            throw IllegalArgumentException("datacenterId can't be greater than MAX_DATACENTER_NUM or less than 0")
        }
        if (machineId > MAX_MACHINE_NUM || machineId < 0) {
            throw IllegalArgumentException("machineId can't be greater than MAX_MACHINE_NUM or less than 0")
        }
    }

    /**
     * 产生下一个ID
     *
     * @return
     */
    @Synchronized
    fun nextId(): Long {
        var currStmp = newstmp
        if (currStmp < lastStmp) {
            throw RuntimeException("Clock moved backwards.  Refusing to generate id")
        }

        if (currStmp == lastStmp) {
            //相同毫秒内，序列号自增
            sequence = sequence + 1 and MAX_SEQUENCE
            //同一毫秒的序列数已经达到最大
            if (sequence == 0L) {
                currStmp = nextMill
            }
        } else {
            //不同毫秒内，序列号置为0
            sequence = 0L
        }

        lastStmp = currStmp

        return (currStmp - START_STMP shl TIMESTMP_LEFT
                or (datacenterId shl DATACENTER_LEFT       //数据中心部分
                )
                or (machineId shl MACHINE_LEFT             //机器标识部分
                )
                or sequence)                             //序列号部分
    }

    companion object {

        /**
         * 起始的时间戳
         */
        private val START_STMP = 1480166465631L

        /**
         * 每一部分占用的位数
         */
        private val SEQUENCE_BIT: Int = 12 //序列号占用的位数
        private val MACHINE_BIT: Int = 5   //机器标识占用的位数
        private val DATACENTER_BIT: Int = 5//数据中心占用的位数

        /**
         * 每一部分的最大值
         */
        private val MAX_DATACENTER_NUM = -1L xor (-1L shl DATACENTER_BIT)
        private val MAX_MACHINE_NUM = -1L xor (-1L shl MACHINE_BIT)
        private val MAX_SEQUENCE = -1L xor (-1L shl SEQUENCE_BIT)

        /**
         * 每一部分向左的位移
         */
        private val MACHINE_LEFT = SEQUENCE_BIT
        private val DATACENTER_LEFT = SEQUENCE_BIT + MACHINE_BIT
        private val TIMESTMP_LEFT = DATACENTER_LEFT + DATACENTER_BIT
    }
}
