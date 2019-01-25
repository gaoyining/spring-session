/*
 * Copyright 2014-2017 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.session.data.redis.config.annotation.web.http;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.session.MapSession;
import org.springframework.session.Session;
import org.springframework.session.SessionRepository;
import org.springframework.session.config.annotation.web.http.EnableSpringHttpSession;
import org.springframework.session.data.redis.RedisFlushMode;
import org.springframework.session.data.redis.RedisOperationsSessionRepository;
import org.springframework.session.web.http.SessionRepositoryFilter;

/**
 * Add this annotation to an {@code @Configuration} class to expose the
 * {@link SessionRepositoryFilter} as a bean named {@code springSessionRepositoryFilter}
 * and backed by Redis. In order to leverage the annotation, a single
 * {@link RedisConnectionFactory} must be provided. For example:
 *
 * <pre class="code">
 * &#064;Configuration
 * &#064;EnableRedisHttpSession
 * public class RedisHttpSessionConfig {
 *
 *     &#064;Bean
 *     public LettuceConnectionFactory redisConnectionFactory() {
 *         return new LettuceConnectionFactory();
 *     }
 *
 * }
 * </pre>
 *
 * More advanced configurations can extend {@link RedisHttpSessionConfiguration} instead.
 *
 * @author Rob Winch
 * @author Vedran Pavic
 * @since 1.0
 * @see EnableSpringHttpSession
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Import(RedisHttpSessionConfiguration.class)
@Configuration
public @interface EnableRedisHttpSession {

	/**
	 * The session timeout in seconds. By default, it is set to 1800 seconds (30 minutes).
	 * This should be a non-negative integer.
	 * @return the seconds a session can be inactive before expiring
	 *
	 * 会话超时（以秒为单位）。 默认情况下，它设置为1800秒（30分钟）。
	 * 这应该是一个非负整数。
	 * @return 会话在到期前可以处于非活动状态的秒数
	 */
	int maxInactiveIntervalInSeconds() default MapSession.DEFAULT_MAX_INACTIVE_INTERVAL_SECONDS;

	/**
	 * Defines a unique namespace for keys. The value is used to isolate sessions by
	 * changing the prefix from default {@code spring:session:} to
	 * {@code <redisNamespace>:}.
	 * <p>
	 * For example, if you had an application named "Application A" that needed to keep
	 * the sessions isolated from "Application B" you could set two different values for
	 * the applications and they could function within the same Redis instance.
	 * @return the unique namespace for keys
	 *
	 * 为键定义唯一的命名空间。 该值用于通过将前缀从默认{@code spring：session：}更改为来隔离会话
	 * {@code <redisNamespace>：}。
	 * <P>
	 * 例如，如果您有一个名为“Application A”的应用程序需要将会话与“Application B”隔离，
	 * 则可以为应用程序设置两个不同的值，它们可以在同一个Redis实例中运行。
	 * @return键的唯一命名空间
	 */
	String redisNamespace() default RedisOperationsSessionRepository.DEFAULT_NAMESPACE;

	/**
	 * Flush mode for the Redis sessions. The default is {@code ON_SAVE} which only
	 * updates the backing Redis when {@link SessionRepository#save(Session)} is invoked.
	 * In a web environment this happens just before the HTTP response is committed.
	 * <p>
	 * Setting the value to {@code IMMEDIATE} will ensure that the any updates to the
	 * Session are immediately written to the Redis instance.
	 *
	 * Redis会话的刷新模式。 默认值为{@code ON_SAVE}，
	 * 仅在调用{@link SessionRepository＃save（Session）}时更新后备Redis。
	 * 在Web环境中，这发生在提交HTTP响应之前。
	 * <P>
	 * 将值设置为{@code IMMEDIATE}将确保对Session的任何更新立即写入Redis实例。
	 * @return the {@link RedisFlushMode} to use
	 * @since 1.1
	 */
	RedisFlushMode redisFlushMode() default RedisFlushMode.ON_SAVE;

	/**
	 * The cron expression for expired session cleanup job. By default runs every minute.
	 *
	 * 过期会话清理作业的cron表达式。 默认情况下每分钟运行一次
	 *
	 * @return the session cleanup cron expression
	 * @since 2.0.0
	 */
	String cleanupCron() default RedisHttpSessionConfiguration.DEFAULT_CLEANUP_CRON;

}
