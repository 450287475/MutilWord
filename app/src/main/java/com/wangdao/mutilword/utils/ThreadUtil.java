package com.wangdao.mutilword.utils;


/**
 * Created by yxd on 2016/4/21 for exam part.
 */
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/**
 * 线程池管理
 * 
 */
public class ThreadUtil {
	/**
	 * 固定数量线程池
	 */
	private static ExecutorService executorService = Executors.newFixedThreadPool(1);
	/**
	 * 非固定数量线程池
	 */
	private static ExecutorService moreExecutorService = Executors.newCachedThreadPool();
	
	/**
	 * 该方法为单线程执行
	 * 
	 */
	public static void execute(Runnable runnable) {
		executorService.execute(runnable);
	}

	/**
	 * 非固定数量线程池
	 * 
	 */
	public static void executeMore(Runnable runnable) {
		moreExecutorService.execute(runnable);
	}
	
}
