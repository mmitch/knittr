package de.cgarbs.lib.data.type;

import de.cgarbs.lib.data.NumberAttribute;

public class FloatAttribute extends NumberAttribute
{
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;

	private transient final int minDecimals;
	private transient final int maxDecimals;

	// Builder pattern start
	public static class Builder extends NumberAttribute.Builder<Float>
	{
		public Builder setMinDecimals(int minDecimals)
		{
			this.minDecimals = minDecimals;
			return this;
		}
		public Builder setMaxDecimals(int maxDecimals)
		{
			this.maxDecimals = maxDecimals;
			return this;
		}
		public Builder setDecimals(int decimals)
		{
			return setMinDecimals(decimals).setMaxDecimals(decimals);
		}

		private int minDecimals;
		private int maxDecimals;

		public FloatAttribute build()
		{
			return new FloatAttribute(this);
		}
	}

	public static Builder builder()
	{
		return new Builder();
	}

	private FloatAttribute(Builder builder)
	{
		super(builder);
		minDecimals = builder.minDecimals;
		maxDecimals = builder.maxDecimals;
		numberFormat.setMinimumFractionDigits(minDecimals);
		numberFormat.setMaximumFractionDigits(maxDecimals);
	}
	// Builder pattern end


	@Override
	public Object getValue()
	{
		if (value == null)
		{
			return null;
		}
		return value.floatValue();
	}
}
