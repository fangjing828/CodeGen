package com.speedment.codegen.lang.models.values;

import com.speedment.codegen.lang.models.Value;
import com.speedment.util.Copier;
import java.util.List;

/**
 *
 * @author Emil Forslund
 */
public class ArrayValue extends Value<List<Value>, ArrayValue> {

	public ArrayValue(List<Value> val) {
		super(val);
	}

	@Override
	public ArrayValue copy() {
		return new ArrayValue(Copier.copy(getValue()));
	}
}