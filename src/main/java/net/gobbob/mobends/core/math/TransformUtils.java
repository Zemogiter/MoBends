package net.gobbob.mobends.core.math;

import net.gobbob.mobends.core.math.matrix.IMat4x4d;
import net.gobbob.mobends.core.math.matrix.Mat4x4d;
import net.gobbob.mobends.core.math.vector.IVec3d;
import net.gobbob.mobends.core.math.vector.IVec3dRead;
import net.gobbob.mobends.core.math.vector.Vec3d;
import net.gobbob.mobends.core.math.vector.Vec3f;
import net.gobbob.mobends.core.math.vector.VectorUtils;

public class TransformUtils
{
	
	public static void translate(IMat4x4d src, double x, double y, double z, IMat4x4d dest)
	{
		double[] srcFields = src.getFields();
		dest.copyFrom(src);
		dest.set(3, 0, srcFields[0] * x + srcFields[4] * y + srcFields[8] * z + srcFields[12]);
		dest.set(3, 1, srcFields[1] * x + srcFields[5] * y + srcFields[9] * z + srcFields[13]);
		dest.set(3, 2, srcFields[2] * x + srcFields[6] * y + srcFields[10] * z + srcFields[14]);
		dest.set(3, 3, srcFields[3] * x + srcFields[7] * y + srcFields[11] * z + srcFields[15]);
	}
	
	public static void rotateX(IVec3dRead src, double angle, IVec3d dest)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		
		dest.setY(src.getY() * cos - src.getZ() * sin);
		dest.setZ(src.getY() * sin + src.getZ() * cos);
	}
	
	public static void rotateY(IVec3dRead src, double angle, IVec3d dest)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		
		dest.setX( src.getX() * cos + src.getZ() * sin);
		dest.setZ(-src.getX() * sin + src.getZ() * cos);
	}
	
	public static void rotateZ(IVec3dRead src, double angle, IVec3d dest)
	{
		final double cos = Math.cos(angle);
		final double sin = Math.sin(angle);
		
		dest.setX(src.getX() * cos - src.getY() * sin);
		dest.setY(src.getX() * sin + src.getY() * cos);
	}
	
	public static void rotateX(IVec3d vec, double angle)
	{
		rotateX(vec, angle, vec);
	}
	
	public static void rotateY(IVec3d vec, double angle)
	{
		rotateY(vec, angle, vec);
	}
	
	public static void rotateZ(IVec3d vec, double angle)
	{
		rotateZ(vec, angle, vec);
	}
	
	public static void rotate(IMat4x4d src, double angle, double axisX, double axisY, double axisZ, IMat4x4d dest)
	{
		final double[] srcFields = src.getFields();
		
		double a = angle;
		double c = Math.cos(a);
		double s = Math.sin(a);

		double tempX = axisX * (1 - c);
		double tempY = axisY * (1 - c);
		double tempZ = axisZ * (1 - c);
		//Vec3d temp = VectorUtils.getScaled(naxis, 1 - c);

		double[] rotate = new double[] {
			// First column
			c + tempX * axisX,
			tempX * axisY + s * axisZ,
			tempX * axisZ - s * axisY,
			0,
			
			// Second column
			tempY * axisX - s * axisZ,
			c + tempY * axisY,
			tempY * axisZ + s * axisX,
			0,
			
			// Third column
			tempZ * axisX + s * axisY,
			tempZ * axisY - s * axisX,
			c + tempZ * axisZ,
			0,
			
			// Fourth column
			0, 0, 0, 0
		};
		
		dest.setFields(
			srcFields[0] * rotate[0] + srcFields[4] * rotate[1] + srcFields[8] * rotate[2],
			srcFields[1] * rotate[0] + srcFields[5] * rotate[1] + srcFields[9] * rotate[2],
			srcFields[2] * rotate[0] + srcFields[6] * rotate[1] + srcFields[10] * rotate[2],
			srcFields[3] * rotate[0] + srcFields[7] * rotate[1] + srcFields[11] * rotate[2],
			
			srcFields[0] * rotate[4] + srcFields[4] * rotate[5] + srcFields[8] * rotate[6],
			srcFields[1] * rotate[4] + srcFields[5] * rotate[5] + srcFields[9] * rotate[6],
			srcFields[2] * rotate[4] + srcFields[6] * rotate[5] + srcFields[10] * rotate[6],
			srcFields[3] * rotate[4] + srcFields[7] * rotate[5] + srcFields[11] * rotate[6],
			
			srcFields[0] * rotate[8] + srcFields[4] * rotate[9] + srcFields[8] * rotate[10],
			srcFields[1] * rotate[8] + srcFields[5] * rotate[9] + srcFields[9] * rotate[10],
			srcFields[2] * rotate[8] + srcFields[6] * rotate[9] + srcFields[10] * rotate[10],
			srcFields[3] * rotate[8] + srcFields[7] * rotate[9] + srcFields[11] * rotate[10],
			
			srcFields[12], srcFields[13], srcFields[14], srcFields[15]
		);
	}
	
	public static void rotate(IMat4x4d src, double angle, IVec3dRead axis, IMat4x4d dest)
	{
		rotate(src, angle, axis.getX(), axis.getY(), axis.getZ(), dest);
	}
	
	public static void rotate(double angle, IVec3dRead axis, IMat4x4d dest)
	{
		rotate(Mat4x4d.ONE, angle, axis, dest);
	}
	
}