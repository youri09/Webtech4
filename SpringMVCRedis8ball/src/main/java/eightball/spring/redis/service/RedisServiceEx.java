package eightball.spring.redis.service;

import java.io.UnsupportedEncodingException;
import java.util.Map;
import java.util.Set;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

@Service
public class RedisServiceEx {
	
	private StringRedisTemplate template;
	private static final String REDIS_CHARSET = "utf-8";	
	
	@Autowired
	public void setRedisTemplate(StringRedisTemplate template) {
		this.template = template;
	}
	
	public Long scard(final String key) {
		return template.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				return connection.sCard(key.getBytes());
			}
		});
	}
	
	public String spop(final String key) {
		return template.execute(new RedisCallback<String>() {
			public String doInRedis(RedisConnection connection)
					throws DataAccessException {
				return byteToString(connection.sPop(key.getBytes()));
			}
		});
	}
	
	public Long sadd(final String key, final byte[]... values) {
		return template.execute(new RedisCallback<Long>() {
			public Long doInRedis(RedisConnection connection)
					throws DataAccessException {
				Long r = connection.sAdd(key.getBytes(), values);
				return r;
			}
		});
	}
	
	public void setKey(String key, String value) {
		ValueOperations<String, String> ops = this.template.opsForValue();
		if (!this.template.hasKey(key)) {
			ops.set(key, value);
		}
	}

	public String getKey(String key) {
 		ValueOperations<String, String> ops = this.template.opsForValue();

 		return ops.get(key);
	}

	public Map<Object, Object> hgetAll(String key) {
		 return template.opsForHash().entries(key);
	}
	
	public void hset(String key, Map<String, String> actors) {
		template.opsForHash().putAll(key, actors);
	}
	
	public void sendMessage(String channel, String message) {
		template.convertAndSend(channel, message);
	}
	
	// Methods without Jedis interface
	public Set<byte[]> keys(String pattern) {
		return template.execute((RedisCallback<Set<byte[]>>) conn -> conn.keys(pattern.getBytes()));
	}

	public Boolean setBit(String key, int offset, boolean value) {
		return template.execute((RedisCallback<Boolean>) conn -> conn.setBit(key.getBytes(), offset, value));
	}
	
	public Boolean getBit(String key, int offset) {
		return template.execute((RedisCallback<Boolean>) conn -> conn.getBit(key.getBytes(), offset));
	}
	
	public Long bitCount(String key) {
		return template.execute((RedisCallback<Long>) conn -> conn.bitCount(key.getBytes()));
	}
	
	public void flushDb() {
		template.execute((RedisCallback<Boolean>) conn -> {
			conn.flushDb();
			return null;
	    });
	}
	
	protected String byteToString(byte[] bytes) {
		if (bytes == null)
			return null;
		if (bytes.length == 0)
			return "";
		try {
			return new String(bytes, REDIS_CHARSET);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}

}
