create table if not exists hbase2hive(
uId int,
uName string
)
stored by 'org.apache.hadoop.hive.hbase.HBaseStorageHandler'
with serdeproperties("hbase.collumns.mapping"=":key,cf1:uName")
tblproperties("hbase.table.name" = "h2h")
;


-- 错误
-- hbase.HTableDescriptor.addFamily(HColumnDescriptor)V

-- 解决
-- 将hive-hbase-handle.jar重新打包
-- 打完包之后将handle以及依赖jar包直接上传到（$HIVE_HOME/lib）hive的依赖目录里


-- 不能使用load方式加载数据(insert into)使用该方式将数据加载
-- hbase中可以使用put方式生成数据

-- 2.对于hbase中存在的表，并且存在数据   创建的时候ns1:_t_userinfo   指定外部表external


-- 注意事项：
-- 映射hbase中的列，要么就写：key，要么就不写，否则列数不匹配
-- 因为默认使用：key
-- hbase中的表已存在时，在hive中创建对应的表示需要加关键字external
-- 若删除hbase中对应的数据，在hive中就不能查询出数据
-- hbase中的列和 hive中的列的数据类型 尽量相同 （hive和hbase表中的字段不是按字段名称进行匹配的）

-- 是按顺序进行匹配的  也就是字段顺序进行匹配


-- hbase、hive、mysql可以使用第三方工具 phoneix

-- 数据整合  蓝灯、shell


# hbase的高级特性

-- rowkey   解决热点问题  解决查询效率问题


-- 二级索引就是解决查询
# 协处理器
-- 二级索引，根据需求来创建
-- 查询：
-- 按时间，按地点，按数据类型，按照姓名等
# 通过以上字段创建二级索引表,设计索引表的rowkey:
# value存储原始表的rowKey


-- 一般都设计到十几到二十几的索引表 查询条件有十几种组合  都会建立索引表
-- MR就是对key进行排序  value没有排序操作

-- put请求就是java的dao层

-- dao--->put---->coprocessor---->table & --->preput 粉丝表
-- preput方法在put之前执行  没加拦截器

# 协处理器写法
-- extends baseRegionOBserver
-- prePut postPut preDelete 定义它的方法




