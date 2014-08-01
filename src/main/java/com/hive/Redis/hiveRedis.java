package main.java.com.hive.Redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class hiveRedis {
	
	private Jedis redis;
	private JedisPool pool;
	private String user;
	
	public hiveRedis( String user ) {
		this.user = user;
	}
	
	public void connectRedis() {
		this.pool = new JedisPool(new JedisPoolConfig(), "localhost");
	}
	
	public void connectRedis( String host, int port ) {
		this.pool = new JedisPool(new JedisPoolConfig(), host, port, 5000);
	}
	
	public void putURL( String url ) {
		this.redis = pool.getResource();
		try {
			redis.lpush(user, url);
		} finally {
			pool.returnResource(redis);
		}
	}
	
	public String getURL() {
		String url = null;
		this.redis = pool.getResource();
		try {
			if( getLength() != 0 ) {
				url = redis.rpop(user);
			}
		} finally {
			pool.returnResource(redis);
		}
		return url;
	}
	
	public long getLength() {
		this.redis = pool.getResource();
		long length = redis.llen(this.user);
		pool.returnResource(redis);
		return length;
	}
	
	public void remove( String url ) {
		this.redis = pool.getResource();
		redis.lrem(user, 0, url);
		pool.returnResource(redis);
	}
	
	public void destory() {
		this.redis.flushAll();
		pool.destroy();
	}
}
