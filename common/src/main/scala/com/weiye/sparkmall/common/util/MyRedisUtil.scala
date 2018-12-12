package com.weiye.sparkmall.common.util

import org.apache.commons.configuration2.FileBasedConfiguration
import redis.clients.jedis.{Jedis, JedisPool, JedisPoolConfig}

object MyRedisUtil {

  var jedisPool: JedisPool = null

  def getJedisClient: Jedis = {
    if (jedisPool == null) {
      println("开辟一个连接池!")
      val config: FileBasedConfiguration = ConfigurationUtil("config.properties").config
      val host: String = config.getString("redis.host")
      val port: Int = config.getInt("redis.port")

      val jedisPoolConfig = new JedisPoolConfig
      jedisPoolConfig.setMaxTotal(100) //最大连接数
      jedisPoolConfig.setMaxIdle(20) //最小空闲
      jedisPoolConfig.setMinIdle(20) //最小空闲
      jedisPoolConfig.setBlockWhenExhausted(true) //忙碌时是否等待
      jedisPoolConfig.setMaxWaitMillis(500) //忙碌时等待时长  毫秒
      jedisPoolConfig.setTestOnBorrow(true) //每次获得连接前进行测试

      jedisPool = new JedisPool(jedisPoolConfig, host, port)
    }
    println(s"jedisPool.getNumActive = ${jedisPool.getNumActive}")
    println("获得一个连接")
    jedisPool.getResource
  }

}
