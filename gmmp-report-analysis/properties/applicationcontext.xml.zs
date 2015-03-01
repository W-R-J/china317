<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns="http://www.springframework.org/schema/beans"
	xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xmlns:aop="http://www.springframework.org/schema/aop"
	xmlns:tx="http://www.springframework.org/schema/tx"
	xsi:schemaLocation="  
     http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-2.5.xsd  
     http://www.springframework.org/schema/tx http://www.springframework.org/schema/tx/spring-tx-2.5.xsd  
     http://www.springframework.org/schema/aop http://www.springframework.org/schema/aop/spring-aop-2.5.xsd">

	<!-- 数据源1 -->
	<bean id="dataSource1" class="org.apache.commons.dbcp.BasicDataSource">
		<property name="driverClassName">
			<value>oracle.jdbc.driver.OracleDriver</value>
		</property>
		<property name="url">
			<value>jdbc:oracle:thin:@192.168.200.7:1521:oracle</value>
		</property>
		<property name="username">
			<value>gmmp</value>
		</property>
		<property name="password">
			<value>gmmp317</value>
		</property>
		<property name="maxActive">
			<value>100</value>
		</property>
		<property name="maxIdle">
			<value>8</value>
		</property>
		<property name="minIdle">
			<value>1</value>
		</property>
	</bean>

	<!-- 数据源2 -->
	<bean id="dataSource2" class="org.apache.commons.dbcp.BasicDataSource">
		<property name="driverClassName">
			<value>oracle.jdbc.driver.OracleDriver</value>
		</property>
		<property name="url">
			<value>jdbc:oracle:thin:@192.168.200.7:1521:oracle</value>
		</property>
		<property name="username">
			<value>gmmpraw</value>
		</property>
		<property name="password">
			<value>gmmpraw</value>
		</property>
		<property name="maxActive">
			<value>100</value>
		</property>
		<property name="maxIdle">
			<value>8</value>
		</property>
		<property name="minIdle">
			<value>1</value>
		</property>
	</bean>
	<!-- dgm -->
	<bean id="dataSourceDgm" class="org.apache.commons.dbcp.BasicDataSource">
		<property name="driverClassName">
			<value>oracle.jdbc.driver.OracleDriver</value>
		</property>
		<property name="url">
			<value>jdbc:oracle:thin:@192.168.200.7:1521:oracle</value>
		</property>
		<property name="username">
			<value>dgm</value>
		</property>
		<property name="password">
			<value>dgm_china317_com</value>
		</property>
		<property name="maxActive">
			<value>100</value>
		</property>
		<property name="maxIdle">
			<value>8</value>
		</property>
		<property name="minIdle">
			<value>1</value>
		</property>
	</bean>
	<!-- ptm -->
	<bean id="dataSourcePtm" class="org.apache.commons.dbcp.BasicDataSource">
		<property name="driverClassName">
			<value>oracle.jdbc.driver.OracleDriver</value>
		</property>
		<property name="url">
			<value>jdbc:oracle:thin:@192.168.200.7:1521:oracle</value>
		</property>
		<property name="username">
			<value>ptm</value>
		</property>
		<property name="password">
			<value>ptm057</value>
		</property>
		<property name="maxActive">
			<value>100</value>
		</property>
		<property name="maxIdle">
			<value>8</value>
		</property>
		<property name="minIdle">
			<value>1</value>
		</property>
	</bean>

	<!-- lybc -->
	<bean id="dataSourceLybc" class="org.apache.commons.dbcp.BasicDataSource">
		<property name="driverClassName">
			<value>oracle.jdbc.driver.OracleDriver</value>
		</property>
		<property name="url">
			<value>jdbc:oracle:thin:@192.168.200.7:1521:oracle</value>
		</property>
		<property name="username">
			<value>lybcapp</value>
		</property>
		<property name="password">
			<value>lybcapp_317</value>
		</property>
		<property name="maxActive">
			<value>100</value>
		</property>
		<property name="maxIdle">
			<value>8</value>
		</property>
		<property name="minIdle">
			<value>1</value>
		</property>
	</bean>
	<!-- 事务1 -->
	<bean id="transactionManager1"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource1" />
	</bean>
	<!-- 事务2 -->
	<bean id="transactionManager2"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSource2" />
	</bean>
	<!-- 事务dgm -->
	<bean id="transactionManagerDgm"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSourceDgm" />
	</bean>
	<!-- 事务ptm -->
	<bean id="transactionManagerPtm"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSourcePtm" />
	</bean>
	<!-- 事务lybc -->
	<bean id="transactionManagerLybc"
		class="org.springframework.jdbc.datasource.DataSourceTransactionManager">
		<property name="dataSource" ref="dataSourceLybc" />
	</bean>

	<!-- 事务拦截1 -->
	<bean id="transactionInterceptor1"
		class="org.springframework.transaction.interceptor.TransactionInterceptor">
		<property name="transactionManager" ref="transactionManager1" />
		<property name="transactionAttributes">
			<props>
				<prop key="insert*">PROPAGATION_REQUIRED</prop>
				<prop key="delete*">PROPAGATION_REQUIRED</prop>
				<prop key="update*">PROPAGATION_REQUIRED</prop>
				<prop key="do*">PROPAGATION_REQUIRED</prop>
			</props>
		</property>
	</bean>
	<!-- 事务拦截2 -->
	<bean id="transactionInterceptor2"
		class="org.springframework.transaction.interceptor.TransactionInterceptor">
		<property name="transactionManager" ref="transactionManager2" />
		<property name="transactionAttributes">
			<props>
				<prop key="insert*">PROPAGATION_REQUIRED</prop>
				<prop key="delete*">PROPAGATION_REQUIRED</prop>
				<prop key="update*">PROPAGATION_REQUIRED</prop>
				<prop key="do*">PROPAGATION_REQUIRED</prop>
			</props>
		</property>
	</bean>
	<!-- 事务拦截Dgm -->
	<bean id="transactionInterceptorDgm"
		class="org.springframework.transaction.interceptor.TransactionInterceptor">
		<property name="transactionManager" ref="transactionManagerDgm" />
		<property name="transactionAttributes">
			<props>
				<prop key="insert*">PROPAGATION_REQUIRED</prop>
				<prop key="delete*">PROPAGATION_REQUIRED</prop>
				<prop key="update*">PROPAGATION_REQUIRED</prop>
				<prop key="do*">PROPAGATION_REQUIRED</prop>
			</props>
		</property>
	</bean>
	<!-- 事务拦截2 -->
	<bean id="transactionInterceptorPtm"
		class="org.springframework.transaction.interceptor.TransactionInterceptor">
		<property name="transactionManager" ref="transactionManagerPtm" />
		<property name="transactionAttributes">
			<props>
				<prop key="insert*">PROPAGATION_REQUIRED</prop>
				<prop key="delete*">PROPAGATION_REQUIRED</prop>
				<prop key="update*">PROPAGATION_REQUIRED</prop>
				<prop key="do*">PROPAGATION_REQUIRED</prop>
			</props>
		</property>
	</bean>
	<!-- 事务拦截2 -->
	<bean id="transactionInterceptorLybc"
		class="org.springframework.transaction.interceptor.TransactionInterceptor">
		<property name="transactionManager" ref="transactionManagerLybc" />
		<property name="transactionAttributes">
			<props>
				<prop key="insert*">PROPAGATION_REQUIRED</prop>
				<prop key="delete*">PROPAGATION_REQUIRED</prop>
				<prop key="update*">PROPAGATION_REQUIRED</prop>
				<prop key="do*">PROPAGATION_REQUIRED</prop>
			</props>
		</property>
	</bean>

	<!-- 管理你连接的地方 -->
	<bean id="autoProxyCreator"
		class="org.springframework.aop.framework.autoproxy.BeanNameAutoProxyCreator">
		<property name="beanNames">
			<value>*Service</value>
		</property>
		<property name="interceptorNames">
			<list>
				<value>transactionInterceptor1</value>
				<value>transactionInterceptor2</value>
				<value>transactionInterceptorDgm</value>
				<value>transactionInterceptorPtm</value>
				<value>transactionInterceptorLybc</value>
			</list>
		</property>
	</bean>

	<!-- ibatis的工厂数据源配置1 -->
	<bean id="sqlMapClient1" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
		<property name="configLocation" value="classpath:ibatisConfig.xml" /><!--这里是ibatis的sqlMap文件集合 -->
		<property name="dataSource" ref="dataSource1" />
	</bean>

	<!-- ibatis的工厂数据源配置2 -->
	<bean id="sqlMapClient2" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
		<property name="configLocation" value="classpath:ibatisConfig.xml" /><!--这里是ibatis的sqlMap文件集合 -->
		<property name="dataSource" ref="dataSource2" />
	</bean>

	<!-- ibatis的工厂数据源配置Dgm -->
	<bean id="sqlMapClientDgm" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
		<property name="configLocation" value="classpath:ibatisConfig.xml" /><!--这里是ibatis的sqlMap文件集合 -->
		<property name="dataSource" ref="dataSourceDgm" />
	</bean>

	<!-- ibatis的工厂数据源配置Ptm -->
	<!-- <bean id="sqlMapClientPtm" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
		<property name="configLocation" value="classpath:ibatisConfigPtm.xml" />这里是ibatis的sqlMap文件集合
		<property name="dataSource" ref="dataSourcePtm" />
	</bean> -->

	<!-- ibatis的工厂数据源配置Lybc -->
	<!-- <bean id="sqlMapClientLybc" class="org.springframework.orm.ibatis.SqlMapClientFactoryBean">
		<property name="configLocation" value="classpath:ibatisConfigLybc.xml" />这里是ibatis的sqlMap文件集合
		<property name="dataSource" ref="dataSourceLybc" />
	</bean> -->
	<!-- Dao bean -->
	<bean id="ruleDao"
		class="com.china317.gmmp.gmmp_report_analysis.dao.imp.RuleDaoImp">
		<property name="sqlMapClient">
			<ref bean="sqlMapClientDgm" />
		</property>
	</bean>
	<!-- Dao bean -->
	<bean id="vehicleDao"
		class="com.china317.gmmp.gmmp_report_analysis.dao.imp.VehicleDaoImp">
		<property name="sqlMapClient">
			<ref bean="sqlMapClient1" />
		</property>
	</bean>
	<!-- Dao bean -->
	<bean id="vehicleLocateDaoGmmp"
		class="com.china317.gmmp.gmmp_report_analysis.dao.imp.VehicleLocateDaoImp">
		<property name="sqlMapClient">
			<ref bean="sqlMapClient1" />
		</property>
	</bean>
	<!-- Dao bean -->
	<bean id="vehicleLocateDaoGmmpRaw"
		class="com.china317.gmmp.gmmp_report_analysis.dao.imp.VehicleLocateDaoImp">
		<property name="sqlMapClient">
			<ref bean="sqlMapClient2" />
		</property>
	</bean>

	<!-- ibatis抽象的Dao1 -->
	<!-- <bean id="baseIbatisDAO1" abstract="true"> <property name="sqlMapClient"> 
		<ref local="sqlMapClient1" /> </property> </bean> -->

	<!-- ibatis抽象的Dao2 -->
	<!-- <bean id="baseIbatisDAO2" abstract="true"> <property name="sqlMapClient"> 
		<ref local="sqlMapClient2" /> </property> </bean> -->
</beans>  