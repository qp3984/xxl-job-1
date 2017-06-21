package com.xxl.job.database.pool;

import com.xxl.job.database.DatabaseException;
import com.xxl.job.database.lifecycle.LifecycleAware;
import com.xxl.job.database.lifecycle.LifecycleState;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 数据库连接池
 *
 * @author dongpo.jia
 */
public class DBConnectionPool implements LifecycleAware {

    static Logger log = LoggerFactory.getLogger(DBConnectionPool.class);

    LifecycleState state;
    private static final String DS_DEPLOY_PATH = "com.xxl.job.database.deploy.path";

    private static ConcurrentHashMap<String, BasicDataSource> dsMap = new ConcurrentHashMap<String, BasicDataSource>();
    private static Lock poollock = new ReentrantLock();

    private static String deployPath;

    public DBConnectionPool(Properties prop) {
        deployPath = prop.getProperty(DS_DEPLOY_PATH, "./deploy");
    }

    /* 遍历数据源目录，加载所有数据源
     * @see LifecycleAware#start()
     */
    @Override
    public void start() {
        deployPath = deployPath + "/datasource";
        File dir = new File(deployPath);
        if (!dir.exists()) {
            log.warn("Datasource deploy dir [" + deployPath + "] is not exist.");
            return;
        }

        File[] files = dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".ds");
            }
        });
        for (File file : files) {
            createConnectionPool(file);
        }
        state = LifecycleState.START;
    }

    /**
     * 创建数据库连接池
     *
     * @param file
     */
    public void createConnectionPool(File file) {
        String name = file.getName().substring(0,
                file.getName().lastIndexOf('.'));
        Properties p = new Properties();
        FileReader reader;
        try {
            reader = new FileReader(file);
            try {
                p.load(reader);
                BasicDataSource ds = (BasicDataSource) BasicDataSourceFactory
                        .createDataSource(p);
                ds.setAccessToUnderlyingConnectionAllowed(true);
                dsMap.put(name, ds);
                log.info("Create connection pool succeed, datasource filename["
                        + file.getAbsolutePath() + "]");
            } catch (Exception e) {
                log.error("Create connection pool error, datasource filename["
                        + file.getAbsolutePath() + "]", e);
            } finally {
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        log.error(
                                "connection pool close error, datasource filename["
                                        + file.getAbsolutePath() + "]", e);
                    }
                }
            }
        } catch (FileNotFoundException e1) {
            log.error("Create connection pool error, datasource filename["
                    + file.getAbsolutePath() + "]" + "Not Found!", e1);
        }
    }


    /**
     * 关闭数据源连接池
     *
     * @param name
     * @return
     * @throws SQLException
     */
    public static void closePooling(String name) throws DatabaseException {
        try {
            poollock.lock();
            forceClosePool(name);
        } finally {
            poollock.unlock();
        }
    }

    /**
     * 强制关闭数据源
     *
     * @param name
     * @return
     * @throws DatabaseException
     */
    public static void forceClosePool(String name) throws DatabaseException {
        if (dsMap.get(name) != null) {
            try {
                dsMap.get(name).close();
            } catch (SQLException e) {
                throw new DatabaseException(e);
            }
            dsMap.remove(name);
        } else {
            throw new DatabaseException(
                    "DataSource name is null,please input datasource name.");
        }
    }

    @Override
    public void stop() {
        Set<String> dsNames = dsMap.keySet();
        for (String dsName : dsNames) {
            try {
                forceClosePool(dsName);
            } catch (DatabaseException e) {
                log.error(e.getMessage(), e);
            }
        }

        state = LifecycleState.STOP;
        log.info("Stop DBConnectionPool OK.");
    }

    @Override
    public LifecycleState getLifecycleState() {
        return state;
    }

    /**
     * 获得指定数据源连接
     *
     * @param dsName 数据源名称
     * @return
     * @throws Exception
     */
    public static Connection getConnection(String dsName) throws Exception {
        Connection conn = null;
        try {
            poollock.lock();
            BasicDataSource bds = dsMap.get(dsName);
            if (bds == null) {
                throw new DatabaseException("Can not get connection for ds["
                        + dsName + "].");
            }

            if (bds != null && (bds.getNumActive() < bds.getMaxActive())) {// 当前连接数据小于最大连接
                conn = dsMap.get(dsName).getConnection();
                log.info("after getConnection " + getDataSourceState(dsName)
                        + " max[" + bds.getMaxActive() + "]");
            } else {
                throw new Exception("can not get connection![active("
                        + bds.getNumActive() + ") max(" + bds.getMaxActive()
                        + ")]");
            }
            return conn;
        } catch (SQLException e1) {
            throw new DatabaseException("Can not get connection,", e1);
        } finally {
            poollock.unlock();
        }
    }


    /**
     * 获得数据源状态描述
     *
     * @param dbsourcename
     * @return
     */
    public static String getDataSourceState(String dbsourcename) {
        if (dbsourcename == null) {
            return "DataSourceName[" + dbsourcename + "]";
        }
        BasicDataSource bds = dsMap.get(dbsourcename);
        if (bds == null) {
            return "DataSourceName[" + dbsourcename + "] not found!";
        }
        String url = bds.getUrl();
        int i = bds.getNumActive();
        int j = bds.getNumIdle();

        int k = bds.getMaxActive();
        String rtn = "DataSourceName[" + dbsourcename + "]; url[" + url + "]; MaxActive[" + k + "]; Active[" + i + "]; Idle[" + j + "]";
        return rtn;
    }

    /**
     * 获得数据源状态描述
     *
     * @return
     */
    public static List<Map<String, Map<String, Object>>> getDataSources() {
        if (dsMap == null) {
            return null;
        }
        Set<Entry<String, BasicDataSource>> entrySet = dsMap.entrySet();
        Iterator<Entry<String, BasicDataSource>> iterator = entrySet.iterator();

        ArrayList<Map<String, Map<String, Object>>> lists = new ArrayList<Map<String, Map<String, Object>>>();
        Map<String, Map<String, Object>> mapss = new HashMap<String, Map<String, Object>>();
        Map<String, Object> maps = new HashMap<String, Object>();
        while (iterator.hasNext()) {
            Entry<String, BasicDataSource> entry = iterator.next();
            String dbsourcename = entry.getKey();
            BasicDataSource bds = entry.getValue();

            Properties props = readValue(getDSFileName(dbsourcename));
            for (final String name : props.stringPropertyNames()) {
                maps.put(name, props.getProperty(name));
            }

            int numActive = bds.getNumActive();//运行中
            int numIdle = bds.getNumIdle();//空闲
            maps.put("numActive", numActive);
            maps.put("numIdle", numIdle);

            BasicDataSource dataSource = getDataSource(dbsourcename);
            if (dataSource == null) {
                maps.put("status", 1);// 状态标识，未启动
            } else {
                maps.put("status", 0);// 状态标识，启动中
            }
            mapss.put(dbsourcename, maps);
        }
        lists.add(mapss);
        return lists;
    }

    /**
     * 获得指定数据源状态描述
     *
     * @param dbsourcename
     * @return
     */
    public static Map<String, Map<String, Object>> getDS(String dbsourcename) {
        if (dbsourcename == null || dsMap == null) {
            // "数据源名称不能为空！"
        }
        BasicDataSource bds = getDataSource(dbsourcename);
        Map<String, Map<String, Object>> mapss = new HashMap<String, Map<String, Object>>();
        Map<String, Object> maps = new HashMap<String, Object>();

        Properties props = readValue(getDSFileName(dbsourcename));
        for (final String name : props.stringPropertyNames()) {
            maps.put(name, props.getProperty(name));
        }

        int numActive = bds.getNumActive();//运行中
        int numIdle = bds.getNumIdle();//空闲
        maps.put("numActive", numActive);
        maps.put("numIdle", numIdle);

        if (bds == null) {
            maps.put("status", 1);// 状态标识，未启动
        } else {
            maps.put("status", 0);// 状态标识，启动中
        }
        mapss.put(dbsourcename, maps);
        return mapss;
    }

    /**
     * TODO get
     *
     * @param dbsourcename
     * @return
     * @see [类、类#方法、类#成员]
     */
    public static BasicDataSource getDataSource(String dbsourcename) {
        if (dbsourcename == null) {
            log.debug("DataSourceName[" + dbsourcename + "]");
            return null;
        }
        BasicDataSource bds = dsMap.get(dbsourcename);
        return bds;
    }

    /**
     * 获取指定数据源的文件路径
     *
     * @param dsName 数据源的名字，如mysql
     * @return
     */
    private static String getDSFileName(String dsName) {
        //deployPath = deployPath + "/datasource";
        File dir = new File(deployPath);
        if (!dir.exists()) {
            log.warn("Datasource deploy dir [" + deployPath + "] is not exist.");
            return null;
        }
        String pathName = dir + File.separator + dsName + ".ds";
        return pathName;
    }

    /**
     * 根据主键key读取主键的值value
     *
     * @param filePath 属性文件路径
     */
    public static Properties readValue(String filePath) {
        Properties props = new Properties();
        try {
            InputStream in = new BufferedInputStream(new FileInputStream(
                    filePath));
            props.load(in);
            return props;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String args[]) throws Exception {
        Properties prop = new Properties();
        prop.put("com.xxl.job.database.deploy.path", "D:\\Workspaces\\GithubSpacesJiadongpo\\xxl-job\\xxl-job-database\\src\\main\\resources\\deploy");
        DBConnectionPool pool = new DBConnectionPool(prop);
        pool.start();
        String dsName = "mysql_101";
        System.out.println(DBConnectionPool.getDataSourceState(dsName));
        DBConnectionPool.getConnection(dsName);
        System.out.println(DBConnectionPool.getDataSources());
    }

}
