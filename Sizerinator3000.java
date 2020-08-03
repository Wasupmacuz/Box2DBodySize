package tools;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.EdgeShape;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;

/**
 * Finds the size (width and height) of a body by checking its fixtures' shapes.
 */
public class Sizerinator3000
{
	/**
	 * Finds the width and height of a given body.
	 * @param body the body to find the size of.
	 * @return the size, in a Vector2.
	 */
	public static Vector2 getBodySize(Body body)
	{
		float maxTop = 0, maxRight = 0, maxBottom = 0, maxLeft = 0;
		for(Fixture f : body.getFixtureList())
		{
			MyVector4 vec = outerVals(f);
			if(vec.a > maxTop)
				maxTop = vec.a;
			if(vec.b > maxRight)
				maxRight = vec.b;
			if(vec.c > maxBottom)
				maxBottom = vec.c;
			if(vec.d > maxLeft)
				maxLeft = vec.d;
		}
		
		return new Vector2(Math.abs(maxRight - maxLeft), Math.abs(maxTop - maxBottom));
	}
	
	/**
	 * Finds the width and height of a given body, then scales it.<br>Calling this method is the same as calling Sizerinator3000.getBodySize(body).scl(scale)
	 * @param body the body to find the size of.
	 * @param scale the scaling factor (ex. your Pixels Per Meter scale). 
	 * @return the size, in a Vector2, scaled by the scaling factor.
	 */
	public static Vector2 getBodySize(Body body, float scale)
	{
		float maxTop = 0, maxRight = 0, maxBottom = 0, maxLeft = 0;
		for(Fixture f : body.getFixtureList())
		{
			MyVector4 vec = outerVals(f).scale(scale);
			if(vec.a > maxTop)
				maxTop = vec.a;
			if(vec.b > maxRight)
				maxRight = vec.b;
			if(vec.c > maxBottom)
				maxBottom = vec.c;
			if(vec.d > maxLeft)
				maxLeft = vec.d;
		}
		
		return new Vector2(Math.abs(maxRight - maxLeft), Math.abs(maxTop - maxBottom));
	}

	/**
	 * Changing which method is called based on the fixture's shape.
	 * @param fixture the current fixture being analyzed.
	 * @return the outermost points of the fixture's shape.
	 */
	private static MyVector4 outerVals(Fixture fixture)
	{
		Shape shape = fixture.getShape();
		Shape.Type type = shape.getType();
		MyVector4 size = new MyVector4();
		switch(type)
		{
		case Polygon: 
		size = shapeVals((PolygonShape)shape);
		break;
		case Chain: 
		size = shapeVals((ChainShape)shape);
		break;
		case Edge: 
		size = shapeVals((EdgeShape)shape);
		break;
		case Circle: 
		size = shapeVals((CircleShape)shape);
		break;
		}
		
		return size;
	}

	/**
	 * Calculates the outermost points for a PolygonShape.
	 * @param s the shape.
	 * @return the maximum upper, lower, left, and right values; given by the vertices.
	 */
	private static MyVector4 shapeVals(PolygonShape s)
	{
		MyVector4 size = new MyVector4(); // top, right, bottom, left
		for(int i = 0; i < s.getVertexCount(); i++)
		{
			Vector2 probe = new Vector2();
			s.getVertex(i, probe);
			if(probe.x > size.b) // right
				size.b = probe.x;
			if(probe.x < size.d) // left
				size.d = probe.x;
			
			if(probe.y > size.a) // top
				size.a = probe.y;
			if(probe.y < size.c) // bottom
				size.c = probe.y;
		}
		return size;
	}

	/**
	 * Calculates the outermost points for a ChainShape.
	 * @param s the shape.
	 * @return the maximum upper, lower, left, and right values; given by the vertices.
	 */
	private static MyVector4 shapeVals(ChainShape s)
	{
		MyVector4 size = new MyVector4(); // top, right, bottom, left
		for(int i = 0; i < s.getVertexCount(); i++)
		{
			Vector2 probe = new Vector2();
			s.getVertex(i, probe);
			if(probe.x > size.b) // right
				size.b = probe.x;
			if(probe.x < size.d) // left
				size.d = probe.x;
			
			if(probe.y > size.a) // top
				size.a = probe.y;
			if(probe.y < size.c) // bottom
				size.c = probe.y;
		}
		return size;
	}

	/**
	 * Calculates the outermost points for an EdgeShape.
	 * @param s the shape.
	 * @return the maximum upper, lower, left, and right values; given by the vertices.
	 */
	private static MyVector4 shapeVals(EdgeShape s)
	{
		MyVector4 size = new MyVector4(); // top, right, bottom, left
		Vector2 probe = new Vector2();
		s.getVertex1(probe);
		if(probe.x > size.b) // right
			size.b = probe.x;
		if(probe.x < size.d) // left
			size.d = probe.x;
		
		if(probe.y > size.a) // top
			size.a = probe.y;
		if(probe.y < size.c) // bottom
			size.c = probe.y;

		s.getVertex2(probe);
		if(probe.x > size.b) // right
			size.b = probe.x;
		if(probe.x < size.d) // left
			size.d = probe.x;
		
		if(probe.y > size.a) // top
			size.a = probe.y;
		if(probe.y < size.c) // bottom
			size.c = probe.y;
		
		return size;
	}

	/**
	 * Calculates the outermost points for a CircleShape.
	 * @param s the shape.
	 * @return the maximum upper, lower, left, and right values; given by radius and position.
	 */
	private static MyVector4 shapeVals(CircleShape s)
	{
		MyVector4 size = new MyVector4(); // top, right, bottom, left
		size.a = s.getPosition().y + s.getRadius(); // top
		size.b = s.getPosition().x + s.getRadius(); // right
		size.c = s.getPosition().y - s.getRadius(); // bottom
		size.d = s.getPosition().x - s.getRadius(); // left
		
		return size;
	}
	
	/**
	 * using this just to store the variables easily.
	 */
	private static class MyVector4
	{
		public float a, b, c, d;
		
		public MyVector4()
		{
			this.a = 0;
			this.b = 0;
			this.c = 0;
			this.d = 0;
		}
		
		@SuppressWarnings("unused")
		public MyVector4(float a, float b, float c, float d)
		{
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
		
		@SuppressWarnings("unused")
		public void set(float a, float b, float c, float d)
		{
			this.a = a;
			this.b = b;
			this.c = c;
			this.d = d;
		}
		
		public MyVector4 scale(float val)
		{
			this.a *= val;
			this.b *= val;
			this.c *= val;
			this.d *= val;
			
			return this;
		}
	}
}
