package org.itmo.reducer;

import org.itmo.dto.CategorySaleDto;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SalesCalculationReducer extends Reducer<Text, CategorySaleDto, Text, Text> {

    @Override
    protected void reduce(Text key, Iterable<CategorySaleDto> values, Context context) throws IOException, InterruptedException {
        double totalSum = 0.0;
        int totalCount = 0;

        for (CategorySaleDto val : values) {
            totalSum += val.getRevenue();
            totalCount += val.getQuantity();
        }

        context.write(key, new Text(String.format("%.2f\t%d", totalSum, totalCount)));
    }

}
