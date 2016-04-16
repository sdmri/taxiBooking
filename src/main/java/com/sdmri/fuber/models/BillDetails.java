package com.sdmri.fuber.models;

public class BillDetails {
	private String invoiceId;
	private double amount;
	private double distanceInKms;
	private double durationInMinutes;
	private BookingDetails bookingDetails;

	public BillDetails(String invoiceId, double amount, double distanceInKms,
			double durationInMinutes, BookingDetails bookingDetails) {
		this.invoiceId = invoiceId;
		this.amount = amount;
		this.distanceInKms = distanceInKms;
		this.durationInMinutes = durationInMinutes;
		this.bookingDetails = bookingDetails;
	}
	
	/**
	 * @return the bookingDetails
	 */
	public BookingDetails getBookingDetails() {
		return bookingDetails;
	}

	/**
	 * @return the invoiceId
	 */
	public String getInvoiceId() {
		return invoiceId;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @return the distanceInKms
	 */
	public double getDistanceInKms() {
		return distanceInKms;
	}

	/**
	 * @return the durationInMinutes
	 */
	public double getDurationInMinutes() {
		return durationInMinutes;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((bookingDetails == null) ? 0 : bookingDetails.hashCode());
		temp = Double.doubleToLongBits(distanceInKms);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(durationInMinutes);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((invoiceId == null) ? 0 : invoiceId.hashCode());
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BillDetails other = (BillDetails) obj;
		if (Double.doubleToLongBits(amount) != Double
				.doubleToLongBits(other.amount))
			return false;
		if (bookingDetails == null) {
			if (other.bookingDetails != null)
				return false;
		} else if (!bookingDetails.equals(other.bookingDetails))
			return false;
		if (Double.doubleToLongBits(distanceInKms) != Double
				.doubleToLongBits(other.distanceInKms))
			return false;
		if (Double.doubleToLongBits(durationInMinutes) != Double
				.doubleToLongBits(other.durationInMinutes))
			return false;
		if (invoiceId == null) {
			if (other.invoiceId != null)
				return false;
		} else if (!invoiceId.equals(other.invoiceId))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BillDetails [invoiceId=" + invoiceId + ", amount=" + amount
				+ ", distanceInKms=" + distanceInKms + ", durationInMinutes="
				+ durationInMinutes + ", bookingDetails=" + bookingDetails
				+ "]";
	}
}
