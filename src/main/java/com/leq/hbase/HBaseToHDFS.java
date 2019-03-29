package com.leq.hbase;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.hbase.Cell;
import org.apache.hadoop.hbase.CellScanner;
import org.apache.hadoop.hbase.CellUtil;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.io.ImmutableBytesWritable;
import org.apache.hadoop.hbase.mapreduce.TableMapReduceUtil;
import org.apache.hadoop.hbase.mapreduce.TableMapper;
import org.apache.hadoop.hbase.util.Bytes;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

import java.io.IOException;

public class HBaseToHDFS {


    private static class MyMapper extends TableMapper<Text, Text> {


        @Override
        protected void map(ImmutableBytesWritable key, Result value, Context context) throws IOException, InterruptedException {

            // 处理value
            CellScanner cellScanner = value.cellScanner();

            StringBuffer sb = new StringBuffer();
            String rowKey = Bytes.toString(value.getRow());

            while (cellScanner.advance()) {
                Cell current = cellScanner.current();
                sb.append(new String(CellUtil.cloneValue(current),"utf-8"));
                sb.append("\t");    // 获取每个单元格  每个单元格按tab来分割
            }


            String outValue = sb.toString().substring(0, sb.length() - 1);

            context.write(new Text(rowKey),new Text(outValue));

        }
    }





    public static void main(String[] args) throws IOException {

        // 1.获取configuration
        Configuration conf = new Configuration();

        // 2.设置连接参数
        // 3.连接hbase的参数

        conf.set("fs.deaultFS","file:///");

//        conf.set("mapreduce");

        conf.set("hbase.zookeeper.quorum","linux01");

        // 2.ToHDFS的连接参数
        conf.set("dfs.nameservices","gp1816");

        conf.set("dfs.ha.namenodes.gp1816","nn1,nn2");

        // nn1的RPC通信
        conf.set("dfs.namenode.rpc-address.gp1816","gp181603：9000");

        // 配置失败自动切换的方式

        conf.set("dfs.client.failover****","org.apache.hdfs.servrce.namenode");

        // 获取job对象

        Job job = Job.getInstance(conf, "hdfsToHbase");

        // 设置job的执行路径
        job.setJarByClass(HBaseToHDFS.class);

        // 设置map端实现类
        job.setMapperClass(MyMapper.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputKeyClass(Text.class);

        // 设置输入输出路径
        FileOutputFormat.setOutputPath(job,new Path(args[0]));


        TableMapReduceUtil.initTableMapperJob("sad",getScan(),MyMapper.class,Text.class,Text.class,job);

//        TableMapReduceUtil.initTableMapperJob("ns1:t_result",MyReducer.class,job);

        // 设置输入输出路径

        // 设置输路径

        FileOutputFormat.setOutputPath(job,new Path(args[0]));

    }

    private static Scan getScan() {
        Scan scan = new Scan();
        return scan;

    }

    // 1.打包时把以来也打入进去  但是我们的jar会变得很大，不是很建议
    // 大数据移动数据不如移动计算
    // 2.在执行我们的jar包前，执行export命令
    // export HADOOP_CLASSPATH = $CLASSPATH:$HBASE_HOME/lib/*.jar

    // 3.咋hadoop-env.sh里，最后将export语句添加进去  分发 重启集群
    // 4.将$HBASE_HOME/lib下的所有依赖全都copy到$HADOOP_HOME/lib
    // ---->容易引起jar包的冲突




    /*
    hive和hbase的整合
    用hbase做数据的存储
    可以使用hive进行分析
    又要使用hbase做实时。。。
     */
}
