package com.sdmri.fuber.models;

public class CabLocation {
	private Cab cab;
	private Coordinate coordinate;
	
	public CabLocation(Cab cab, Coordinate coordinate) {
		this.cab = cab;
		this.coordinate = coordinate;
	}
	
	/**
	 * @return the coordinate
	 */
	public Coordinate getCoordinate() {
		return coordinate;
	}

	/**
	 * @return the cab
	 */
	public Cab getCab() {
		return cab;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((cab == null) ? 0 : cab.hashCode());
		result = prime * result
				+ ((coordinate == null) ? 0 : coordinate.hashCode());
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
		CabLocation other = (CabLocation) obj;
		if (cab == null) {
			if (other.cab != null)
				return false;
		} else if (!cab.equals(other.cab))
			return false;
		if (coordinate == null) {
			if (other.coordinate != null)
				return false;
		} else if (!coordinate.equals(other.coordinate))
			return false;
		return true;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "CabLocation [cab=" + cab + ", coordinate=" + coordinate + "]";
	}
	
}
