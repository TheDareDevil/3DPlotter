package munk.graph.plot;

import javax.media.j3d.LineArray;
import javax.media.j3d.Shape3D;
import javax.vecmath.Point3f;

import org.nfunk.jep.*;

public class ParametricPlotter {
	
	private final float	tMin;
	private final float	tMax;
	private float stepsize;
	
	private JEP jep;
	private Node xNode;
	private Node yNode;
	private Node zNode;
	
	public ParametricPlotter(String xExpr, String yExpr, String zExpr, float tMin, float tMax, float stepSize) throws ParseException {
		this.tMin = tMin;
		this.tMax = tMax;
		stepsize = stepSize;
		
		jep = new JEP();
		jep.addStandardFunctions();
		jep.addStandardConstants();
		jep.addVariable("t", tMin);
		jep.addVariable("x", 0);
		jep.addVariable("y", 0);
		jep.addVariable("z", 0);
		
		xNode = jep.parse(xExpr);
		yNode = jep.parse(yExpr);
		zNode = jep.parse(zExpr);
	}
	
	public ParametricPlotter(String xFunc, String yFunc, String zFunc, float tMin, float tMax) throws ParseException {
		this(xFunc, yFunc, zFunc, tMin, tMax, 0.1f);
	}
	
	public Shape3D getPlot() {
		int length = (int) ((tMax - tMin) / stepsize);
		LineArray la = new LineArray(length, LineArray.COORDINATES);
		
		float t = tMin;
		for (int i = 0; i < length; i++) {
			jep.addVariable("t", t);
			
			try {
				double xValue = (Double) jep.evaluate(xNode);
				double yValue = (Double) jep.evaluate(yNode);
				double zValue = (Double) jep.evaluate(zNode);
				la.setCoordinate(i, new Point3f((float) xValue, (float) yValue, (float) zValue));
				
			} catch (ParseException e) {
				// Does not happen
			}
			t += stepsize;
			
		}
		
		
		return new Shape3D(la);
	}

}
