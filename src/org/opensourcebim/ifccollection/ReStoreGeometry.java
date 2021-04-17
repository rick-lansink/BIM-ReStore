package org.opensourcebim.ifccollection;
import java.util.Arrays;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
public class ReStoreGeometry {
	private double volume;
	private double floorArea;

	private Boolean isComplete;
	
	private Double[] principalDimensions;
	private Double[] sortedDims;
	private Double[] roundedDims;

	public ReStoreGeometry() {
		volume = Double.NaN;
		floorArea = Double.NaN;

		setIsComplete(false);
	}

	public Double getVolume() {
		return volume;
	}

	public void setVolume(Double volume) {
		this.volume = volume;
	}

	public Double getFloorArea() {
		return floorArea;
	}

	public void setFloorArea(Double floorArea) {
		this.floorArea = floorArea;
	}

	@JsonIgnore
	public Double getFaceArea() {
		return this.sortedDims[0] * this.sortedDims[1];
	}

	// return the largest axis
	@JsonIgnore
	public Double getPrincipalDimension() {
		if (this.sortedDims.length == 0) {
			return Double.NaN;
		}
	    return this.sortedDims[0];
	}
	

	public Boolean getIsComplete() {
		return isComplete;
	}

	public void setIsComplete(Boolean isComplete) {
		this.isComplete = isComplete;
	}

	public Double[] getDimensions() {
		return this.principalDimensions;
	}
	
	public Double[] getSortedDimensions() {
		return this.roundedDims;
	}

	public void setDimensions(double x_dir, double y_dir, double z_dir) {
		this.principalDimensions = new Double[] {x_dir, y_dir, z_dir};
		this.sortedDims = new Double[] {x_dir, y_dir, z_dir};
		this.roundedDims = new Double[] {
				round(x_dir, 4), round(y_dir, 4), round(z_dir, 4)
		};
		Arrays.sort(sortedDims, Collections.reverseOrder());
		Arrays.sort(roundedDims, Collections.reverseOrder());
	}
	
	public static double round(double value, int places) {
	    if (places < 0) throw new IllegalArgumentException();

	    BigDecimal bd = BigDecimal.valueOf(value);
	    bd = bd.setScale(places, RoundingMode.HALF_UP);
	    return bd.doubleValue();
	}
	

	/*
	 * Assume equal shape of the two geometries and define the dimensions of the object by scaling 
	 * the input geometry.
	 */
	public void setDimensionsByVolumeRatio(ReStoreGeometry geom) {
		if (geom.getDimensions().length != 3) {
			this.setDimensions(Double.NaN, Double.NaN, Double.NaN);
			return;
		}
		Double lengthRatio = Math.pow(this.getVolume() / geom.getVolume(), 1.0/3.0);
		this.setDimensions(geom.getDimensions()[0] * lengthRatio,
				geom.getDimensions()[1] * lengthRatio,
				geom.getDimensions()[2] * lengthRatio);
	}
}