package org.itmo;

import org.itmo.dto.CategorySaleDto;
import org.itmo.dto.SalesSortValueDto;
import org.itmo.mapper.SalesCalculationMapper;
import org.itmo.mapper.SalesSortingMapper;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.itmo.reducer.SalesCalculationReducer;
import org.itmo.reducer.SalesSortingReducer;

import java.io.IOException;

public class Application {

    public static void main(String[] args) throws IOException, InterruptedException, ClassNotFoundException {
        if (args.length != 4) {
            printUsage();
            System.exit(-1);
        }

        String inputDir = args[0];
        String outputDir = args[1];
        int reducersCount = Integer.parseInt(args[2]);
        int datablockSizeMb = Integer.parseInt(args[3]) * ((int) Math.pow(2, 10));
        String calculateJobResultDir = outputDir + "-mid";

        long startTime = System.currentTimeMillis();
        Configuration conf = new Configuration();
        conf.set("mapreduce.input.fileinputformat.split.maxsize", Integer.toString(datablockSizeMb));

        Job salesAnalysisJob = Job.getInstance(conf, "sales calculation");
        salesAnalysisJob.setNumReduceTasks(reducersCount);
        salesAnalysisJob.setJarByClass(Application.class);
        salesAnalysisJob.setMapperClass(SalesCalculationMapper.class);
        salesAnalysisJob.setReducerClass(SalesCalculationReducer.class);
        salesAnalysisJob.setMapOutputKeyClass(Text.class);
        salesAnalysisJob.setMapOutputValueClass(CategorySaleDto.class);
        salesAnalysisJob.setOutputKeyClass(Text.class);
        salesAnalysisJob.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(salesAnalysisJob, new Path(inputDir));
        Path intermediateOutput = new Path(calculateJobResultDir);
        FileOutputFormat.setOutputPath(salesAnalysisJob, intermediateOutput);

        boolean success = salesAnalysisJob.waitForCompletion(true);

        if (!success) {
            System.exit(1);
        }

        Job sortByValueJob = Job.getInstance(conf, "sorting by revenue");
        sortByValueJob.setJarByClass(Application.class);
        sortByValueJob.setMapperClass(SalesSortingMapper.class);
        sortByValueJob.setReducerClass(SalesSortingReducer.class);

        sortByValueJob.setMapOutputKeyClass(DoubleWritable.class);
        sortByValueJob.setMapOutputValueClass(SalesSortValueDto.class);

        sortByValueJob.setOutputKeyClass(SalesSortValueDto.class);
        sortByValueJob.setOutputValueClass(Text.class);

        FileInputFormat.addInputPath(sortByValueJob, intermediateOutput);
        FileOutputFormat.setOutputPath(sortByValueJob, new Path(outputDir));

        long endTime = System.currentTimeMillis();
        System.out.println("Jobs completed in " + (endTime - startTime) + " milliseconds.");

        System.exit(sortByValueJob.waitForCompletion(true) ? 0 : 1);
    }

    static void printUsage() {
        System.out.println("hadoop jar <jar-file path> <main-class-fullname> " +
                "[<input data dir>] " +
                "[<output data dir>] " +
                "[<reducers count>] " +
                "[<data block size in kb>] ");
    }

}
