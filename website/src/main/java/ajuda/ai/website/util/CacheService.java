package ajuda.ai.website.util;

import java.util.Set;

import javax.annotation.Resource;
import javax.ejb.Singleton;

import org.infinispan.Cache;
import org.infinispan.lifecycle.ComponentStatus;
import org.infinispan.manager.EmbeddedCacheManager;
import org.infinispan.stats.CacheContainerStats;

@Singleton
public class CacheService {
	@Resource(lookup = "java:/cache/ajuda-ai")
	private EmbeddedCacheManager cacheManager;
	
	public EmbeddedCacheManager getManager() {
		return cacheManager;
	}

	public <K, V> Cache<K, V> getCache() {
		return cacheManager.getCache();
	}

	public <K, V> Cache<K, V> getCache(final String cacheName) {
		return cacheManager.getCache(cacheName);
	}

	public ComponentStatus getStatus() {
		return cacheManager.getStatus();
	}

	public Set<String> getCacheNames() {
		return cacheManager.getCacheNames();
	}

	public <K, V> Cache<K, V> getCache(final String cacheName, final boolean createIfAbsent) {
		return cacheManager.getCache(cacheName, createIfAbsent);
	}

	public <K, V> Cache<K, V> getCache(final String cacheName, final String configurationName) {
		return cacheManager.getCache(cacheName, configurationName);
	}

	public <K, V> Cache<K, V> getCache(final String cacheName, final String configurationTemplate, final boolean createIfAbsent) {
		return cacheManager.getCache(cacheName, configurationTemplate, createIfAbsent);
	}

	public CacheContainerStats getStats() {
		return cacheManager.getStats();
	}
}
