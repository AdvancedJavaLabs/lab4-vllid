package org.itmo.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.apache.hadoop.io.Writable;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CategorySaleDto implements Writable {

    private double revenue;
    private int quantity;

    @Override
    public void write(DataOutput dataOutput) throws IOException {
        dataOutput.writeDouble(revenue);
        dataOutput.writeInt(quantity);
    }

    @Override
    public void readFields(DataInput dataInput) throws IOException {
        revenue = dataInput.readDouble();
        quantity = dataInput.readInt();
    }
}
