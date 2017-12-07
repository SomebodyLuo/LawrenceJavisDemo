uniform mat4 uMVPMatrix; //总变换矩阵
uniform mat4 uMMatrix; //模型视图矩阵

uniform vec3 uLightLocation;

attribute vec2 aTexCoor;    //顶点纹理坐标
attribute vec3 aPosition;  //顶点位置
attribute vec4 aColor;
attribute vec3 aNormal;

varying vec2 vTextureCoord_;  //用于传递给片元着色器的变量
varying vec4 vAmbient; //用于传递给片元着色器的环境光变量
varying vec4 vDiffuse; //用于传递给片元着色器的漫反射变量
varying vec4 vColor;
 
void main()     
{                            		
      
   vTextureCoord_ = aTexCoor;//将接收的纹理坐标传递给片元着色器
//   vColor = aColor;
   gl_Position = uMVPMatrix * vec4(aPosition,1); //根据总变换矩阵计算此次绘制此顶点位置
   
}                      