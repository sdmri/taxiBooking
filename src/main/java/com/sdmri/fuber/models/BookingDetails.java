package com.sdmri.fuber.models;

public class BookingDetails {
	private String id;
	private long tripStartEpoch;
	private CabLocation initialCabLocation;
	private boolean tripComplete;

	public BookingDetails(String id, long tripStartEpoch,
			CabLocation initialCabLocation) {
		this.id = id;
		this.tripStartEpoch = tripStartEpoch;
		this.initialCabLocation = initialCabLocation;
	}
	
	/**
	 * @return the tripComplete
	 */
	public boolean isTripComplete() {
		return tripComplete;
	}



	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the tripStartEpoch
	 */
	public long getTripStartEpoch() {
		return tripStartEpoch;
	}

	/**
	 * @return the initialCabLocation
	 */
	public CabLocation getInitialCabLocation() {
		return initialCabLocation;
	}
	
	public void completeTrip(){
		tripComplete = true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime
				* result
				+ ((initialCabLocation == null) ? 0 : initialCabLocation
						.hashCode());
		result = prime * result + (tripComplete ? 1231 : 1237);
		result = prime * result
				+ (int) (tripStartEpoch ^ (tripStartEpoch >>> 32));
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
		BookingDetails other = (BookingDetails) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (initialCabLocation == null) {
			if (other.initialCabLocation != null)
				return false;
		} else if (!initialCabLocation.equals(other.initialCabLocation))
			return false;
		if (tripComplete != other.tripComplete)
			return false;
		if (tripStartEpoch != other.tripStartEpoch)
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "BookingDetails [id=" + id + ", tripStartEpoch="
				+ tripStartEpoch + ", initialCabLocation=" + initialCabLocation
				+ ", tripComplete=" + tripComplete + "]";
	}
}
