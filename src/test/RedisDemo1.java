package test;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import redis.clients.jedis.Jedis;


/**
 * @date:2015-5-5 下午2:47:00
 * @version:V1.0
 */
@SuppressWarnings("unused")
public class RedisDemo1 {
	
	private Jedis jedis;
	
	@Before
	public void setup(){
		//创建连接
		jedis = new Jedis("127.0.0.1", 6379);
		//权限认证
		//jedis.auth("admin");
	}
	
	//redis存储字符串
	@Test
	public void testString(){
		//存
		String setName = jedis.set("name", "zhangsan"); //ok
		String setAge = jedis.set("age", "23");			//ok
		System.out.println(setName + setAge);
		//取
		String getName = jedis.get("name");				//zhangsan
		
		System.out.println(getName);
		//拼接
		jedis.append("name", "feng");
		
		System.out.println(jedis.get("name"));
		
		//删除
		Long result = jedis.del("age");		//1
		System.out.println(result);
		
		//存多个
		String mset = jedis.mset("tel","18093421152","email","123456@qq.com","addr","北京市海淀区");	//ok
		
		Long incr = jedis.incr("age");		//1	?使用意义
		System.out.println(jedis.mget("name","age","tel","email","addr"));
		//[zhangsanfeng, 1, 18093421152, 123456@qq.com, 北京市海淀区]
		
		//System.out.println(incr + mset);
	}
	
	//redis存储Map
	@Test
	public void testMap(){
		Map<String,String> map = new HashMap<String,String>();
		map.put("name", "李四");
		map.put("age", "35");
		map.put("tel", "15012345678");
		map.put("e-mail", "12345678@163.com");
		map.put("addr", "北京市朝阳区");
		//存储集合
		jedis.hmset("user", map);
		
		//遍历
		Iterator<String> iterator = jedis.hkeys("user").iterator();
		while(iterator.hasNext()){
			String key = iterator.next();
			System.out.println(key+":"+jedis.hmget("user", key));
		}
		
		//删除一个
		jedis.hdel("user","age");
		//返回所有key
		System.out.println(jedis.hkeys("user"));
		//返回所有value
		System.out.println(jedis.hvals("user"));
		//判断集合中是否有该key
		System.out.println(jedis.hexists("user", "age"));
		//集合中是否有值
		System.out.println(jedis.exists("user"));
		//字段个数
		System.out.println(jedis.hlen("user"));
		
	}
	
	//list集合
	@Test
	public void testList(){
		jedis.del("items");
		jedis.lpush("items", "1");
		jedis.lpush("items", "5");
		jedis.lpush("items", "3");
		jedis.lpush("items", "8");
		List<String> list = jedis.lrange("items", 0, -1);
		for(int i=0;i<list.size();i++){
			System.out.println("items==>"+list.get(i));
		}
		//排序
		jedis.sort("items");
		List<String> sort = jedis.lrange("items", 0, -1);
		for(int i=0;i<sort.size();i++){
			System.out.println("items==>"+sort.get(i));
		}
		
	}
	
	//set集合
	@Test
	public void testSet(){
		jedis.sadd("person", "zhangsan");
		jedis.sadd("person", "lisi");
		jedis.sadd("person", "wangwu");
		jedis.sadd("person", "zhaoliu");
		
		//移除
		jedis.srem("person","lisi");
		//获取所有的value
		System.out.println(jedis.smembers("person"));
		//获取个数
		System.out.println(jedis.scard("person"));
		//判断是否有元素
		System.out.println(jedis.sismember("person", "zhaoliu"));
		
	}
	
	//清除
	@Test
	public void clean(){
		//清空DB
		jedis.flushDB();
		//清空所有
		jedis.flushAll();
	}
	
	//使用[Redis]连接池
	@Test
    public void testRedisPool() {
        RedisUtils.getJedis().set("newname", "中文测试");
        System.out.println(RedisUtils.getJedis().get("newname"));
    }
	
	//测试redis的set方法
	/**
	 * 测试结果：redis中set()两个相同的key时,后一个会覆盖前一个
	 */
	@Test
	public void testSet_(){
		jedis.set("name", "zhangsan");
		String name = jedis.get("name");
		System.out.println("====1===="+name+"=====1===");
		jedis.set("name", "lisi");
		name = jedis.get("name");
		System.out.println("====2===="+name+"=====2===");
	}
	
}
