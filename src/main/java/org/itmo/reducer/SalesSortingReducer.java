package org.itmo.reducer;

import org.itmo.dto.SalesSortValueDto;
import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class SalesSortingReducer extends Reducer<DoubleWritable, SalesSortValueDto, Text, Text> {

    @Override
    protected void reduce(DoubleWritable key, Iterable<SalesSortValueDto> values, Context context) throws IOException, InterruptedException {
        for (SalesSortValueDto value : values) {
            Text category = new Text(value.getCategory());
            context.write(category, new Text(String.format("%.2f\t%d", -1 * key.get(), value.getQuantity())));
        }
    }

}
